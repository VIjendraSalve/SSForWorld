package com.windhans.client.forworld.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Services {
    String category_id;
    String category_name;
    String image;
    String category_name_marathi;
    String category_name_hindi;

    public Services(String category_id, String category_name, String image) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.image = image;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Services(JSONObject object) {
        try {
            this.category_id=object.getString("category_id");
            this.category_name=object.getString("category_name");
            this.image=object.getString("image");
            this.category_name_marathi=object.getString("category_name_marathi");
            this.category_name_hindi=object.getString("category_name_hindi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name_marathi() {
        return category_name_marathi;
    }

    public void setCategory_name_marathi(String category_name_marathi) {
        this.category_name_marathi = category_name_marathi;
    }

    public String getCategory_name_hindi() {
        return category_name_hindi;
    }

    public void setCategory_name_hindi(String category_name_hindi) {
        this.category_name_hindi = category_name_hindi;
    }

    @NonNull
    @Override
    public String toString() {
        return this.category_name;
    }

}
