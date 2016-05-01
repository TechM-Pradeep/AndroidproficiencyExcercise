package com.techm.telestra.assignment.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class Feed implements Parcelable {
    private String title;
    @SerializedName(value = "rows")
    private List<FeedProperty> feedProperties;

    public Feed() {
    }

    protected Feed(Parcel in) {
        title = in.readString();
        feedProperties = in.createTypedArrayList(FeedProperty.CREATOR);
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FeedProperty> getFeedProperties() {
        return feedProperties;
    }

    public void setFeedProperties(List<FeedProperty> feedProperties) {
        this.feedProperties = feedProperties;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedList(feedProperties);
    }
}
