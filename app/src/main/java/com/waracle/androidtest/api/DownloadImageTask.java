package com.waracle.androidtest.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.waracle.androidtest.TaskApplication;
import com.waracle.androidtest.datasource.ImageController;
import com.waracle.androidtest.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final String REQUEST_METHOD = "GET";

    private final WeakReference<ImageView> mImageViewWeakReference;

    private String mImageUrl;

    /**
     * Constructor.
     *
     * @param imageView ImageView instance
     */
    public DownloadImageTask(ImageView imageView) {
        mImageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        mImageUrl = urls[0];
        final Bitmap bitmap = downloadImage(mImageUrl);

        // Save image to cache
        TaskApplication.getInstance().addBitmapToMemoryCache(mImageUrl, bitmap);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (mImageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = mImageViewWeakReference.get();
            final DownloadImageTask downloadImageTask = ImageController.getDownloadBitmapTask(imageView);
            if (this == downloadImageTask && imageView != null) {
                // Set image to imageView
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    // Creates Bitmap from InputStream and returns it
    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.outHeight = 128;
        bmOptions.outWidth = 128;

        try {
            stream = HttpUtils.getHttpConnection(url, REQUEST_METHOD);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
