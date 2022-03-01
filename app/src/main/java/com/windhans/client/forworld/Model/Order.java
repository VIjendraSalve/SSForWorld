package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {

    private String id;
    private String user_id;
    private String lead_id;
    private String points_redeem;
    private String remaining_cash;
    private String total_amount;
    private String reason;
    private String payment_type;
    private String first_name;
    private String last_name;
    private String mobile;

    public Order(JSONObject jsonObject1) {
        try {

            this.id = jsonObject1.getString("id");
            this.user_id = jsonObject1.getString("user_id");
            this.lead_id = jsonObject1.getString("lead_id");
            this.points_redeem = jsonObject1.getString("points_redeem");
            this.remaining_cash = jsonObject1.getString("remaining_cash");
            this.total_amount = jsonObject1.getString("total_amount");
            this.reason = jsonObject1.getString("reason");
            this.payment_type = jsonObject1.getString("payment_type");
            this.first_name = jsonObject1.getString("first_name");
            this.last_name = jsonObject1.getString("last_name");
            this.mobile = jsonObject1.getString("mobile");

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

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getPoints_redeem() {
        return points_redeem;
    }

    public void setPoints_redeem(String points_redeem) {
        this.points_redeem = points_redeem;
    }

    public String getRemaining_cash() {
        return remaining_cash;
    }

    public void setRemaining_cash(String remaining_cash) {
        this.remaining_cash = remaining_cash;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
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
