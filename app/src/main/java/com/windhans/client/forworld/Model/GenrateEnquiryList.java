package com.example.ssforword.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class GenrateEnquiryList {
    private String user_id;
    private String name;
    private String business_id;
    private String product_id;
    private String contact;
    private String purpose;
    private String address;
    private String city;

/* "id": "1",
            "name": "demo user",
            "business_id": "1",
            "product_id": "1",
            "purpose": "nothing just tp",
            "contact_no": "9876543210",
            "address": "nsk",
            "city": "n*/

    public GenrateEnquiryList(JSONObject jsonObject) {
        try {

            this.user_id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.business_id = jsonObject.getString("business_id");
            this.product_id = jsonObject.getString("product_id");
            this.purpose = jsonObject.getString("purpose");
            this.contact = jsonObject.getString("contact_no");
            this.address = jsonObject.getString("address");
            this.city = jsonObject.getString("city");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public GenrateEnquiryList(String user_id, String name, String business_id, String product_id, String contact, String purpose, String address, String city) {
        this.user_id = user_id;
        this.name = name;
        this.business_id = business_id;
        this.product_id = product_id;
        this.contact = contact;
        this.purpose = purpose;
        this.address = address;
        this.city = city;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return this.business_id;
    }


}
