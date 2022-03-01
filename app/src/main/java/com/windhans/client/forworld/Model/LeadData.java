package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class LeadData {
    String id;
    String name;
    String purpose;
    String contact_no;
    String address;
    String city;
    String product_name;
    String description;
    String image;
    String price;
    String business_id;
    String product_id;
    String status;
    String created_by;
    String created_at;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LeadData(JSONObject obj) {
        try {
            this.id=obj.getString("id");
            this.name=obj.getString("name");
            this.purpose=obj.getString("purpose");
            this.contact_no=obj.getString("contact_no");
            this.address=obj.getString("address");
            this.city=obj.getString("city");
            this.product_name=obj.getString("name");
            this.created_by=obj.getString("created_by");
if(obj.has("description"))
            this.description=obj.getString("description");
            if(obj.has("product_image"))
                this.image=obj.getString("product_image");
            if(obj.has("business_id"))
                this.business_id=obj.getString("business_id");
            if(obj.has("product_id"))
                this.product_id=obj.getString("product_id");
            if(obj.has("price"))
                this.price=obj.getString("price");
            if(obj.has("status"))
                this.status=obj.getString("status");
            if(obj.has("created_at"))
                this.created_at=obj.getString("created_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
