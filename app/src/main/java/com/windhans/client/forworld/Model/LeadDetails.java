package com.windhans.client.forworld.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class LeadDetails {

    String status;
    String lead_id;
    String lead_reason;
    String created_at;

    public LeadDetails(JSONObject object) {
        try {

                this.lead_id=object.getString("lead_id");
                this.lead_reason=object.getString("lead_reason");
                this.status=object.getString("status");
                this.created_at=object.getString("created_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getLead_reason() {
        return lead_reason;
    }

    public void setLead_reason(String lead_reason) {
        this.lead_reason = lead_reason;
    }




}
