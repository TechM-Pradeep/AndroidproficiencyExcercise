package com.techm.telestra.assignment.network;

import android.content.Context;

import java.io.IOException;
import java.net.URL;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class StringConnection<T> extends BaseConnection implements HttpConnection {
    public StringConnection(Context context, URL url) {
        super(context,url);
    }

    public StringConnection(Context context, URL url, ConnectionSetting connectionSetting) {
        super(context,url, connectionSetting);
    }

    @Override
    public void doGetRequest() throws NetworkException {
        super.doGetRequest();
        extractStringFromInputStream();
    }

    @Override
    public void doPostRequest() throws NetworkException {
        super.doPostRequest();
        extractStringFromInputStream();
    }

    private void extractStringFromInputStream() throws NetworkException {
        try {
            mResponse = NetworkUtil.convertInputStreamToString(mInputStream);
            mInputStream.close();
        } catch (IOException e) {
            throw new NetworkException("extractStringFromInputStream ,IOException " + e.getMessage());
        } finally {
            close();
            mConnection = null;
        }
    }

    @Override
    public Object getResponse() {
        return mResponse;
    }
}
