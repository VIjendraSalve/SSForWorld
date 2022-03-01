package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class OfferModel {
    String id;
    String name;
    String validity;
    String offer_type;
    String offer_price;
    String product_id;
    String terms;
    String image;
    String status;
    String offer_code;
    String offer_limit;
    String created_on;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public OfferModel(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.name=object.getString("name");
            this.validity=object.getString("validity");
            this.offer_type=object.getString("offer_type");
            this.offer_price=object.getString("offer_price");
            this.product_id=object.getString("product_id");
            this.terms=object.getString("terms");
            this.image=object.getString("image");
            this.status=object.getString("status");
            this.offer_code=object.getString("offer_code");
            this.offer_limit=object.getString("offer_limit");
            this.created_on=object.getString("created_on");
            this.description=object.getString("description");
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

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public String getOffer_limit() {
        return offer_limit;
    }

    public void setOffer_limit(String offer_limit) {
        this.offer_limit = offer_limit;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }


}
