package com.windhans.client.forworld.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeadDetailModel {
    String id;
    String name;
    String business_id;
    String product_id;
    String purpose;
    String contact_no;
    String address;
    String city;
    String status;
    String created_at;

    //ArrayList<LeadDetailsNew> leadDetailsNewArrayList = new ArrayList<>();

    /*String product_name;
    String brand;
    String description;
    String price;
    String image;
    String ld_status;
    String ld_reason;
    */


    public LeadDetailModel(JSONObject object) {
        try {

            this.id=object.getString("id");
            this.name=object.getString("name");
            this.business_id=object.getString("business_id");
            this.product_id=object.getString("product_id");
            this.purpose=object.getString("purpose");
            this.contact_no=object.getString("contact_no");
            this.address=object.getString("address");
            this.city=object.getString("city");
            this.status=object.getString("status");
            this.created_at=object.getString("created_at");

            /*JSONArray array=object.getJSONArray("leadDetails");
            for (int i=0 ; i<array.length(); i++)
            {
                JSONObject jsonObject=array.getJSONObject(i);
                leadDetailsNewArrayList.add(new LeadDetailsNew(jsonObject));
               *//* this.product_name=jsonObject.getString("name");
                this.brand=jsonObject.getString("brand");
                this.description=jsonObject.getString("description");
                this.price=jsonObject.getString("selling_price");
                this.image=jsonObject.getString("product_image");
                this.ld_status=jsonObject.getString("ld_status");
                this.ld_reason=jsonObject.getString("ld_reason");*//*
            }*/


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  /*  public String getProduct_name() {
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
    }*/

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
