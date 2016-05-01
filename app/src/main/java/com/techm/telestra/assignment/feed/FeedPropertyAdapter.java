package com.techm.telestra.assignment.feed;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techm.telestra.assignment.R;
import com.techm.telestra.assignment.network.NetworkUtil;
import com.techm.telestra.assignment.network.cache.ImageLoader;

import java.util.List;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class FeedPropertyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ALL = 0; // when title,description and image is not null
    private static final int VIEW_TYPE_WITH_IMAGE = 1; // when descriptio is null
    private static final int VIEW_TYPE_WITH_DESCRIPTION = 2;// when image is null
    private static final int VIEW_TYPE_EMPTY = 3; // when all the fields are null

    private List<FeedProperty> mFeedProperties;
    private Context mContext;
    private ImageLoader mImageLoader;

    public FeedPropertyAdapter(Context context, List<FeedProperty> feedProperties) {
        this.mFeedProperties = feedProperties;
        this.mContext = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        // Switching between different views with respect to value received
        switch (viewType) {
            case VIEW_TYPE_ALL:
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_property_all, parent, false);
                viewHolder = new ViewHolder1(view);
                break;
            case VIEW_TYPE_WITH_DESCRIPTION:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_property_description, parent, false);
                viewHolder = new ViewHolder2(view);
                break;
            case VIEW_TYPE_WITH_IMAGE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_property_image, parent, false);
                viewHolder = new ViewHolder3(view);
                break;
            case VIEW_TYPE_EMPTY:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_empty, parent, false);
                viewHolder = new ViewHolder4(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedProperty feedProperty = mFeedProperties.get(position);
        if (holder instanceof ViewHolder1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.mLblTitle.setText(feedProperty.getTitle());
            viewHolder1.mLblDescription.setText(feedProperty.getDescription());
            // lazy loading of image
            mImageLoader.displayImage(feedProperty.getImage(), viewHolder1.mImgIcon);
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.mLblTitle.setText(feedProperty.getTitle());
            viewHolder2.mLblDescription.setText(feedProperty.getDescription());
        } else if (holder instanceof ViewHolder3) {
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            viewHolder3.mLblTitle.setText(feedProperty.getTitle());
            // lazy loading of image
            mImageLoader.displayImage(feedProperty.getImage(), viewHolder3.mImgIcon);
        }
    }

    @Override
    public int getItemCount() {
        return this.mFeedProperties.size();
    }

    @Override
    public int getItemViewType(int position) {
        FeedProperty feedProperty = mFeedProperties.get(position);
        if (feedProperty.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else if (feedProperty.isImageEmpty()) {
            return VIEW_TYPE_WITH_DESCRIPTION;
        } else if (feedProperty.isDescriptionEmpty()) {
            return VIEW_TYPE_WITH_IMAGE;
        }

        return VIEW_TYPE_ALL;
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView mLblTitle;
        public TextView mLblDescription;
        public ImageView mImgIcon;

        public ViewHolder1(View itemView) {
            super(itemView);
            mLblTitle = (TextView) itemView.findViewById(R.id.lbl_item_feed_title);
            mLblDescription = (TextView) itemView.findViewById(R.id.lbl_item_feed_description);
            mImgIcon = (ImageView) itemView.findViewById(R.id.img_item_feed_icon);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView mLblTitle;
        public TextView mLblDescription;

        public ViewHolder2(View itemView) {
            super(itemView);
            mLblTitle = (TextView) itemView.findViewById(R.id.lbl_item_feed_title);
            mLblDescription = (TextView) itemView.findViewById(R.id.lbl_item_feed_description);
        }
    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder {
        public TextView mLblTitle;
        public ImageView mImgIcon;

        public ViewHolder3(View itemView) {
            super(itemView);
            mLblTitle = (TextView) itemView.findViewById(R.id.lbl_item_feed_title);
            mImgIcon = (ImageView) itemView.findViewById(R.id.img_item_feed_icon);
        }
    }

    public static class ViewHolder4 extends RecyclerView.ViewHolder {
        public ImageView mImgIcon;

        public ViewHolder4(View itemView) {
            super(itemView);
            mImgIcon = (ImageView) itemView.findViewById(R.id.img_empty);
        }
    }
}
