package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VendorByProduct implements Parcelable {


    private String produtId;
    private String original_cost;
    private String selling_price;
    private String offer_type;
    private String offer_price;
    private String id;
    private String user_id;
    private String business_name;
    private String description;
    private String contact_name;
    private String email;
    private String contact_mobile;
    private String address;
    private String latitude;
    private String longitude;
    private String category_id;
    private String land_mark;
    private String city;
    private String district;
    private String state;
    private String pincode;
    private String business_licence_type;
    private String licence_no;
    private String expiry_date;
    private String itr;
    private String shop_age;
    private String gst_exemption;
    private String gst_no;
    private String birth_date;
    private String gender;
    private String member_name;
    private String business_banner;

    private String Offerid;
    private String Offername;
    private String Offervalidity;
    private String Offerdescription;
    private String Offeroffer_type;
    private String Offeroffer_price;
    private String Offerterms;
    private String Offerimage;
    private String Offerstatus;
    private String Offeroffer_code;
    private String Offeroffer_limit;

    public VendorByProduct(JSONObject object) {
        try {

            this.produtId = object.getString("produtId");
            this.original_cost = object.getString("original_cost");
            this.selling_price = object.getString("selling_price");
            this.offer_type = object.getString("offer_type");
            this.offer_price = object.getString("offer_price");
            this.id = object.getString("id");
            this.user_id = object.getString("user_id");
            this.business_name = object.getString("business_name");
            this.description = object.getString("description");
            this.contact_name = object.getString("contact_name");
            this.email = object.getString("email");
            this.contact_mobile = object.getString("contact_mobile");
            this.address = object.getString("address");
            this.latitude = object.getString("latitude");
            this.longitude = object.getString("longitude");
            this.category_id = object.getString("category_id");
            this.land_mark = object.getString("land_mark");
            this.city = object.getString("city");
            this.district = object.getString("district");
            this.state = object.getString("state");
            this.pincode = object.getString("pincode");
            this.business_licence_type = object.getString("business_licence_type");
            this.licence_no = object.getString("licence_no");
            this.expiry_date = object.getString("expiry_date");
            this.itr = object.getString("itr");
            this.shop_age = object.getString("shop_age");
            this.gst_exemption = object.getString("gst_exemption");
            this.gst_no = object.getString("gst_no");
            this.birth_date = object.getString("birth_date");
            this.gender = object.getString("gender");
            this.member_name = object.getString("member_name");
            this.business_banner = object.getString("business_banner");


            JSONArray jsonArray = object.getJSONArray("offer");
            if (jsonArray.length() != 0) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                this.Offerid = jsonObject1.getString("id");
                this.Offername = jsonObject1.getString("name");
                this.Offervalidity = jsonObject1.getString("validity");
                this.Offerdescription = jsonObject1.getString("description");
                this.Offeroffer_type = jsonObject1.getString("offer_type");
                this.Offeroffer_price = jsonObject1.getString("offer_price");
                this.Offerterms = jsonObject1.getString("terms");
                this.Offerimage = jsonObject1.getString("image");
                this.Offerstatus = jsonObject1.getString("status");
                this.Offeroffer_code = jsonObject1.getString("offer_code");
                this.Offeroffer_limit = jsonObject1.getString("offer_limit");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected VendorByProduct(Parcel in) {
        produtId = in.readString();
        original_cost = in.readString();
        selling_price = in.readString();
        offer_type = in.readString();
        offer_price = in.readString();
        id = in.readString();
        user_id = in.readString();
        business_name = in.readString();
        description = in.readString();
        contact_name = in.readString();
        email = in.readString();
        contact_mobile = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        category_id = in.readString();
        land_mark = in.readString();
        city = in.readString();
        district = in.readString();
        state = in.readString();
        pincode = in.readString();
        business_licence_type = in.readString();
        licence_no = in.readString();
        expiry_date = in.readString();
        itr = in.readString();
        shop_age = in.readString();
        gst_exemption = in.readString();
        gst_no = in.readString();
        birth_date = in.readString();
        gender = in.readString();
        member_name = in.readString();
        business_banner = in.readString();
        Offerid = in.readString();
        Offername = in.readString();
        Offervalidity = in.readString();
        Offerdescription = in.readString();
        Offeroffer_type = in.readString();
        Offeroffer_price = in.readString();
        Offerterms = in.readString();
        Offerimage = in.readString();
        Offerstatus = in.readString();
        Offeroffer_code = in.readString();
        Offeroffer_limit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(produtId);
        dest.writeString(original_cost);
        dest.writeString(selling_price);
        dest.writeString(offer_type);
        dest.writeString(offer_price);
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(business_name);
        dest.writeString(description);
        dest.writeString(contact_name);
        dest.writeString(email);
        dest.writeString(contact_mobile);
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(category_id);
        dest.writeString(land_mark);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(business_licence_type);
        dest.writeString(licence_no);
        dest.writeString(expiry_date);
        dest.writeString(itr);
        dest.writeString(shop_age);
        dest.writeString(gst_exemption);
        dest.writeString(gst_no);
        dest.writeString(birth_date);
        dest.writeString(gender);
        dest.writeString(member_name);
        dest.writeString(business_banner);
        dest.writeString(Offerid);
        dest.writeString(Offername);
        dest.writeString(Offervalidity);
        dest.writeString(Offerdescription);
        dest.writeString(Offeroffer_type);
        dest.writeString(Offeroffer_price);
        dest.writeString(Offerterms);
        dest.writeString(Offerimage);
        dest.writeString(Offerstatus);
        dest.writeString(Offeroffer_code);
        dest.writeString(Offeroffer_limit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VendorByProduct> CREATOR = new Creator<VendorByProduct>() {
        @Override
        public VendorByProduct createFromParcel(Parcel in) {
            return new VendorByProduct(in);
        }

        @Override
        public VendorByProduct[] newArray(int size) {
            return new VendorByProduct[size];
        }
    };

    public String getProdutId() {
        return produtId;
    }

    public void setProdutId(String produtId) {
        this.produtId = produtId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLand_mark() {
        return land_mark;
    }

    public void setLand_mark(String land_mark) {
        this.land_mark = land_mark;
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

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getItr() {
        return itr;
    }

    public void setItr(String itr) {
        this.itr = itr;
    }

    public String getShop_age() {
        return shop_age;
    }

    public void setShop_age(String shop_age) {
        this.shop_age = shop_age;
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

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getBusiness_banner() {
        return business_banner;
    }

    public void setBusiness_banner(String business_banner) {
        this.business_banner = business_banner;
    }

    public String getOffername() {
        return Offername;
    }

    public void setOffername(String offername) {
        Offername = offername;
    }

    public String getOffervalidity() {
        return Offervalidity;
    }

    public void setOffervalidity(String offervalidity) {
        Offervalidity = offervalidity;
    }

    public String getOfferdescription() {
        return Offerdescription;
    }

    public void setOfferdescription(String offerdescription) {
        Offerdescription = offerdescription;
    }

    public String getOfferoffer_type() {
        return Offeroffer_type;
    }

    public void setOfferoffer_type(String offeroffer_type) {
        Offeroffer_type = offeroffer_type;
    }

    public String getOfferoffer_price() {
        return Offeroffer_price;
    }

    public void setOfferoffer_price(String offeroffer_price) {
        Offeroffer_price = offeroffer_price;
    }

    public String getOfferterms() {
        return Offerterms;
    }

    public void setOfferterms(String offerterms) {
        Offerterms = offerterms;
    }

    public String getOfferimage() {
        return Offerimage;
    }

    public void setOfferimage(String offerimage) {
        Offerimage = offerimage;
    }

    public String getOfferstatus() {
        return Offerstatus;
    }

    public void setOfferstatus(String offerstatus) {
        Offerstatus = offerstatus;
    }

    public String getOfferoffer_code() {
        return Offeroffer_code;
    }

    public void setOfferoffer_code(String offeroffer_code) {
        Offeroffer_code = offeroffer_code;
    }

    public String getOfferoffer_limit() {
        return Offeroffer_limit;
    }

    public void setOfferoffer_limit(String offeroffer_limit) {
        Offeroffer_limit = offeroffer_limit;
    }

    public String getOfferid() {
        return Offerid;
    }

    public void setOfferid(String offerid) {
        Offerid = offerid;
    }
}

