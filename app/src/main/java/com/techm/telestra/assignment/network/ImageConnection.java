package com.techm.telestra.assignment.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class ImageConnection extends BaseConnection implements HttpConnection {
    public ImageConnection(Context context, URL url) {
        super(context, url);
    }

    public ImageConnection(Context context, URL url, ConnectionSetting connectionSetting) {
        super(context, url, connectionSetting);
    }

    @Override
    public void doGetRequest() throws NetworkException {
        try {
            mConnection = getUrlConnection(mConnectionSetting);
            mConnection.setUseCaches(true);

            Object response = mConnection.getContent();
            if (response != null) {
                boolean redirect = false;

                // normally, 3xx is redirect
                int status = mConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    if (status == HttpURLConnection.HTTP_MOVED_TEMP
                            || status == HttpURLConnection.HTTP_MOVED_PERM
                            || status == HttpURLConnection.HTTP_SEE_OTHER)
                        redirect = true;

                    if (redirect) {
                        // get redirect url from "location" header field
                        String newUrl = mConnection.getHeaderField("Location");
                        mUrl = new URL(newUrl);
                        doGetRequest();
                        return;
                    }
                }
                mResponse = (Bitmap) BitmapFactory.decodeStream((InputStream) response);
                Log.i("@@@", "Image cache");
                return;
            }

            Log.i("@@@", "Downloading image");
            super.doGetRequest();
            mResponse = (Bitmap) BitmapFactory.decodeStream(mInputStream);
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public Object getResponse() {
        return mResponse;
    }
}
