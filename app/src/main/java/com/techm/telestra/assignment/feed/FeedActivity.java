package com.techm.telestra.assignment.feed;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.techm.telestra.assignment.R;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class FeedActivity extends AppCompatActivity {
    private static final String TAG = FeedActivity.class.getSimpleName();
    private FeedFragment mFeedFragment;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initializeActionBar();
        // Fragment is used to display the feeds as fragment will be useful in multi screen support.
        showFeedFragment();
    }

    private void showFeedFragment() {
        if (mFeedFragment == null) {
            mFeedFragment = FeedFragment.getInstance(null);
        }
        if (!mFeedFragment.isVisible()) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_activity_feed, mFeedFragment,
                    FeedFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void initializeActionBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar_feed);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
