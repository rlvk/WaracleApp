package com.networking.androidtest.pipeline;

/**
 * Created by rafalwesolowski on 20/07/2016.
 */
public class Response<T> {

    private T mData;
    private RequestError mError;


    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public RequestError getError() {
        return mError;
    }

    public void setError(RequestError error) {
        mError = error;
    }
}
