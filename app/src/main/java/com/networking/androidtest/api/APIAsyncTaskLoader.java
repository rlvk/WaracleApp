package com.networking.androidtest.api;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.networking.androidtest.cache.CacheData;
import com.networking.androidtest.cache.SimpleDBCache;
import com.networking.androidtest.pipeline.Request;
import com.networking.androidtest.pipeline.RequestError;
import com.networking.androidtest.pipeline.Response;
import com.networking.androidtest.support.NetworkStateManager;
import com.networking.androidtest.utils.Constants;
import com.networking.androidtest.utils.HttpUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class APIAsyncTaskLoader<T> extends AsyncTaskLoader<Response<T>> {

    boolean isLoading = false;

    private final Request mRequest;
    private final SimpleDBCache mSimpleDBCache;
    private CacheData mCacheData;
    private final NetworkStateManager mNetworkStateManager;
    private final Type mType;
    private final Gson gson = new Gson();

    /**
     * Constructor.
     *
     * @param context       Context
     * @param request       Request
     * @param simpleDBCache {@link SimpleDBCache} Database helper controller class
     * @param type          Class type
     */
    public APIAsyncTaskLoader(Context context, Request request, SimpleDBCache simpleDBCache, Type type) {
        super(context);
        mRequest = request;
        mSimpleDBCache = simpleDBCache;
        mType = type;
        mNetworkStateManager = new NetworkStateManager(context);
    }

    @Override
    public void forceLoad() {
        if (!isLoading) {
            super.forceLoad();
            isLoading = !isLoading;
        }
    }

    @Override
    public Response<T> loadInBackground() {

        Response<T> result = new Response<>();

        InputStream inputStream = null;

        RequestError error = new RequestError();
        Response<T> errorResponse = new Response<>();

        try {
            // Check for not valid url
            if (mRequest == null || mRequest.getUrl() == null) {
                error.setError(Constants.BAD_URL_ERROR_TYPE, "INVALID_URL");
                errorResponse.setError(error);
                return errorResponse;
            }

            // Get cached data if enabled
            if (!mRequest.isBypassCache()) {

                mCacheData = getCache(mRequest.getUrl());

                if (mCacheData != null) {
                    long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                    long ageOfCacheEntry = currentTimeInSeconds - mCacheData.getTimestamp();

                    if (ageOfCacheEntry < mRequest.getCacheTimeoutInSeconds()) {
                        // We have a cache hit for this request.  need to check if its within the timestamp range.
                        try {
                            return getCacheResponse(mCacheData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Check connectivity state after checking if the cache exists
                if (!mNetworkStateManager.isNetworkAvailable()) {
                    error.setError(Constants.OFFLINE_ERROR_TYPE, Constants.OFFLINE_ERROR_TYPE);
                    errorResponse.setError(error);
                    return errorResponse;
                }


                URL url = new URL(mRequest.getUrl());
                URLConnection connection = url.openConnection();

                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod(mRequest.getRequestType().getRequestMethod());
                httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");

                //before requesting the service check the requests last access time and if there is a cached copy of the response.

                //TODO finish customization for POST/PUT requests
                /*
                if (!mRequest.getRequestType().equals(Request.RequestType.HTTP_GET)) {
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    httpURLConnection.setDoOutput(true);

                    List<NameValuePair> params = request.getEncodedParams();
                    UrlEncodedFormEntity parameters = new UrlEncodedFormEntity(params);
                    //Send request
                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                    wr.writeBytes(parameters.toString());
                    wr.flush();
                    wr.close();
                }*/

                httpURLConnection.connect();

                int response = httpURLConnection.getResponseCode();
                inputStream = httpURLConnection.getInputStream();

                switch (response) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_CREATED:

                        try {
                            String responseString = HttpUtils.convertStreamToString(inputStream);

                            if (!mRequest.isBypassCache()) {
                                CacheData cacheData = new CacheData();
                                cacheData.setRequestString(mRequest.getUrl());

                                long timestamp = System.currentTimeMillis() / 1000;
                                cacheData.setTimestamp(timestamp);

                                cacheData.setResponse(responseString);

                                mSimpleDBCache.put(cacheData);
                            }

                            InputStream dataInputStream = new ByteArrayInputStream(responseString.getBytes());

                            result.setData(getDataFromInputStream(dataInputStream));
                            return result;

                        } catch (Exception e) {
                            error.setError(Constants.PARSE_ERROR_TYPE, e.getMessage());
                            e.printStackTrace();
                        }
                }
            }

        } catch (MalformedURLException e) {
            error.setError(Constants.BAD_URL_ERROR_TYPE, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            error.setError(Constants.IO_EXCEPTION_ERROR_TYPE, e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // If anything goes wrong AND there's still a cached version return it.
        if (mCacheData != null) {
            try {
                return getCacheResponse(mCacheData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        errorResponse.setError(error);
        return errorResponse;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(Response<T> data) {
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
    }

    public CacheData getCache(String url) {
        // Check if the request is in the cache
        CacheData cacheData = new CacheData();
        cacheData.setRequestString(url);

        // This will fill up the cacheData Object with its cached data and return it.
        cacheData = mSimpleDBCache.get(cacheData);
        if (cacheData.isRetrievedItemFromCache()) {
            return cacheData;
        } else {
            return null;
        }
    }

    public Response<T> getCacheResponse(CacheData cacheData) throws Exception {
        Response<T> result = new Response<T>();

        String responseString = cacheData.getResponse();
        ByteArrayInputStream cacheInputStream = new ByteArrayInputStream(responseString.getBytes());

        T resultData = null;

        resultData = getDataFromInputStream(cacheInputStream);

        result.setData(resultData);
        return result;
    }

    private T getDataFromInputStream(InputStream inputStream) {
        String response = HttpUtils.convertStreamToString(inputStream);
        return gson.fromJson(response, mType);
    }
}
