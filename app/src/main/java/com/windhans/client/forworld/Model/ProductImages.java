package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductImages {
    private String id;
    private String ImageName;

    public ProductImages(JSONObject jsonObject) {
        try {

            this.id = jsonObject.getString("id");
            this.ImageName = jsonObject.getString("ImageName");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
