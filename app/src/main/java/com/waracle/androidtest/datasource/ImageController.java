package com.waracle.androidtest.datasource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.waracle.androidtest.TaskApplication;
import com.waracle.androidtest.api.DownloadImageTask;
import com.waracle.androidtest.utils.BitmapUtil;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class ImageController {

    private final String mImageUrl;
    private Context mContext;

    /**
     * Constructor.
     *
     * @param context  Context
     * @param imageUrl url of the image to be loaded into ImageView
     */
    public ImageController(Context context, String imageUrl) {
        mContext = context;
        mImageUrl = imageUrl;
    }

    /**
     * Starts downloading the image and displaying in provided image view.
     *
     * @param imageView ImageView
     */
    public void loadBitmap(ImageView imageView) {

        final Bitmap bitmap = TaskApplication.getInstance().getBitmapFromMemCache(mImageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        if (cancelPotentialWork(mImageUrl, imageView)) {
            final DownloadImageTask task = new DownloadImageTask(imageView);
            final BitmapUtil.AsyncDrawable asyncDrawable = new BitmapUtil.AsyncDrawable(mContext.getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(mImageUrl);
        }
    }

    public static boolean cancelPotentialWork(String url, ImageView imageView) {
        final DownloadImageTask bitmapWorkerTask = getDownloadBitmapTask(imageView);

        if (bitmapWorkerTask != null) {
            final String imageUrl = bitmapWorkerTask.getImageUrl();
            // If url is the same it means work is already in progress
            if (imageUrl != null && imageUrl.equals(url)) {
                return false;
            } else {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public static DownloadImageTask getDownloadBitmapTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapUtil.AsyncDrawable) {
                final BitmapUtil.AsyncDrawable asyncDrawable = (BitmapUtil.AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
