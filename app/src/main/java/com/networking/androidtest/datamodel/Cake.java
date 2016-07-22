package com.networking.androidtest.datamodel;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class Cake {

    private static final String NAME_JSON_KEY = "title";
    private static final String DESCRIPTION_JSON_KEY = "desc";
    private static final String IMAGE_URL_JSON_KEY = "image";

    @SerializedName("title")
    private String mName;

    @SerializedName("desc")
    private String mDescription;

    @SerializedName("image")
    private String mImageUrl;

    /**
     * Constructor.
     *
     * @param name        Cake name
     * @param description cake description
     * @param imageUrl    cake image url
     */
    public Cake(String name, String description, String imageUrl) {
        mName = name;
        mDescription = description;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    /**
     * Converts JSONArray to List of Cake objects.
     *
     * @param jsonArray {@link JSONArray}
     * @return
     */
    public static List<Cake> convertJsonToCake(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<Cake> cakesFromAPI = new ArrayList<>();

        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                if (jsonObject != null) {
                    String name = jsonObject.getString(NAME_JSON_KEY);
                    String description = jsonObject.getString(DESCRIPTION_JSON_KEY);
                    String imageURL = jsonObject.getString(IMAGE_URL_JSON_KEY);
                    Cake cake = new Cake(name, description, imageURL);
                    cakesFromAPI.add(cake);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cakesFromAPI;
    }
}
