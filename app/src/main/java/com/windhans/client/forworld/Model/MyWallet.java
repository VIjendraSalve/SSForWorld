package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyWallet {

    private String id;
    private String user_id;
    private String action;
    private String ref_user_id;
    private String amount;
    private String trans_details;
    private String order_id;
    private String created_at;
    private String first_name;
    private String last_name;
    private String mobile;

    public MyWallet(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.user_id=object.getString("user_id");
            this.action=object.getString("action");
            this.ref_user_id=object.getString("ref_user_id");
            this.amount=object.getString("amount");
            this.trans_details=object.getString("trans_details");
            this.order_id=object.getString("order_id");
            this.created_at=object.getString("created_at");
            this.first_name=object.getString("first_name");
            this.last_name=object.getString("last_name");
            this.mobile=object.getString("mobile");

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRef_user_id() {
        return ref_user_id;
    }

    public void setRef_user_id(String ref_user_id) {
        this.ref_user_id = ref_user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTrans_details() {
        return trans_details;
    }

    public void setTrans_details(String trans_details) {
        this.trans_details = trans_details;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
