package com.techm.telestra.assignment;

import android.app.Application;
import android.util.Log;

import com.techm.telestra.assignment.network.cache.NetworkCacheException;
import com.techm.telestra.assignment.network.cache.NetworkCacheManager;

/**
 * Created by PP00344204 on 5/1/2016.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class TelestraApplication extends Application {
    private static final String TAG = TelestraApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //Starting network caching
        try {
            NetworkCacheManager.getInstance().start(getCacheDir(), NetworkCacheManager.DEFAULT_CACHE_SIZE);
        } catch (NetworkCacheException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkCacheManager.getInstance().stop();
    }
}
