package com.networking.androidtest.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.networking.androidtest.TaskApplication;
import com.networking.androidtest.datasource.ImageController;
import com.networking.androidtest.utils.BitmapUtil;
import com.networking.androidtest.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private static final String REQUEST_METHOD = "GET";

    private final WeakReference<ImageView> mImageViewWeakReference;

    private String mImageUrl;
    private int mRequiredWidth, mRequiredHeight;

    /**
     * Constructor.
     *
     * @param imageView      ImageView instance
     * @param requiredWidth  Bitmap required width
     * @param requiredHeight Bitmap required height
     */
    public ImageLoader(ImageView imageView, int requiredWidth, int requiredHeight) {
        // Set the maximum size for thumbnail
        mRequiredWidth = requiredWidth;
        mRequiredHeight = requiredHeight;
        mImageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        mImageUrl = urls[0];

        final Bitmap bitmap = downloadImage(mImageUrl, mRequiredWidth, mRequiredHeight);

        // Save image to cache
        if (bitmap != null) {
            TaskApplication.getInstance().addBitmapToMemoryCache(mImageUrl, bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (mImageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = mImageViewWeakReference.get();
            final ImageLoader downloadImageTask = ImageController.getDownloadBitmapTask(imageView);
            if (this == downloadImageTask && imageView != null) {
                // Set image to imageView
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * Decodes bitmap from inputstream with required width and height.
     *
     * @param url            remote URL of bitmap
     * @param requiredWidth  Bitmap required width
     * @param requiredHeight Bitmap required height
     * @return Bitmap if download and decode precess succeeded. Otherwise null.
     */
    private Bitmap downloadImage(String url, int requiredWidth, int requiredHeight) {
        Bitmap bitmap = null;
        try {
            InputStream stream = HttpUtils.getHttpConnection(url, REQUEST_METHOD);

            bitmap = BitmapUtil.decodeSampledBitmapFromInputStream(stream, requiredWidth, requiredHeight);
            stream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
