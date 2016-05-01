package com.techm.telestra.assignment.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class ConnectionSetting {
    private int connectionTimeout = 10000; // milliseconds
    private int readTimeout = 15000;  //milliseconds
    private boolean doInput = true;
    private Map<String, String> properties;

    public ConnectionSetting() {
        properties = new HashMap<>();
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isDoInput() {
        return doInput;
    }

    public void setDoInput(boolean doInput) {
        this.doInput = doInput;
    }

    public String getPropertie(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Map<String, String> getProperties() {
        return properties;
    }


    public enum Property {
        CACHE_CONTROL, MAX_STALE, CONTENT_TYPE {
            @Override
            public String toString() {
                return "Content-Type";
            }
        };

        @Override
        public String toString() {
            switch (this) {
                case CACHE_CONTROL:
                    return "Cache-Control";
                case MAX_STALE:
                    return "max-stale";
            }
            return null;
        }
    }
}
