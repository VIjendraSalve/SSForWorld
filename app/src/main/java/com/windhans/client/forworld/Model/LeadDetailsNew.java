package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class LeadDetailsNew {

    String product_name;
    String brand;
    String description;
    String price;
    String image;
    String ld_status;
    String ld_reason;
    String created_at;
    String category_name;
    String subcategory_name;


    public LeadDetailsNew(JSONObject object) {
        try {


            this.product_name = object.getString("name");
            this.ld_status = object.getString("ld_status");
            this.ld_reason = object.getString("ld_reason");
            if(object.has("brand"))
            this.brand = object.getString("brand");
            if(object.has("description"))
            this.description = object.getString("description");
            if(object.has("selling_price"))
            this.price = object.getString("selling_price");
            if(object.has("product_image"))
            this.image = object.getString("product_image");
            this.created_at = object.getString("reason_created");




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLd_status() {
        return ld_status;
    }

    public void setLd_status(String ld_status) {
        this.ld_status = ld_status;
    }

    public String getLd_reason() {
        return ld_reason;
    }

    public void setLd_reason(String ld_reason) {
        this.ld_reason = ld_reason;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }
}
