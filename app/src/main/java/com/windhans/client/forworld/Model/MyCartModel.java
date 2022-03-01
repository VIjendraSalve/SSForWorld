package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCartModel {
    String cart_id;
    String id;
    String category_id;
    String subcategory_id;
    String product_name;
    String original_cost;
    String image;
    String description;
    String business_id;
    String product_id;
    String offer_price;
    String business_name;
    String contact_mobile;
    String selling_price;
    String quantity;




    public MyCartModel(JSONObject object) {
        try {
            this.cart_id=object.getString("cart_id");
            this.id=object.getString("id");
            this.category_id=object.getString("category");
            this.subcategory_id=object.getString("sub_category");
            this.product_name=object.getString("name");
            this.original_cost=object.getString("original_cost");
            this.image=object.getString("product_image");
            this.business_id=object.getString("business_id");
            this.product_id=object.getString("product_id");
            this.description=object.getString("description");
            this.offer_price=object.getString("offer_price");
            this.business_name=object.getString("business_name");
            this.contact_mobile=object.getString("contact_mobile");
            this.selling_price=object.getString("selling_price");
            this.quantity=object.getString("quantity");

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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOriginal_cost() {
        return original_cost;
    }

    public void setOriginal_cost(String original_cost) {
        this.original_cost = original_cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
}

