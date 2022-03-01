package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivitySubServices;
import com.windhans.client.forworld.Activities.BuisnessHome;
import com.windhans.client.forworld.Model.Services;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class DashboardAdapterForServices extends RecyclerView.Adapter<DashboardAdapterForServices.MyViewHolder> {
    Context context;
    List<Services> dashboardList;
    View view;
    public DashboardAdapterForServices(Context context, List<Services> dashboardList) {
        this.context=context;
        this.dashboardList=dashboardList;
    }

    public DashboardAdapterForServices(BuisnessHome buisnessHome, List<Services> categoryModelList, RecyclerView recycler_view) {
        this.context=buisnessHome;
        this.dashboardList=categoryModelList;
    }

    public DashboardAdapterForServices(Context context, List<Services> categoryModelList, RecyclerView recycler_view) {
        this.context=context;
        this.dashboardList=categoryModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gridlayout_business, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Services services =dashboardList.get(position);

        holder.txt_buisnessName.setText(services.getCategory_name());

        if(Shared_Preferences.getPrefs(context, "select") != null) {
            if (Shared_Preferences.getPrefs(context, "select").equals("0")) {
                holder.txt_buisnessName.setText(services.getCategory_name());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("1")) {
                holder.txt_buisnessName.setText(services.getCategory_name_hindi());
            } else if (Shared_Preferences.getPrefs(context, "select").equals("2")) {
                holder.txt_buisnessName.setText(services.getCategory_name_marathi());
            }
        }else {
            holder.txt_buisnessName.setText(services.getCategory_name());
        }

      /*  DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        holder.imageView.getLayoutParams().width = (int) (cardwidth / 5);
        holder.imageView.getLayoutParams().height = (int) (cardheight /8);*/
        if(services.getCategory_id().equals("-1")) {

           /* if (services.getImage() != null && !services.getImage().isEmpty() && !services.getImage().equals("null")) {
                Log.d("image", "response: ");*/

                Glide.with(context)
                        .load(R.drawable.see_more)
                        .into(holder.imageView);
           /* } else {
                Glide.with(context)
                        .load(R.drawable.no_image_available)
                        .skipMemoryCache(true)
                        .into(holder.imageView);
            }*/

        }
        else {
            if (services.getImage() != null && !services.getImage().isEmpty() && !services.getImage().equals("null")) {
                Log.d("image", "response: ");

                Glide.with(context)
                        .load(MyConfig.JSON_CategoryPath + services.getImage())
                        .into(holder.imageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.no_image_available)
                        .into(holder.imageView);
            }
        }
        //holder.txt_buisnessDescription.setText(dashboardList.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivitySubServices.class);
                intent.putExtra(Constants.ServiceId, services.getCategory_id());
                intent.putExtra(Constants.ServiceName, services.getCategory_name());
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
      ImageView imageView;
       // ImageView
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt_buisnessName=itemView.findViewById(R.id.txt_buisnessName);
            imageView=itemView.findViewById(R.id.imageView);
         //   txt_buisnessDescription=itemView.findViewById(R.id.txt_buisnessDescription);


        }

    }

}
