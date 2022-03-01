package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessProfileModel {
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

    String land_mark;
    String city;
    String district;
    String state;
    String pincode;
    String business_licence_type;
    String licence_no;
    String expiry_date;
    String shop_age;
    String gst_exemption;
    String gst_no;
    String dob;
    String gender;
    String aadhar_no;
    String pan_card_no;
    String bank_name;
    String account_no;
    String ifsc;
    String member_name;
    String seller_name;
    String googlepay;
    String phonepay;
    String paytm;
    String shop_name;
    String mobile_no2;


    public BusinessProfileModel(JSONObject jsonObject1) {
        try {
            this.id=jsonObject1.getString("id");
            this.user_id=jsonObject1.getString("user_id");
            this.category_id=jsonObject1.getString("category_id");
            this.business_name=jsonObject1.getString("business_name");
            this.description=jsonObject1.getString("description");
            this.contact_name=jsonObject1.getString("contact_name");
            this.business_banner=jsonObject1.getString("business_banner");
            this.email=jsonObject1.getString("email");
            this.address=jsonObject1.getString("address");
            this.contact_mobile=jsonObject1.getString("contact_mobile");
            this.land_mark=jsonObject1.getString("land_mark");

            this.city=jsonObject1.getString("city");
            this.district=jsonObject1.getString("district");
            this.state=jsonObject1.getString("state");
            this.pincode=jsonObject1.getString("pincode");
            this.business_licence_type=jsonObject1.getString("business_licence_type");
            this.licence_no=jsonObject1.getString("licence_no");
            this.expiry_date=jsonObject1.getString("expiry_date");
            this.shop_age=jsonObject1.getString("shop_age");
            this.gst_exemption=jsonObject1.getString("gst_exemption");
            this.gst_no=jsonObject1.getString("gst_no");
            this.dob=jsonObject1.getString("birth_date");
            this.gender=jsonObject1.getString("gender");
            this.aadhar_no=jsonObject1.getString("aadhar_no");
            this.pan_card_no=jsonObject1.getString("pan_card_no");
            this.bank_name=jsonObject1.getString("bank_name");
            this.account_no=jsonObject1.getString("account_no");
            this.ifsc=jsonObject1.getString("ifsc");
            this.member_name=jsonObject1.getString("member_name");
            this.seller_name=jsonObject1.getString("seller_name");
            this.googlepay=jsonObject1.getString("googlepay");
            this.phonepay=jsonObject1.getString("phonepay");
            this.paytm=jsonObject1.getString("paytm");
            this.shop_name=jsonObject1.getString("shop_name");
            this.mobile_no2=jsonObject1.getString("mobile_no2");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBusiness_licence_type() {
        return business_licence_type;
    }

    public void setBusiness_licence_type(String business_licence_type) {
        this.business_licence_type = business_licence_type;
    }

    public String getLicence_no() {
        return licence_no;
    }

    public void setLicence_no(String licence_no) {
        this.licence_no = licence_no;
    }

    public String getGst_exemption() {
        return gst_exemption;
    }

    public void setGst_exemption(String gst_exemption) {
        this.gst_exemption = gst_exemption;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getMobile_no2() {
        return mobile_no2;
    }

    public void setMobile_no2(String mobile_no2) {
        this.mobile_no2 = mobile_no2;
    }


    public String getLand_mark() {
        return land_mark;
    }

    public void setLand_mark(String land_mark) {
        this.land_mark = land_mark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getShop_age() {
        return shop_age;
    }

    public void setShop_age(String shop_age) {
        this.shop_age = shop_age;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getPan_card_no() {
        return pan_card_no;
    }

    public void setPan_card_no(String pan_card_no) {
        this.pan_card_no = pan_card_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
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

    public String getGooglepay() {
        return googlepay;
    }

    public void setGooglepay(String googlepay) {
        this.googlepay = googlepay;
    }

    public String getPhonepay() {
        return phonepay;
    }

    public void setPhonepay(String phonepay) {
        this.phonepay = phonepay;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }
}
