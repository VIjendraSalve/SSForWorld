package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityBuisnessListing;
import com.windhans.client.forworld.Activities.ActivityBusinessDetail;
import com.windhans.client.forworld.Activities.ActivityBusinessDetailNew;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.ArrayList;
import java.util.List;

public class BuisnessListAdapter extends RecyclerView.Adapter<BuisnessListAdapter.MyViewHolder> {
    Context context;
    ArrayList<BuisnessModel> buisnessModelList;
    View view;

    public BuisnessListAdapter(ActivityBuisnessListing activityBuisnessListing, ArrayList<BuisnessModel> buisnessModelList) {
        this.context = activityBuisnessListing;
        this.buisnessModelList = buisnessModelList;
    }


    @NonNull
    @Override
    public BuisnessListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.buisness_list, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BuisnessListAdapter.MyViewHolder myViewHolder, int position) {
        BuisnessModel model = buisnessModelList.get(position);
        myViewHolder.txt_BuisnessTitle.setText(model.getBusiness_name());
        myViewHolder.txt_description.setText(model.getDescription());
        myViewHolder.txt_location.setText(model.getAddress());

        if (model.getBusiness_banner() != null && !model.getBusiness_banner().isEmpty() && !model.getBusiness_banner().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessPath + model.getBusiness_banner())
                    .into(myViewHolder.iv_image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(myViewHolder.iv_image);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared_Preferences.setPrefs(context, Constants.BUISNESS_BANNER, buisnessModelList.get(position).getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_IMAGE, buisnessModelList.get(position).getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_NAME, buisnessModelList.get(position).getBusiness_name());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DEC, buisnessModelList.get(position).getDescription());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CONTACT, buisnessModelList.get(position).getContact_mobile());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_EMAIL, buisnessModelList.get(position).getEmail());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ADDRESS, buisnessModelList.get(position).getAddress());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CITY, buisnessModelList.get(position).getCity());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DISTRICT, buisnessModelList.get(position).getDistrict());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_STATE, buisnessModelList.get(position).getState());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_PINCODE, buisnessModelList.get(position).getPincode());
                Shared_Preferences.setPrefs(context, Constants.TOTAL_PRODUCT, buisnessModelList.get(position).getTotal_product());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ADDRESS, buisnessModelList.get(position).getAddress());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CITY, buisnessModelList.get(position).getCity());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DISTRICT, buisnessModelList.get(position).getDistrict());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_PINCODE, buisnessModelList.get(position).getPincode());

                Intent intent = new Intent(context, ActivityBusinessDetailNew.class);
                intent.putParcelableArrayListExtra(Constants.VendorDetailList, buisnessModelList);
                intent.putExtra(Constants.PositionOfVendor, position);
                context.startActivity(intent);

                // context.startActivity(intent);
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID, model.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return buisnessModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Regular  txt_description, txt_location;
        MyTextView_Poppins_Bold txt_BuisnessTitle;
        ImageView iv_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_BuisnessTitle = itemView.findViewById(R.id.txt_BuisnessTitle);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_location = itemView.findViewById(R.id.txt_location);

            iv_image = itemView.findViewById(R.id.iv_image);
        }
    }
}
