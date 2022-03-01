package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityBuisnessListing;
import com.windhans.client.forworld.Model.CategoryModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.MyViewHolder>   {
    Context context;
    List<CategoryModel> dashboardList;
    View view;
    int row_index;
    private int position1;
    RecyclerView recycler_view;


    public BusinessCategoryAdapter(Context context, List<CategoryModel> dashboardList, RecyclerView recycler_view) {
        this.context=context;
        this.dashboardList=dashboardList;
this.recycler_view=recycler_view;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gridlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);



        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        CategoryModel categoryModel=dashboardList.get(position);
        holder.txt_buisnessName.setText(categoryModel.getCategory_name());
        //holder.txt_buisnessDescription.setText(dashboardList.get(position).getDescription());
        String category_id=Shared_Preferences.getPrefs(context,Constants.CATEGORY_ID);
   //    int position2= recycler_view.getChildAdapterPosition(view);

        if (categoryModel.getImage() != null && !categoryModel.getImage().isEmpty() && !categoryModel.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_CategoryPath + categoryModel.getImage())

                    .into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(holder.imageView);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //Intent intent=new Intent(context, ActivityBuisnessListing.class);
              //  holder.cardView_Category.setCardBackgroundColor(context.getResources().getColor(R.color.light_gray));



                Intent intent=new Intent(context, ActivityBuisnessListing.class);
                Shared_Preferences.setPrefs(context, Constants.CATEGORY_ID,categoryModel.getCategory_id());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dashboardList.size();

    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_buisnessName, txt_buisnessDescription;
        CardView cardView_Category;
       // ImageView
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt_buisnessName=itemView.findViewById(R.id.txt_buisnessName);
         /*   txt_buisnessDescription=itemView.findViewById(R.id.txt_buisnessDescription);
            cardView_Category=itemView.findViewById(R.id.cardView_Category);*/
            imageView=itemView.findViewById(R.id.imageView);

        }

    }

}
