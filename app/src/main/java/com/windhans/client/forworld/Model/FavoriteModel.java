package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteModel {
    private String id;
    private String product_name;
    private String description;
    private String product_image;
    private String price;
    private String category_id;
    private String subcategory_id;
    private String image;
    private String business_id;
    private String brand;
    private String business_name;
    private String contact_name;
    private String contact_mobile;
    private String address;
    private String product_id;



    private String original_cost;
    private String selling_price;

    public FavoriteModel(JSONObject jsonObject) {
        try {
            this.id=jsonObject.getString("id");
            this.product_id=jsonObject.getString("product_id");
            JSONObject jsonObject1=jsonObject.getJSONObject("product_details");

            this.product_name=jsonObject1.getString("name");
            this.description=jsonObject1.getString("description");
            //  this.product_image=jsonObject.getString("product_image");
         //   this.price=jsonObject1.getString("price");
            this.category_id=jsonObject1.getString("category");
            this.subcategory_id=jsonObject1.getString("sub_category");
            this.image=jsonObject1.getString("product_image");
            this.selling_price=jsonObject1.getString("selling_price");
            this.original_cost=jsonObject1.getString("original_cost");
       //     this.business_id=jsonObject1.getString("business_id");
            this.brand=jsonObject1.getString("brand");
        //    this.business_name=jsonObject1.getString("business_name");
          //  this.address=jsonObject1.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOriginal_cost() {
        return original_cost;
    }

    public void setOriginal_cost(String original_cost) {
        this.original_cost = original_cost;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


}
