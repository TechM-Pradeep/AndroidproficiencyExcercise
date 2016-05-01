package com.techm.telestra.assignment.feed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techm.telestra.assignment.ParserFactory;
import com.techm.telestra.assignment.R;
import com.techm.telestra.assignment.Urls;
import com.techm.telestra.assignment.network.HttpConnection;
import com.techm.telestra.assignment.network.NetworkException;
import com.techm.telestra.assignment.network.StringConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by PP00344204 on 5/1/2016.
 * <p/>
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class FeedFragment extends Fragment {
    public static final String TAG = FeedFragment.class.getSimpleName();
    private RecyclerView mLstFeeds;
    private ProgressDialog mProgressDialog;
    private boolean mHasServerConnectionStarted;
    private SwipeRefreshLayout mRefresh;

    public static FeedFragment getInstance(Bundle args) {
        FeedFragment fragment = new FeedFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        mLstFeeds = (RecyclerView) view.findViewById(R.id.lst_feed);
        configurePullToRefresh(view);
        getFeeds();
        onServerConnectionStart();
        return view;
    }

    private void populateList(RecyclerView recyclerView, List<FeedProperty> feedProperties) {
        //Setting the layout managers as linear
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // There are 4 different types of views which will be displayed according to the data in the list.
        FeedPropertyAdapter adapter = new FeedPropertyAdapter(getActivity(), feedProperties);
        recyclerView.setAdapter(adapter);
    }

    private void getFeeds() {
        final Context context = getActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL feedsUrl = null;
                try {
                    feedsUrl = Urls.getFeedsURL();
                } catch (MalformedURLException e) {
                    Log.e(TAG, e.getMessage());
                    onServerConnectComplete();
                }
                /** Data fetched from server can be done using voley and retrofit also
                 * over here created own connection to handle the same only thing missing is Thread pool
                 * which can be achieved by ThreadPoolExecutor instance that manages a pool of thread with a task queue
                 * and communication is done using Handler.
                 **/

                HttpConnection<String> connection = new StringConnection<StringConnection>(context, feedsUrl);
                try {
                    connection.doGetRequest();
                    String response = connection.getResponse();

                    Log.i(TAG, response);
                    // parsing the response
                    ParserFactory factory = ParserFactory.getFactory(getActivity(), ParserFactory.JSON);
                    final Feed feed = factory.getFeedParser().parseFeed(response);
                    Log.i(TAG, feed.getFeedProperties().get(0).getDescription() + "");
                    onServerConnectComplete();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Screen title is updated on the Tool bar
                            updateTitle(feed.getTitle());
                            // Populating the list of feed properties
                            populateList(mLstFeeds, feed.getFeedProperties());
                        }
                    });
                } catch (NetworkException e) {
                    Log.e(TAG, e.getMessage());
                    if (e.getMessage().equals(NetworkException.NO_INTERNET)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showNoInternetMessage();
                            }
                        });
                    }
                    onServerConnectComplete();
                }
            }
        }).start();
    }

    private void updateTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    private void showNoInternetMessage() {
        Toast.makeText(getActivity(), getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                getFeeds();
                onServerConnectionStart();
                break;
        }
        return true;
    }

    private void onServerConnectionStart() {
        mHasServerConnectionStarted = true;
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.lodaing), true);
        } else {
            mProgressDialog.show();
        }
    }

    private void onServerConnectComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHasServerConnectionStarted = false;
                mProgressDialog.dismiss();
            }
        });
    }

    private void configurePullToRefresh(View view) {
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh_fragment_feed);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeeds();
                mHasServerConnectionStarted = true;
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }
}
