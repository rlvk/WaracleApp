package com.waracle.androidtest.api;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.waracle.androidtest.datamodel.Cake;
import com.waracle.androidtest.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class MyAsyncTaskLoader extends AsyncTaskLoader<List<Cake>> {

    boolean isLoading = false;

    private Request mRequest;

    public MyAsyncTaskLoader(Context context, Request request) {
        super(context);
        mRequest = request;
    }

    @Override
    public void forceLoad() {
        if (!isLoading) {
            super.forceLoad();
            isLoading = !isLoading;
        }
    }

    @Override
    public List<Cake> loadInBackground() {
        return fetchData(mRequest.getUrl(), mRequest.getRequestType().getRequestMethod());
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(List<Cake> data) {
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<Cake> fetchData(String urlString, String method) {
        HttpURLConnection connection = null;
        try {
            InputStream inputStream = HttpUtils.getHttpConnection(urlString, method);
            String response = convertStreamToString(inputStream);
            JSONArray responseArray = new JSONArray(response);

            return Cake.convertJsonToCake(responseArray);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private String convertStreamToString(InputStream in) {
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
