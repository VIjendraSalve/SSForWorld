package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDataForBill {


    private String srno;
    private String id;
    private String product_name;
    private String quantity;
    private String total;

    public ProductDataForBill(JSONObject object) {
        try {
            this.srno = "";
            this.id = object.getString("id");
            this.product_name = object.getString("product_name");
            this.quantity = object.getString("quantity");
            this.total = object.getString("total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
