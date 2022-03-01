package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityLeadDetails1;
import com.windhans.client.forworld.Model.LeadData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class AdapterLeadListing extends RecyclerView.Adapter<AdapterLeadListing.MyViewHolder> {

    private Context context;
    private List<LeadData> leadDataList;
    private ProgressDialog progressDialog;
    View itemView;
    public AdapterLeadListing(List<LeadData> leadDataList)
    {

        this.leadDataList = leadDataList;
    }


    @Override
    public AdapterLeadListing.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_product_item1, parent, false);
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lead_list_new, parent, false);
        AdapterLeadListing.MyViewHolder holder = new AdapterLeadListing.MyViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(AdapterLeadListing.MyViewHolder holder, final int position) {
        LeadData leadData=leadDataList.get(position);
        holder.textview_name.setText(leadDataList.get(position).getName());
        holder.txt_purpose.setText(leadDataList.get(position).getPurpose());
        holder.txt_address.setText(leadDataList.get(position).getAddress());
        holder.txt_contact.setText(leadDataList.get(position).getContact_no());

        Log.d("CreatedAT", "onBindViewHolder: "+leadDataList.get(position).getCreated_at());

        holder.tv_date.setText(DateTimeFormat.getDate1_0(leadDataList.get(position).getCreated_at()));
        holder.tv_month.setText(DateTimeFormat.getDate1_1(leadDataList.get(position).getCreated_at()));
        holder.tv_year.setText(DateTimeFormat.getDate1_2(leadDataList.get(position).getCreated_at()));

        //holder.txt_productName.setText(leadDataList.get(position).getProduct_name());
        String status=leadDataList.get(position).getStatus();
        if(status!=null)
        {
            if(status.equals("0"))
            {
                holder.txt_Description.setText("Pending");
                holder.txt_Description.setTextColor(context.getResources().getColor(R.color.white));
                holder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));
            }
            else if(status.equals("1")){
                holder.txt_Description.setText("Completed");
                holder.txt_Description.setTextColor(context.getResources().getColor(R.color.white));
                holder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.green_new));
            }
            else  if(status.equals("2")){
                holder.txt_Description.setText("Progress");
                holder.txt_Description.setTextColor(context.getResources().getColor(R.color.white));
                holder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.primary_orange_new));
            }else  if(status.equals("3"))
            {
                holder.txt_Description.setText("Rejected");
                holder.txt_Description.setTextColor(context.getResources().getColor(R.color.white));
                holder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.red_new));
            }
        }


        // Toast.makeText(context, ""+productList.get(position).getProduct_name(), Toast.LENGTH_SHORT).show();

        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        holder.image.getLayoutParams().width = (int) (cardwidth / 3);
        holder.image.getLayoutParams().height = (int) (cardheight / 7);
        if (leadData.getImage() != null && !leadData.getImage().isEmpty() && !leadData.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + leadData.getImage())

                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(context, ActivityLeadDetails.class);
                Intent intent=new Intent(context, ActivityLeadDetails1.class);
                Shared_Preferences.setPrefs(context,Constants.LEAD_ID,leadData.getId());
                Shared_Preferences.setPrefs(context,Constants.LEAD_USER_ID,leadData.getCreated_by());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID,leadData.getBusiness_id());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_NAME,leadData.getProduct_name());
                Shared_Preferences.setPrefs(context,Constants.PURPOSE,leadData.getPurpose());
                Shared_Preferences.setPrefs(context,Constants.CONTACT_NO,leadData.getContact_no());
                Shared_Preferences.setPrefs(context,Constants.ADDRESS,leadData.getAddress());
                Shared_Preferences.setPrefs(context,Constants.CITY,leadData.getCity());
                Shared_Preferences.setPrefs(context,Constants.STATUS,leadData.getStatus());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_DEC,leadData.getDescription());
                Shared_Preferences.setPrefs(context,Constants.PRICE,leadData.getPrice());
                Shared_Preferences.setPrefs(context,Constants.IMAGE1,leadData.getImage());
                Log.d("image", "onClick: "+leadData.getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leadDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textview_name, txt_purpose, txt_address,txt_Description,txt_contact, tv_date, tv_month, tv_year;
        ImageView image;
        CardView card_view_background;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            textview_name = (TextView) itemView.findViewById(R.id.textview_name);
            txt_purpose = (TextView) itemView.findViewById(R.id.txt_purpose);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
          /*  txt_productName = (TextView) itemView.findViewById(R.id.txt_productName);*/
            txt_Description = (TextView) itemView.findViewById(R.id.txt_status);
            txt_contact = (TextView) itemView.findViewById(R.id.txt_contact);
//            books = (TextView) itemView.findViewById(R.id.books);
            image = (ImageView) itemView.findViewById(R.id.imageViewpro);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            tv_year = (TextView) itemView.findViewById(R.id.tv_year);
            card_view_background = (CardView) itemView.findViewById(R.id.card_view_background);

        }

    }




}
