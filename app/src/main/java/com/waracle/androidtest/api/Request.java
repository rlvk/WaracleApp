package com.waracle.androidtest.api;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class Request {

    private String mUrl;
    private RequestType mRequestType;

    public enum RequestType {

        HTTP_GET("GET"),
        HTTP_POST("POST"),
        HTTP_PUT("PUT"),
        HTTP_DELETE("DELETE");

        String mRequestMethod;

        RequestType(String requestMethod) {
            this.mRequestMethod = requestMethod;
        }

        String getRequestMethod() {
            return mRequestMethod;
        }
    }

    /**
     * Constructor.
     *
     * @param url Url
     * @param requestType {@link RequestType}
     */
    public Request(String url, RequestType requestType) {
        mUrl = url;
        mRequestType = requestType;
    }

    public RequestType getRequestType() {
        return mRequestType;
    }

    public String getUrl() {
        return mUrl;
    }
}
