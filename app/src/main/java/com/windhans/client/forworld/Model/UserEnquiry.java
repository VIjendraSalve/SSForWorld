package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserEnquiry {
    String id;
    String name;
    String business_id;
    String product_id;
    String purpose;
    String contact_no;
    String address;
    String city;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    String category_id;
    String subcategory_id;
    String product_name;
    String brand;
    String description;
    String price;
    String image;

    public UserEnquiry(JSONObject obj) {
        try {

            this.id = obj.getString("id");
            this.name=obj.getString("name");
            this.business_id=obj.getString("business_id");
            this.purpose=obj.getString("purpose");
            this.contact_no=obj.getString("contact_no");
            this.address=obj.getString("address");
            this.status=obj.getString("status");




            JSONObject jsonObject1=obj.getJSONObject("productData");
            this.category_id = jsonObject1.getString("category_id");
            this.subcategory_id = jsonObject1.getString("subcategory_id");
            this.product_name = jsonObject1.getString("product_name");
            this.description = jsonObject1.getString("description");
            this.price = jsonObject1.getString("price");

            this.image = jsonObject1.getString("image");





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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


}
