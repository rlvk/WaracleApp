package com.waracle.androidtest.api;

import android.content.Context;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class ApiClient {

    private static final String CAKES_JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private static ApiClient _apiClient;
    private Context mContext;

    /**
     * Returns the singleton instance of this class.
     *
     * @param context Context
     * @return singleton instance
     */
    public static ApiClient getInstance(Context context) {
        if (_apiClient == null) {
            _apiClient = new ApiClient(context);
        }
        return _apiClient;
    }

    public ApiClient(Context context) {
        mContext = context;
    }

    /**
     * Creates AsyncTaskLoader for Cakes API call.
     *
     * @return MyAsyncTaskLoader
     */
    public MyAsyncTaskLoader createLoaderForCakes() {
        Request request = new Request(CAKES_JSON_URL, Request.RequestType.HTTP_GET);
        return new MyAsyncTaskLoader(mContext, request);
    }
}
