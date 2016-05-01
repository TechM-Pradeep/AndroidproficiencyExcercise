package com.techm.telestra.assignment;

import android.content.Context;

import com.techm.telestra.assignment.feed.FeedParser;
/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public abstract class ParserFactory {
    public static final int JSON = 1;
    public static final int XML = 2;

    abstract public FeedParser getFeedParser();

    public static ParserFactory getFactory(Context context, int type) {
        switch (type) {
            case JSON:
                return new JsonParserFactory(context);
        }

        return null;
    }
}
