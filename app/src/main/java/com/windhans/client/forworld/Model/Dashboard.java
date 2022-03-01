package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard {
    String id;
    String user_id;
    String category_id;
    String business_name;
    String description;
    String contact_name;
    String business_banner;
    String email;
    String address;
    String contact_mobile;

    public Dashboard(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.user_id=object.getString("user_id");
            this.category_id=object.getString("category_id");
            this.business_name=object.getString("business_name");
            this.description=object.getString("description");
            this.contact_name=object.getString("contact_name");
            this.business_banner=object.getString("business_banner");
            this.email=object.getString("email");
            this.address=object.getString("address");
            this.contact_mobile=object.getString("contact_mobile");
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getBusiness_banner() {
        return business_banner;
    }

    public void setBusiness_banner(String business_banner) {
        this.business_banner = business_banner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }
}
