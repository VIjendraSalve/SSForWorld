package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class BuisnessModel implements Parcelable {
    String id;
    String user_id;
    String category_id;
    String business_name;
    String description;
    String contact_name;
    String business_banner;
    String email;
    String address;
    String contact_mobile;
    String city;
    String district;
    String state;

    protected BuisnessModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        category_id = in.readString();
        business_name = in.readString();
        description = in.readString();
        contact_name = in.readString();
        business_banner = in.readString();
        email = in.readString();
        address = in.readString();
        contact_mobile = in.readString();
        city = in.readString();
        district = in.readString();
        state = in.readString();
        total_product = in.readString();
        pincode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(category_id);
        dest.writeString(business_name);
        dest.writeString(description);
        dest.writeString(contact_name);
        dest.writeString(business_banner);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(contact_mobile);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(state);
        dest.writeString(total_product);
        dest.writeString(pincode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BuisnessModel> CREATOR = new Creator<BuisnessModel>() {
        @Override
        public BuisnessModel createFromParcel(Parcel in) {
            return new BuisnessModel(in);
        }

        @Override
        public BuisnessModel[] newArray(int size) {
            return new BuisnessModel[size];
        }
    };

    public String getTotal_product() {
        return total_product;
    }

    public void setTotal_product(String total_product) {
        this.total_product = total_product;
    }

    String total_product;

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


    String pincode;

    public BuisnessModel(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.user_id=object.getString("user_id");
            this.category_id=object.getString("category_id");
            this.business_name=object.getString("business_name");
            this.description=object.getString("description");
            this.contact_name=object.getString("contact_name");
            this.business_banner=object.getString("business_banner");
            this.email=object.getString("email");
            this.address=object.getString("address");
            this.contact_mobile=object.getString("contact_mobile");
            this.city=object.getString("city");
            this.district=object.getString("district");
            this.state=object.getString("state");
            this.pincode=object.getString("pincode");
            this.total_product=object.getString("totalProducts");
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}
