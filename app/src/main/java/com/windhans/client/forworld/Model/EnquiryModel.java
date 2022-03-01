package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class EnquiryModel {
    String id;
    String name;
    String business_id;
    String product_id;
    String purpose;
    String contact_no;
    String address;
    String status;
    String created_at;
    String business_name;
    String district;

    public EnquiryModel(JSONObject obj) {
        try {
            this.id=obj.getString("id");
            this.name=obj.getString("name");
            this.business_id=obj.getString("business_id");
            this.product_id=obj.getString("product_id");
            this.purpose=obj.getString("purpose");
            this.contact_no=obj.getString("contact_no");
            this.address=obj.getString("address");
            this.status=obj.getString("status");
            this.created_at=obj.getString("created_at");
            this.business_name=obj.getString("business_name");
            this.district=obj.getString("district");
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
