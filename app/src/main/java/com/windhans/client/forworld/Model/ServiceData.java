package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceData {

    private String id;
    private String name;
    private String category_id;
    private String subcategory_id;
    private String user_id;
    private String banner_image;
    private String description;
    private String category_name;
    private String sub_category;
    private String business_name;
    private String contact_name;
    private String email;
    private String contact_mobile;
    private String address;
    private String latitude;
    private String longitude;
    private String city;

    public ServiceData(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.name=object.getString("name");
            this.category_id=object.getString("category_id");
            this.subcategory_id=object.getString("subcategory_id");
            this.user_id=object.getString("user_id");
            this.banner_image=object.getString("banner_image");
            this.description=object.getString("description");
            this.category_name=object.getString("category_name");
            this.sub_category=object.getString("sub_category");
            this.business_name=object.getString("business_name");
            this.contact_name=object.getString("contact_name");
            this.email=object.getString("email");
            this.contact_mobile=object.getString("contact_mobile");
            this.address=object.getString("address");
            this.latitude=object.getString("latitude");
            this.longitude=object.getString("longitude");
            this.city=object.getString("city");
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
