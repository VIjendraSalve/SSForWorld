package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windhans.client.forworld.Activities.ActivityProductList;
import com.windhans.client.forworld.Model.SubCategory;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    Context context;
    View view;
  List<SubCategory> subCategoryArrayList;



    public SubCategoryAdapter(Context context, ArrayList<SubCategory> subCategoryArrayList) {
        this.context=context;
        this.subCategoryArrayList=subCategoryArrayList;
    }

    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        SubCategoryAdapter.MyViewHolder holder = new SubCategoryAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final SubCategoryAdapter.MyViewHolder holder, final int position) {
        SubCategory subCategory = subCategoryArrayList.get(position);

        holder.txt_categoryName.setText(subCategoryArrayList.get(position).getCategory_name());

        if(Shared_Preferences.getPrefs(context, "select") != null) {
            if (Shared_Preferences.getPrefs(context, "select").equals("0")) {
                holder.txt_categoryName.setText(subCategoryArrayList.get(position).getCategory_name());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("1")) {
                holder.txt_categoryName.setText(subCategoryArrayList.get(position).getCategory_name_hindi());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("2")) {
                holder.txt_categoryName.setText(subCategoryArrayList.get(position).getCategory_name_marathi());
            }
        }else {
            holder.txt_categoryName.setText(subCategoryArrayList.get(position).getCategory_name());
        }

        String name=subCategory.getCategory_name();
        String firstChar=name.substring(0);
        holder.image_name.setText(firstChar+"");
        holder.image_name.setAllCaps(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared_Preferences.setPrefs(context, Constants.SUBCATEGORY,subCategory.getId());
                Intent intent=new Intent(context, ActivityProductList.class);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return subCategoryArrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Regular txt_categoryName;
        TextView image_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt_categoryName=itemView.findViewById(R.id.txt_categoryName);
            image_name=itemView.findViewById(R.id.image_name);



        }

    }

}


