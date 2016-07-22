package com.networking.androidtest.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.networking.androidtest.api.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class BitmapUtil {

    public static class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<ImageLoader> mDownloadImageTask;

        public AsyncDrawable(Resources res, Bitmap bitmap, ImageLoader downloadImageTask) {
            super(res, bitmap);
            mDownloadImageTask = new WeakReference<>(downloadImageTask);
        }

        public ImageLoader getBitmapWorkerTask() {
            return mDownloadImageTask.get();
        }
    }

    /**
     * Returns sample size of the bitmap about to be scaled down.
     *
     * @param options   {@link BitmapFactory.Options}
     * @param reqWidth  required bitmap width
     * @param reqHeight required bitmap height
     * @return inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (reqWidth == 0 || reqHeight == 0) {
            return inSampleSize;
        }

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Decodes the scaled bitmap with required width and height from byte array.
     *
     * @param inputStream Image data input stream
     * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream, int reqWidth, int reqHeight) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        ByteArrayOutputStream baos = cloneInputStream(inputStream);

        InputStream bitmapInputStreamForQuery = new ByteArrayInputStream(baos.toByteArray());
        InputStream bitmapInputStreamForReturn = new ByteArrayInputStream(baos.toByteArray());

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bitmapInputStreamForQuery, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(bitmapInputStreamForReturn, null, options);
    }

    /**
     * Copy input stream into ByteArrayOutputStream to being able to clone byte array data and use it
     * for creating new InputStream instances.
     *
     * @param inputStream {@link InputStream}
     * @return ByteArrayOutputStream
     */
    private static ByteArrayOutputStream cloneInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        return baos;
    }
}
