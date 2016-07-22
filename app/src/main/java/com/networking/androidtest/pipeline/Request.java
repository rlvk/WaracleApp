package com.networking.androidtest.pipeline;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class Request {

    private String mUrl;
    private RequestType mRequestType;
    private int mCacheTimeoutInSeconds;

    // Force fetch new data, even if cache data exists
    private boolean mBypassCache;

    public enum RequestType {

        HTTP_GET("GET"),
        HTTP_POST("POST"),
        HTTP_PUT("PUT"),
        HTTP_DELETE("DELETE");

        String mRequestMethod;

        RequestType(String requestMethod) {
            this.mRequestMethod = requestMethod;
        }

        public String getRequestMethod() {
            return mRequestMethod;
        }
    }

    /**
     * Constructor.
     *
     * @param url Url
     * @param requestType {@link RequestType}
     * @param cacheTimeoutInSeconds cache timeout in miliseconds (expiration time)
     * @param bypassCache true if cache should be ignored. Otherwise false.
     */
    public Request(String url, RequestType requestType, int cacheTimeoutInSeconds, boolean bypassCache) {
        mUrl = url;
        mRequestType = requestType;
        mCacheTimeoutInSeconds = cacheTimeoutInSeconds;
        mBypassCache = bypassCache;
    }

    /**
     * Constructor with default cache enabled.
     *
     * @param url Url
     * @param requestType {@link RequestType}
     * @param cacheTimeoutInSeconds time out in seconds as long
     */
    public Request(String url, RequestType requestType, int cacheTimeoutInSeconds) {
        this(url, requestType, cacheTimeoutInSeconds, false);
    }

    public RequestType getRequestType() {
        return mRequestType;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getCacheTimeoutInSeconds() {
        return mCacheTimeoutInSeconds;
    }

    public boolean isBypassCache() {
        return mBypassCache;
    }
}
