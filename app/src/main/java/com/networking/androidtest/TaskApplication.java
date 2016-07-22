package com.networking.androidtest;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.networking.androidtest.cache.DBHelper;
import com.networking.androidtest.cache.SimpleDBCache;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class TaskApplication extends Application {

    private static LruCache<String, Bitmap> mMemoryCache;
    private SimpleDBCache mSimpleCache;
    private static TaskApplication _applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        _applicationInstance = this;

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public static TaskApplication getInstance() {
        return _applicationInstance;
    }

    /**
     * Returns instance of the database cache class.
     *
     * @return SimpleDBCache
     */
    public SimpleDBCache getNewLookCacheDb() {
        if (mSimpleCache == null) {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            mSimpleCache = new SimpleDBCache(dbHelper);
        }
        return mSimpleCache;
    }

    /**
     * Saves bitmap to memory cache.
     *
     * @param key    key of the bitmap (in this case it's gonna be URL)
     * @param bitmap {@link Bitmap}
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * Get bitmap image from cache.
     *
     * @param key key of the bitmap (in this case it's gonna be URL)
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
