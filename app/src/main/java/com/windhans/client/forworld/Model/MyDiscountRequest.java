package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyDiscountRequest {

    private String qr_request_id;
    private String id;
    private String user_id;
    private String vendor_id;
    private String total_amount;
    private String product_title;
    private String discount_amount;
    private String approved_at;
    private String status;
    private String business_name;
    private String email;
    private String contact_mobile;
    private String business_banner;

    public MyDiscountRequest(JSONObject object) {
        try {
            this.qr_request_id=object.getString("qr_request_id");
            this.id=object.getString("id");
            this.user_id=object.getString("user_id");
            this.vendor_id=object.getString("vendor_id");
            this.total_amount=object.getString("total_amount");
            this.product_title=object.getString("product_title");
            this.discount_amount=object.getString("discount_amount");
            this.approved_at=object.getString("approved_at");
            this.status=object.getString("status");
            this.business_name=object.getString("business_name");
            this.email=object.getString("email");
            this.contact_mobile=object.getString("contact_mobile");
            this.business_banner=object.getString("business_banner");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getQr_request_id() {
        return qr_request_id;
    }

    public void setQr_request_id(String qr_request_id) {
        this.qr_request_id = qr_request_id;
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

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(String approved_at) {
        this.approved_at = approved_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
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

    public String getBusiness_banner() {
        return business_banner;
    }

    public void setBusiness_banner(String business_banner) {
        this.business_banner = business_banner;
    }

}
