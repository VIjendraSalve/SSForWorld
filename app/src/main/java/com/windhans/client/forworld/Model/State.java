package com.windhans.client.forworld.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class State {

    private String id;
    private String name;

    public State(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public State(JSONObject object) {

        try {

            this.id=object.getString("id");
            this.name=object.getString("name");

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
