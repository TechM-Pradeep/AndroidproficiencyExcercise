package com.techm.telestra.assignment.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class FeedProperty implements Parcelable {
    private String title;
    private String description;
    @SerializedName(value = "imageHref")
    private String image;

    public FeedProperty() {
    }

    protected FeedProperty(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<FeedProperty> CREATOR = new Creator<FeedProperty>() {
        @Override
        public FeedProperty createFromParcel(Parcel in) {
            return new FeedProperty(in);
        }

        @Override
        public FeedProperty[] newArray(int size) {
            return new FeedProperty[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
    }

    public boolean isEmpty() {
        return (title == null && description == null && image == null);
    }

    public boolean isImageEmpty() {
        return image == null;
    }

    public boolean isDescriptionEmpty() {
        return description == null;
    }
}
