package com.networking.androidtest.cache;

/**
 * Created by rafalwesolowski on 20/07/2016.
 */
public class CacheData {

    private String mRequestString;
    private long mTimestamp;
    private String mResponse;
    private boolean mRetrievedItemFromCache;

    public String getRequestString() {
        return mRequestString;
    }

    public void setRequestString(String requestString) {
        mRequestString = requestString;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        mResponse = response;
    }

    public boolean isRetrievedItemFromCache() {
        return mRetrievedItemFromCache;
    }

    public void setRetrievedItemFromCache(boolean retrievedItemFromCache) {
        mRetrievedItemFromCache = retrievedItemFromCache;
    }

    public Integer getRequestId() {
        return mRequestString.hashCode();
    }
}
