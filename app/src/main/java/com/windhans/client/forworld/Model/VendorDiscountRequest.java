package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class VendorDiscountRequest {

    private String qr_request_id;
    private String id;
    private String user_id;
    private String vendor_id;
    private String total_amount;
    private String product_title;
    private String discount_amount;
    private String approved_at;
    private String status;
    private String first_name;
    private String last_name;
    private String profile_image;
    private String mobile;
    private String user_card_number;


    public VendorDiscountRequest(JSONObject object) {
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
            this.first_name=object.getString("first_name");
            this.last_name=object.getString("last_name");
            this.profile_image=object.getString("profile_image");
            this.mobile=object.getString("mobile");
            this.user_card_number=object.getString("user_card_number");

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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_card_number() {
        return user_card_number;
    }

    public void setUser_card_number(String user_card_number) {
        this.user_card_number = user_card_number;
    }
}
