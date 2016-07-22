package com.networking.androidtest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * @param urlString            request url
     * @param method               Request method
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getHttpConnection(String urlString, String method) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setUseCaches(true);
            httpConnection.setRequestMethod(method);
            httpConnection.setRequestProperty("Content-length", "0");
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

    /**
     * Converts InputStream object to String
     *
     * @param in {@link InputStream}
     * @return String
     */
    public static String convertStreamToString(InputStream in) {
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = null;
        try {
            read = br.readLine();

            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
