package com.networking.androidtest.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.networking.androidtest.cache.SimpleDBCache;
import com.networking.androidtest.datamodel.Cake;
import com.networking.androidtest.pipeline.Request;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class ApiClient {

    private static final String CAKES_JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private static ApiClient _apiClient;
    private WeakReference<Context> mContext;
    private SimpleDBCache mCacheDB;

    /**
     * Returns the singleton instance of this class.
     *
     * @param context Context
     * @param cacheDb SimpleDBCache instance
     * @return singleton instance
     */
    public static ApiClient getInstance(Context context, SimpleDBCache cacheDb) {
        if (_apiClient == null) {
            _apiClient = new ApiClient(context, cacheDb);
        }
        return _apiClient;
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param cacheDb SimpleDBCache instance
     */
    public ApiClient(Context context, SimpleDBCache cacheDb) {
        mContext = new WeakReference<Context>(context);
        mCacheDB = cacheDb;
    }

    /**
     * Creates AsyncTaskLoader for Cakes API call.
     *
     * @return APIAsyncTaskLoader
     */
    public APIAsyncTaskLoader<List<Cake>> createLoaderForCakes() {

        int cacheTimeOutInSeconds = 30;

        Request request = new Request(CAKES_JSON_URL, Request.RequestType.HTTP_GET, cacheTimeOutInSeconds);

        APIAsyncTaskLoader<List<Cake>> cakeTaskLoader = null;

        if (mContext != null && mContext.get() != null) {
            cakeTaskLoader = new APIAsyncTaskLoader<>(mContext.get(), request, mCacheDB,
                    new TypeToken<List<Cake>>() {}.getType());
        }

        return cakeTaskLoader;
    }
}
