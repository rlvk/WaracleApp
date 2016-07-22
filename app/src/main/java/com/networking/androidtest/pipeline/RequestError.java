package com.networking.androidtest.pipeline;

/**
 * Created by rafalwesolowski on 20/07/2016.
 */
public class RequestError {

    private String mError;
    private String mMessage;

    public String getError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setError(String error, String message) {
        mError = error;
        mMessage = message;
    }
}
