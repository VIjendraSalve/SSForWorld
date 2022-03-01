package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class NewProduct implements Parcelable {

    private String id;
    private String name;
    private String category;
    private String sub_category;
    private String brand;
    private String product_image;
    private String description;
    private String features;
    private String original_cost;
    private String selling_price;
    private String offer_type;
    private String offer_price;
    private String brand_image;
    private String brand_name;
    private String category_name;
    private String is_favourite;
    private String multiple_images;

    public NewProduct(String id, String name, String product_image,String is_favourite) {
        this.id = id;
        this.name = name;
        this.product_image = product_image;
        this.is_favourite=is_favourite;
    }

    public NewProduct(JSONObject jsonObject) {
        try {

            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.category = jsonObject.getString("category");
            this.sub_category = jsonObject.getString("sub_category");
            this.brand = jsonObject.getString("brand");
            this.product_image = jsonObject.getString("product_image");
            this.description = jsonObject.getString("description");
            this.features = jsonObject.getString("features");
            this.original_cost = jsonObject.getString("original_cost");
            this.selling_price = jsonObject.getString("selling_price");
            this.is_favourite = jsonObject.getString("is_favourite");
            this.offer_type = jsonObject.getString("offer_type");
            this.offer_price = jsonObject.getString("offer_price");
            this.multiple_images = jsonObject.getString("multiple_images");
            if(jsonObject.has("brand_image"))
            this.brand_image = jsonObject.getString("brand_image");
            if(jsonObject.has("brand_name"))
            this.brand_name = jsonObject.getString("brand_name");
            if(jsonObject.has("category_name"))
            this.category_name = jsonObject.getString("category_name");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected NewProduct(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        sub_category = in.readString();
        brand = in.readString();
        product_image = in.readString();
        description = in.readString();
        features = in.readString();
        original_cost = in.readString();
        selling_price = in.readString();
        offer_type = in.readString();
        offer_price = in.readString();
        brand_image = in.readString();
        brand_name = in.readString();
        category_name = in.readString();
        is_favourite = in.readString();
        multiple_images = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(sub_category);
        dest.writeString(brand);
        dest.writeString(product_image);
        dest.writeString(description);
        dest.writeString(features);
        dest.writeString(original_cost);
        dest.writeString(selling_price);
        dest.writeString(offer_type);
        dest.writeString(offer_price);
        dest.writeString(brand_image);
        dest.writeString(brand_name);
        dest.writeString(category_name);
        dest.writeString(is_favourite);
        dest.writeString(multiple_images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewProduct> CREATOR = new Creator<NewProduct>() {
        @Override
        public NewProduct createFromParcel(Parcel in) {
            return new NewProduct(in);
        }

        @Override
        public NewProduct[] newArray(int size) {
            return new NewProduct[size];
        }
    };

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getOriginal_cost() {
        return original_cost;
    }

    public void setOriginal_cost(String original_cost) {
        this.original_cost = original_cost;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
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

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getMultiple_images() {
        return multiple_images;
    }

    public void setMultiple_images(String multiple_images) {
        this.multiple_images = multiple_images;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }
}
