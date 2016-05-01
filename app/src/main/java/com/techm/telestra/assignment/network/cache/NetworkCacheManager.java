package com.techm.telestra.assignment.network.cache;

import android.net.http.HttpResponseCache;
import android.os.Build;

import java.io.File;
import java.io.IOException;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class NetworkCacheManager {
    public static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB
    private static NetworkCacheManager cacheManager;

    public static NetworkCacheManager getInstance() {
        if (cacheManager == null) {
            cacheManager = new NetworkCacheManager();
        }
        return cacheManager;
    }

    public void start(File cacheDirLocation, long cacheSize) throws NetworkCacheException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            File cacheDir = new File(cacheDirLocation, "http");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                try {
                    HttpResponseCache.install(cacheDir, cacheSize);
                } catch (IOException e) {
                    throw new NetworkCacheException("HTTP response cache installation failed: " + e.getMessage());
                }
            }
        } else {
            try {
                Class.forName("android.net.http.HttpResponseCache")
                        .getMethod("install", File.class, long.class)
                        .invoke(null, cacheDirLocation, cacheSize);
            } catch (Exception e) {
                throw new NetworkCacheException("HTTP response cache is unavailable: " + e.getMessage());
            }
        }


    }

    public void stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if (cache != null) {
                cache.flush();
            }
        }
    }
}
