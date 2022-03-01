package com.windhans.client.forworld.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class District {

    private String id;
    private String name;
    private String state_id;

    public District(String id, String name, String state_id) {
        this.id = id;
        this.name = name;
        this.state_id = state_id;
    }

    public District(JSONObject object) {

        try {

            this.id=object.getString("id");
            this.name=object.getString("name");
            this.state_id=object.getString("state_id");

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

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
