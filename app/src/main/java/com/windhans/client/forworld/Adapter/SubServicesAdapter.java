package com.windhans.client.forworld.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windhans.client.forworld.Activities.ActivityServiceListing;
import com.windhans.client.forworld.Activities.ActivitySubServices;
import com.windhans.client.forworld.Model.SubService;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.ArrayList;
import java.util.List;

public class SubServicesAdapter extends RecyclerView.Adapter<SubServicesAdapter.MyViewHolder> {
    Context context;
    View view;
    List<SubService> subServiceArrayList;



    public SubServicesAdapter(Context context, ArrayList<SubService> subServiceArrayList) {
        this.context=context;
        this.subServiceArrayList=subServiceArrayList;
    }

    @Override
    public SubServicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        SubServicesAdapter.MyViewHolder holder = new SubServicesAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final SubServicesAdapter.MyViewHolder holder, final int position) {
        SubService subCategory = subServiceArrayList.get(position);

        holder.txt_categoryName.setText(subServiceArrayList.get(position).getCategory_name());

        if(Shared_Preferences.getPrefs(context, "select") != null) {
            if (Shared_Preferences.getPrefs(context, "select").equals("0")) {
                holder.txt_categoryName.setText(subServiceArrayList.get(position).getCategory_name());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("1")) {
                holder.txt_categoryName.setText(subServiceArrayList.get(position).getCategory_name_hindi());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("2")) {
                holder.txt_categoryName.setText(subServiceArrayList.get(position).getCategory_name_marathi());
            }
        }else {
            holder.txt_categoryName.setText(subServiceArrayList.get(position).getCategory_name());
        }

        String name=subCategory.getCategory_name();
        String firstChar=name.substring(0);
        holder.image_name.setText(firstChar+"");
        holder.image_name.setAllCaps(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ActivityServiceListing.class);
                intent.putExtra(Constants.SubServiceID, subCategory.getCategory_id());
                intent.putExtra(Constants.SubServiceName, subCategory.getCategory_name());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subServiceArrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_categoryName;
        TextView image_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt_categoryName=itemView.findViewById(R.id.txt_categoryName);
            image_name=itemView.findViewById(R.id.image_name);



        }

    }

}


