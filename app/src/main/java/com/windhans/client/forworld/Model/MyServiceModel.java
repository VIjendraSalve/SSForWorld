package com.windhans.client.forworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyServiceModel implements Parcelable {

    String id;
    String name;
    String description;
    String category_id;
    String subcategory_id;
    String user_id;
    String category_name;
    String sub_category;
    String banner_image;
    // Service_Images  service_images =new Service_Images;
    ArrayList<Service_Images> service_imagesArrayList=new ArrayList<>();

    public MyServiceModel(JSONObject object) {
        try {
            this.id=object.getString("id");
            this.name=object.getString("name");
            this.category_id=object.getString("category_id");
            this.subcategory_id=object.getString("subcategory_id");
            this.user_id=object.getString("user_id");
            this.category_name=object.getString("category_name");
            this.banner_image=object.getString("banner_image");
            //  this.multi_image=object.getString("multi_image");
            this.sub_category=object.getString("sub_category");
            this.description=object.getString("description");
            JSONArray jsonArray=object.getJSONArray("other_image");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                service_imagesArrayList.add(new Service_Images(jsonObject));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected MyServiceModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        category_id = in.readString();
        subcategory_id = in.readString();
        user_id = in.readString();
        category_name = in.readString();
        sub_category = in.readString();
        banner_image = in.readString();
        service_imagesArrayList = in.createTypedArrayList(Service_Images.CREATOR);
        description = in.readString();

    }

    public static final Creator<MyServiceModel> CREATOR = new Creator<MyServiceModel>() {
        @Override
        public MyServiceModel createFromParcel(Parcel in) {
            return new MyServiceModel(in);
        }

        @Override
        public MyServiceModel[] newArray(int size) {
            return new MyServiceModel[size];
        }
    };

    public ArrayList<Service_Images> getService_imagesArrayList() {
        return service_imagesArrayList;
    }

    public void setService_imagesArrayList(ArrayList<Service_Images> service_imagesArrayList) {
        this.service_imagesArrayList = service_imagesArrayList;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
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

    public String getRole_id() {
        return user_id;
    }

    public void setRole_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category_id);
        parcel.writeString(subcategory_id);
        parcel.writeString(user_id);
        parcel.writeString(category_name);
        parcel.writeString(sub_category);
        parcel.writeString(banner_image);
        parcel.writeTypedList(service_imagesArrayList);
        parcel.writeString(description);
    }
}
