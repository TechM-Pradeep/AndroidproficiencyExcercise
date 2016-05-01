package com.techm.telestra.assignment.network;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class BaseConnection<T> implements HttpConnection<T> {

    private static final String FORM_BOUNDARY = "*****";
    private static final String END_OF_LINE = "\r\n";
    private static final String TAG = BaseConnection.class.getSimpleName();

    protected HttpURLConnection mConnection;
    protected URL mUrl;
    protected InputStream mInputStream;
    protected ConnectionSetting mConnectionSetting;
    protected T mResponse;
    protected String mParam;
    protected Context mContext;
    protected Map<String, byte[]> mFileParts;

    public BaseConnection(Context context, URL url) {
        this.mUrl = url;
        this.mContext = context;
    }

    public BaseConnection(Context context, URL url, ConnectionSetting connectionSetting) {
        this(context, url);
        this.mConnectionSetting = connectionSetting;
    }

    @Override
    public void doGetRequest() throws NetworkException {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            throw new NetworkException(NetworkException.NO_INTERNET);
        }

        try {
            mConnection = getUrlConnection(mConnectionSetting);
            mConnection.setRequestMethod("GET");

            //Start querying
            mConnection.connect();

            int responseCode = mConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                mInputStream = mConnection.getInputStream();
            } else {
                throw new NetworkException(responseCode + "");
            }
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    @Override
    public void doPostRequest() throws NetworkException {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            throw new NetworkException(NetworkException.NO_INTERNET);
        }

        try {
            if (mConnection == null) {
                mConnection = getUrlConnection(mConnectionSetting);
            }
            mConnection.setDoOutput(true);
            mConnection.setDoInput(true);

            /*For best performance, you should call either setFixedLengthStreamingMode(int) when the body
            length is known in advance, or setChunkedStreamingMode(int) when it is not. Otherwise
            HttpURLConnection will be forced to buffer the complete request body in memory before it is
            transmitted, wasting (and possibly exhausting) heap and increasing latency.

            the length to use, or 0 for the default chunk length.
            */

//            mConnection.setChunkedStreamingMode(0);

            OutputStream outputStream = mConnection.getOutputStream();

            DataOutputStream writer = new DataOutputStream(outputStream);
            if (mParam != null) {
                Log.i(TAG, mParam);
                writer.writeBytes(mParam);
            }

            if (mFileParts != null) {
                for (Map.Entry<String, byte[]> entry : mFileParts.entrySet()) {
                    writer.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "" + END_OF_LINE);
                    writer.write(entry.getValue());
                    writer.writeBytes(END_OF_LINE);
                }
            }
            writer.flush();
            writer.close();
            outputStream.close();

            int responseCode = mConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                mInputStream = mConnection.getInputStream();
            } else {
                throw new NetworkException(responseCode + "");
            }
        } catch (IOException e) {
            throw new NetworkException("doPostRequest IOException " + e.getMessage());
        }
    }

    @Override
    public void addPayload(Object payload) {
        if (payload instanceof Map) {
            if (mConnection == null) {
                Map<String, String> params = (Map<String, String>) payload;
                StringBuilder result = new StringBuilder();
                boolean firstCharacter = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (firstCharacter) {
                        firstCharacter = false;
                    } else {
                        result.append("&");
                    }
                    String key = entry.getKey();
                    if (key != null) {
                        result.append(key);
                        result.append("=");
                    }

                    String value = entry.getValue();
                    if (value != null) {
                        result.append(value);
                    }
                }
                mParam = result.toString();
            }
        } else if (payload instanceof String) {
            mParam = (String) payload;
        }
    }

    @Override
    public void addFilePart(Map<String, byte[]> fileParts) throws NetworkException {
        try {
            if (mConnection == null) {
                mConnection = getUrlConnection(mConnectionSetting);
            }

            mConnection.setRequestProperty("Connection", "Keep-Alive");
            mConnection.setRequestProperty("Cache-Control", "no-cache");
            mConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + FORM_BOUNDARY);

            mFileParts = fileParts;
        } catch (IOException e) {
            throw new NetworkException("doPostRequest IOException " + e.getMessage());
        }
    }

    @Override
    public void close() {
        mConnection.disconnect();
    }

    protected HttpURLConnection getUrlConnection(ConnectionSetting
                                                         connectionSetting) throws IOException {
        Log.i("@@@", mUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
        if (connectionSetting == null) {
            connectionSetting = new ConnectionSetting();
        }
        connection.setReadTimeout(connectionSetting.getReadTimeout());
        connection.setConnectTimeout(connectionSetting.getConnectionTimeout());
        connection.setDoInput(connectionSetting.isDoInput());

        if (connectionSetting.getProperties().size() > 0) {
            for (Map.Entry<String, String> entry : connectionSetting.getProperties().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return connection;
    }

    @Override
    public T getResponse() {
        return null;
    }
}