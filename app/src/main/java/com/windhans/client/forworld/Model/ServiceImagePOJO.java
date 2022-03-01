package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.windhans.client.forworld.my_library.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;

/**
 * Created by wind Prasanna on 11/1/2017.
 */

public class ServiceImagePOJO implements Parcelable {

    private String img_id;
    private String img_name;
    private MultipartBody.Part image_path_multipart;
    private String img_path_local;
    private String pg_requirement_id;
    private int image_type = 0;


    public ServiceImagePOJO(String img_id, String img_name, MultipartBody.Part image_path_multipart, String img_path_local) {
        this.img_id = img_id;
        this.img_name = img_name;
        this.img_path_local = img_path_local;
        this.image_path_multipart = image_path_multipart;
    }

    public ServiceImagePOJO(JSONObject jsonObject, int image_type) {
        this.image_type = image_type;
        try {
            switch (image_type) {

                case Constants.PROFILE_DOC_IMAGE:
                    this.img_id = "" + jsonObject.getString("pg_profile_id");
                    this.img_name = "" + jsonObject.getString("image");
                    this.pg_requirement_id = "" + jsonObject.getString("pg_signup_id");
                    break;

                case Constants.ROOM_IMAGE:
                    this.img_id = "" + jsonObject.getString("pg_requirement_image_id");
                    this.img_name = "" + jsonObject.getString("pg_requirement_image");
                    this.pg_requirement_id = "" + jsonObject.getString("pg_requirement_id");
                    break;

                case Constants.ROOM_MEMBER_DOC_IMAGE:
                    this.img_id = "" + jsonObject.getString("user_documents_id");
                    this.img_name = "" + jsonObject.getString("image");
                    this.pg_requirement_id = "" + jsonObject.getString("requirement_user_id");
                    //requirement user id is member registration id
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.img_path_local = "";
    }

    protected ServiceImagePOJO(Parcel in) {
        img_id = in.readString();
        img_name = in.readString();
        img_path_local = in.readString();
        pg_requirement_id = in.readString();
        image_type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img_id);
        dest.writeString(img_name);
        dest.writeString(img_path_local);
        dest.writeString(pg_requirement_id);
        dest.writeInt(image_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceImagePOJO> CREATOR = new Creator<ServiceImagePOJO>() {
        @Override
        public ServiceImagePOJO createFromParcel(Parcel in) {
            return new ServiceImagePOJO(in);
        }

        @Override
        public ServiceImagePOJO[] newArray(int size) {
            return new ServiceImagePOJO[size];
        }
    };

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public MultipartBody.Part getImage_path_multipart() {
        return image_path_multipart;
    }

    public void setImage_path_multipart(MultipartBody.Part image_path_multipart) {
        this.image_path_multipart = image_path_multipart;
    }

    public String getImg_path_local() {
        return img_path_local;
    }

    public void setImg_path_local(String img_path_local) {
        this.img_path_local = img_path_local;
    }

    public String getPg_requirement_id() {
        return pg_requirement_id;
    }

    public int getImage_type() {
        return image_type;
    }
}
