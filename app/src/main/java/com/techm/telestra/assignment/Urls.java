package com.techm.telestra.assignment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class Urls {
    private static final String DOMAIN = "https://dl.dropboxusercontent.com";
    private static final String SLASH = "/";

    public static String getFeedsUrl() {
        String feedsUrl = "u/746330/facts.json";
        return DOMAIN + SLASH + feedsUrl;
    }

    public static URL getFeedsURL() throws MalformedURLException {
        URL url = new URL(Urls.getFeedsUrl());
        return url;
    }
}
