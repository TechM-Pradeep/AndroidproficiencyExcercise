package com.techm.telestra.assignment;

import android.content.Context;

import com.techm.telestra.assignment.feed.FeedJsonParser;
import com.techm.telestra.assignment.feed.FeedParser;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class JsonParserFactory extends ParserFactory {
    private Context mContext;

    public JsonParserFactory(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public FeedParser getFeedParser() {
        return new FeedJsonParser();
    }
}
