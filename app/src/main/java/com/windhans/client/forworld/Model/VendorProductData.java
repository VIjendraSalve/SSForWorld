package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class VendorProductData implements Parcelable {

    String id;
    String category_id;
    String subcategory_id;
    String business_id;
    String product_name;
    String brand;
    String description;
    String price;
    String image;
    String multiple_images;
    String selling_price;

    public VendorProductData(JSONObject jsonObject1) {
        try {
            this.id=jsonObject1.getString("id");
            this.category_id=jsonObject1.getString("category");
            //this.subcategory_id=jsonObject1.getString("subcategory_id");
            this.subcategory_id=jsonObject1.getString("sub_category");
       //     this.business_id=jsonObject1.getString("business_id");
            this.product_name=jsonObject1.getString("name");
            this.brand=jsonObject1.getString("brand");
            this.description=jsonObject1.getString("description");
           // this.price=jsonObject1.getString("price");
            this.price=jsonObject1.getString("original_cost");
            this.image=jsonObject1.getString("product_image");
            this.selling_price=jsonObject1.getString("selling_price");
            this.multiple_images=jsonObject1.getString("multiple_images");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected VendorProductData(Parcel in) {
        id = in.readString();
        category_id = in.readString();
        subcategory_id = in.readString();
        business_id = in.readString();
        product_name = in.readString();
        brand = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.readString();
        multiple_images = in.readString();
        selling_price = in.readString();
    }

    public static final Creator<VendorProductData> CREATOR = new Creator<VendorProductData>() {
        @Override
        public VendorProductData createFromParcel(Parcel in) {
            return new VendorProductData(in);
        }

        @Override
        public VendorProductData[] newArray(int size) {
            return new VendorProductData[size];
        }
    };

    public String getMultiple_images() {
        return multiple_images;
    }

    public void setMultiple_images(String multiple_images) {
        this.multiple_images = multiple_images;
    }
    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(category_id);
        dest.writeString(subcategory_id);
        dest.writeString(business_id);
        dest.writeString(product_name);
        dest.writeString(brand);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeString(multiple_images);
        dest.writeString(selling_price);
    }
}
