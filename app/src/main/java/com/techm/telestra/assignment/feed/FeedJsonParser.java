package com.techm.telestra.assignment.feed;

import com.google.gson.Gson;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class FeedJsonParser implements FeedParser {

    @Override
    public Feed parseFeed(String data) {
        // GSON library is used to parse the data
        Gson gson = new Gson();
        Feed feed = gson.fromJson(data, Feed.class);
        return feed;
    }
}
