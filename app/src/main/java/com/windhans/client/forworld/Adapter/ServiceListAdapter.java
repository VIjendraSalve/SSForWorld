package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityBuisnessListing;
import com.windhans.client.forworld.Activities.ActivityBusinessDetail;
import com.windhans.client.forworld.Activities.GenrateEnquiryForService;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.ServiceData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder>{
    Context context;
    List<ServiceData> serviceDataList;
    View view;

    public ServiceListAdapter(Context context, List<ServiceData> serviceDataList) {
        this.context=context;
        this.serviceDataList=serviceDataList;
    }



    @NonNull
    @Override
    public ServiceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_listview, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.MyViewHolder myViewHolder, int position) {
        ServiceData model=serviceDataList.get(position);
        myViewHolder.txt_BuisnessTitle.setText(model.getName());
        myViewHolder.txt_description.setText(model.getDescription());
        myViewHolder.txt_location.setText(model.getAddress());
        myViewHolder.tv_businessName.setText(model.getBusiness_name());
        myViewHolder.tv_businessMobile.setText(model.getContact_mobile());

        if (model.getBanner_image() != null && !model.getBanner_image().isEmpty() && !model.getBanner_image().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_ServicePath + model.getBanner_image())
                    .into(myViewHolder.iv_image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(myViewHolder.iv_image);
        }
        myViewHolder.btn_add_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenrateEnquiryForService.class);
                intent.putExtra(Constants.ServiceIDForEnquiry, serviceDataList.get(position).getId());
                intent.putExtra(Constants.BusinessIDForEnquiry,serviceDataList.get(position).getUser_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Bold txt_BuisnessTitle,txt_description, tv_businessName, tv_businessMobile;
        MyTextView_Poppins_Regular txt_location;
        ImageView iv_image;
        Button btn_add_enquiry;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_BuisnessTitle=itemView.findViewById(R.id.txt_BuisnessTitle);
            txt_description=itemView.findViewById(R.id.txt_description);
            txt_location=itemView.findViewById(R.id.txt_location);
            tv_businessName=itemView.findViewById(R.id.tv_businessName);
            tv_businessMobile=itemView.findViewById(R.id.tv_businessMobile);
            btn_add_enquiry=itemView.findViewById(R.id.btn_add_enquiry);
            iv_image=itemView.findViewById(R.id.iv_image);
        }
    }
}
