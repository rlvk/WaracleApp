package com.waracle.androidtest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class HttpUtils {

    /**
     * Returns InputStream from HttpConnection.
     *
     * @param urlString
     * @param method
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getHttpConnection(String urlString, String method) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod(method);
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setConnectTimeout(10000);
            httpConnection.connect();

            int status = httpConnection.getResponseCode();

            switch (status) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_CREATED:
                    stream = connection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}
