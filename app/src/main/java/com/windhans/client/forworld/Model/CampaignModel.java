package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class CampaignModel {
    String id;
    String business_id;
    String ads_unit_id;
    String title;
    String description;
    String from_date;
    String to_date;
    String ad_banner;
    String is_enable_enquiry;
    String is_locationbased_enquiry;

    public CampaignModel(JSONObject jsonObject) {
        try {
            this.id=jsonObject.getString("id");
            this.business_id=jsonObject.getString("business_id");
            this.ads_unit_id=jsonObject.getString("ads_unit_id");
            this.title=jsonObject.getString("title");
            this.description=jsonObject.getString("description");
            this.from_date=jsonObject.getString("from_date");
            this.to_date=jsonObject.getString("to_date");
            this.ad_banner=jsonObject.getString("ad_banner");
            this.is_enable_enquiry=jsonObject.getString("is_enable_enquiry");
            this.is_locationbased_enquiry=jsonObject.getString("is_locationbased_enquiry");
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

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getAds_unit_id() {
        return ads_unit_id;
    }

    public void setAds_unit_id(String ads_unit_id) {
        this.ads_unit_id = ads_unit_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getAd_banner() {
        return ad_banner;
    }

    public void setAd_banner(String ad_banner) {
        this.ad_banner = ad_banner;
    }

    public String getIs_enable_enquiry() {
        return is_enable_enquiry;
    }

    public void setIs_enable_enquiry(String is_enable_enquiry) {
        this.is_enable_enquiry = is_enable_enquiry;
    }

    public String getIs_locationbased_enquiry() {
        return is_locationbased_enquiry;
    }

    public void setIs_locationbased_enquiry(String is_locationbased_enquiry) {
        this.is_locationbased_enquiry = is_locationbased_enquiry;
    }


}
