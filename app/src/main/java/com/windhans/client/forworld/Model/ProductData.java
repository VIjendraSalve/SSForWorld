package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductData {
    String product_id;
    String category_id;
    String subcategory_id;
    String business_id;
    String product_name;
    String brand;
    String description;
    String price;
    String image;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
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


    public ProductData(JSONObject jsonObject1) {
        try {

             this.product_id=jsonObject1.getString("id");
            this.product_name=jsonObject1.getString("name");
            this.description=jsonObject1.getString("description");
            this.image=jsonObject1.getString("product_image");

            /*this.category_id=jsonObject1.getString("category_id");
            this.subcategory_id=jsonObject1.getString("subcategory_id");
            this.business_id=jsonObject1.getString("business_id");

            this.brand=jsonObject1.getString("brand");

            this.price=jsonObject1.getString("price");*/


           /* this.product_id=jsonObject1.getString("id");
            this.product_name=jsonObject1.getString("product_name");
            this.category_id=jsonObject1.getString("category_id");
            this.subcategory_id=jsonObject1.getString("subcategory_id");
            this.business_id=jsonObject1.getString("business_id");

            this.brand=jsonObject1.getString("brand");
            this.description=jsonObject1.getString("description");
            this.price=jsonObject1.getString("price");
            this.image=jsonObject1.getString("image");*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
