package com.techm.telestra.assignment.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public final class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        ;
        return false;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8192);
        String strLine = null;
        while ((strLine = reader.readLine()) != null) {
            response.append(strLine);
        }
        reader.close();

        return response.toString();
    }

    public static boolean isInternetAvailable() {
        try {
            String pingStr = "ping -c 1    techmahindra.com";
            Process process = Runtime.getRuntime().exec(pingStr);
            int returnVal = process.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                return reachable;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return false;
    }

    public static class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private static final String TAG = ImageDownloaderTask.class.getSimpleName();
        private WeakReference<ProgressBar> progressBarWeakReference;
        private WeakReference<ImageView> imageViewWeakReference;
        private WeakReference<List<ViewGroup>> backgroundGroupWeakRefrence;
        private WeakReference<List<View>> titlesWeakRefrence;
        private int placeHolderImageId;
        private Context mContext;

        public ImageDownloaderTask(Context context, ImageView imageView, int placeHolderImageId) {
            this.mContext = context;
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            this.placeHolderImageId = placeHolderImageId;
        }

        public ImageDownloaderTask(Context context, ImageView imageView, List<ViewGroup> backgroundGroup, List<View> titles, int placeHolderImageId) {
            this(context, imageView, placeHolderImageId);
            backgroundGroupWeakRefrence = new WeakReference<List<ViewGroup>>(backgroundGroup);
            titlesWeakRefrence = new WeakReference<List<View>>(titles);
        }

        public ImageDownloaderTask(Context context, ImageView imageView, ProgressBar progressBar, int placeHolderImageId) {
            this(context, imageView, placeHolderImageId);
            this.progressBarWeakReference = new WeakReference<ProgressBar>(progressBar);

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                HttpConnection<Bitmap> connection = new ImageConnection(mContext, new URL(strings[0]));
                connection.doGetRequest();
                bitmap = (Bitmap) connection.getResponse();
            } catch (NetworkException e) {
                String msg = e.getMessage();
                if (msg != null) {
                    Log.e(TAG, e.getMessage());
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewWeakReference != null) {
                ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    if (progressBarWeakReference != null) {
                        ProgressBar progressBar = progressBarWeakReference.get();
                        progressBar.setVisibility(View.GONE);
                    }
                    if (bitmap != null) {
                        imageView.setTag(true);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(placeHolderImageId);
                        imageView.setImageDrawable(placeholder);
                    }

                    if (backgroundGroupWeakRefrence != null && titlesWeakRefrence != null) {
                        List<ViewGroup> backgrounds = backgroundGroupWeakRefrence.get();
                        List<View> titles = titlesWeakRefrence.get();

                        try {
                            Palette palette = Palette.from(bitmap).generate();
                            Palette.Swatch vibrant =
                                    palette.getVibrantSwatch();
                            if (vibrant != null) {
                                if (backgrounds != null) {
                                    int rgb = vibrant.getRgb();
                                    float alpha = 0.9f;
                                    for (ViewGroup viewGroup : backgrounds) {
                                        viewGroup.setBackgroundColor(rgb);
                                        viewGroup.setAlpha(alpha);
                                    }
                                }

                                if (titles != null) {
                                    for (View view : titles) {
                                        TextView textview = ((TextView) view);
                                        textview.setTextColor(vibrant.getTitleTextColor());

                                        Drawable[] drawables = textview.getCompoundDrawables();
                                        Drawable drawableLeft = null;
                                        Drawable drawableTop = null;
                                        Drawable drawableRight = null;
                                        Drawable drawableBottom = null;

                                        if (drawables != null) {
                                            for (int i = 0; i < 4; i++) {
                                                switch (i) {
                                                    case 0:
                                                        if (drawables[0] != null) {
                                                            drawableLeft = drawables[0].mutate();
                                                            drawableLeft.setColorFilter(vibrant.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                                                        }
                                                        break;
                                                    case 1:
                                                        if (drawables[1] != null) {
                                                            drawableTop = drawables[1].mutate();
                                                            drawableTop.setColorFilter(vibrant.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                                                        }
                                                        break;
                                                    case 2:
                                                        if (drawables[2] != null) {
                                                            drawableRight = drawables[2].mutate();
                                                            drawableRight.setColorFilter(vibrant.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                                                        }
                                                        break;
                                                    case 3:
                                                        if (drawables[3] != null) {
                                                            drawableBottom = drawables[3].mutate();
                                                            drawableBottom.setColorFilter(vibrant.getTitleTextColor(), PorterDuff.Mode.MULTIPLY);
                                                        }
                                                        break;
                                                }
                                            }
                                            textview.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
                                        }
                                    }

                                }
                            }
                        } catch (IllegalArgumentException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}