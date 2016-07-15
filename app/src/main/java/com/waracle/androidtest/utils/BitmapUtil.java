package com.waracle.androidtest.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.waracle.androidtest.api.DownloadImageTask;

import java.lang.ref.WeakReference;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class BitmapUtil {

    public static class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<DownloadImageTask> mDownloadImageTask;

        public AsyncDrawable(Resources res, Bitmap bitmap, DownloadImageTask downloadImageTask) {
            super(res, bitmap);
            mDownloadImageTask = new WeakReference<>(downloadImageTask);
        }

        public DownloadImageTask getBitmapWorkerTask() {
            return mDownloadImageTask.get();
        }
    }
}
