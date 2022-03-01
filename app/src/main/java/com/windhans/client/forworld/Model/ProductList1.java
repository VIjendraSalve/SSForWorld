package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductList1 {
    private String id;
    private String product_name;
    private String description;
    private String product_image;
    private String price;
    private String category_id;
    private String subcategory_id;
    private String image;
    private String business_id;
    private String brand;
    private String city;
    private String district;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    private String pincode;

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    String isFavorite;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    String buisness_id;
    String user_id;
    String business_name;
    String buisness_description;
    String contact_name;
    String business_banner;
    String email;
    String address;
    String contact_mobile;

    String offer_id;
    String offer_name;
    String validity;
    String offer_description;
    String offer_type;
    String offer_price;
    String offer_code;
    String offer_term;

    public String getOffer_term() {
        return offer_term;
    }

    public void setOffer_term(String offer_term) {
        this.offer_term = offer_term;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
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

    String offer_limit;

    public ProductList1(String id, String product_name, String image,String isFavroite) {
        this.id = id;
        this.product_name = product_name;
        this.image = image;
        this.isFavorite=isFavroite;
    }

    public ProductList1(JSONObject jsonObject) {
        try {

            this.id = jsonObject.getString("id");
            this.product_name = jsonObject.getString("product_name");
            this.description = jsonObject.getString("description");
            this.price = jsonObject.getString("price");
            this.category_id = jsonObject.getString("category_id");
            this.subcategory_id = jsonObject.getString("subcategory_id");
            this.image = jsonObject.getString("image");
            this.business_id = jsonObject.getString("business_id");
            this.brand = jsonObject.getString("brand");
            this.isFavorite = jsonObject.getString("is_favourite");

            /*JSONObject jsonObject1=jsonObject.getJSONObject("businessInfo");
            this.user_id=jsonObject1.getString("user_id");
            this.category_id=jsonObject1.getString("category_id");
            this.business_name=jsonObject1.getString("business_name");
            this.buisness_description=jsonObject1.getString("description");
            this.contact_name=jsonObject1.getString("contact_name");
            this.business_banner=jsonObject1.getString("business_banner");
            this.email=jsonObject1.getString("email");
            this.address=jsonObject1.getString("address");
            this.contact_mobile=jsonObject1.getString("contact_mobile");
            this.city=jsonObject1.getString("city");
            this.state=jsonObject1.getString("state");
            this.district=jsonObject1.getString("district");
            this.pincode=jsonObject1.getString("pincode");*/


           // JSONObject jsonObject2=jsonObject.getJSONObject("offerInfo");
        /*    JSONObject jsonObject2=jsonObject.getJSONObject("offerInfo");
            if(jsonObject2.length()==0)
            {

            }
            else {
                this.offer_id=jsonObject2.getString("id");
                this.offer_name=jsonObject2.getString("name");
                this.validity=jsonObject2.getString("validity");
                this.offer_description=jsonObject2.getString("description");
                this.offer_type=jsonObject2.getString("offer_type");
                this.offer_limit=jsonObject2.getString("offer_limit");
                this.offer_code=jsonObject2.getString("offer_code");
                this.offer_price=jsonObject2.getString("offer_price");
                this.offer_term=jsonObject2.getString("terms");
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBuisness_id() {
        return buisness_id;
    }

    public void setBuisness_id(String buisness_id) {
        this.buisness_id = buisness_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBuisness_description() {
        return buisness_description;
    }

    public void setBuisness_description(String buisness_description) {
        this.buisness_description = buisness_description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    @Override
    public String toString() {
        return this.product_name;            // What to display in the Spinner list.
    }
    public ProductList1(String product_name) {
        this.product_name = product_name;
    }

}

