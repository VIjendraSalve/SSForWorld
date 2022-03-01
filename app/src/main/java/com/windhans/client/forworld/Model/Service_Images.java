package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Images implements Parcelable {
    String image_id;
    String service_id;
    String image_name;

    public Service_Images(JSONObject jsonObject) {
        try {
            this.image_id=jsonObject.getString("id");
            this.service_id=jsonObject.getString("service_id");
            this.image_name=jsonObject.getString("image_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected Service_Images(Parcel in) {
        image_id = in.readString();
        service_id = in.readString();
        image_name = in.readString();
    }

    public static final Creator<Service_Images> CREATOR = new Creator<Service_Images>() {
        @Override
        public Service_Images createFromParcel(Parcel in) {
            return new Service_Images(in);
        }

        @Override
        public Service_Images[] newArray(int size) {
            return new Service_Images[size];
        }
    };

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_id);
        parcel.writeString(service_id);
        parcel.writeString(image_name);
    }
}
