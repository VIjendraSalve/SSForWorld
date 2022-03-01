package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.Activities.Activity_My_Enquiry;
import com.windhans.client.forworld.Activities.EnquiryListActivity;
import com.windhans.client.forworld.Model.EnquiryModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class EnquiryListAdapter extends RecyclerView.Adapter<EnquiryListAdapter.MyViewHolder>{
    Context context;
    List<EnquiryModel> userEnquiryList;
    //List<UserEnquiry> userEnquiryList;
    View view;



    public EnquiryListAdapter(EnquiryListActivity activityLeadDetails, List<EnquiryModel> userEnquiryList) {
        this.context=activityLeadDetails;
        this.userEnquiryList=userEnquiryList;
    }



    @NonNull
    @Override
    public EnquiryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_enqiry_list1, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_enqiry_list_new, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryListAdapter.MyViewHolder myViewHolder, int position) {

        final EnquiryModel type = userEnquiryList.get(position);
        myViewHolder.txt_product_name.setText(userEnquiryList.get(position).getName());
        myViewHolder.txt_purpose.setText(userEnquiryList.get(position).getPurpose());
        myViewHolder.txt_contact.setText(userEnquiryList.get(position).getContact_no());
        myViewHolder.txt_address.setText(userEnquiryList.get(position).getAddress());
        myViewHolder.txt_date.setText(DateTimeFormat.getDate(userEnquiryList.get(position).getCreated_at()));

        myViewHolder.tv_date.setText(DateTimeFormat.getDate1_0(userEnquiryList.get(position).getCreated_at()));
        myViewHolder.tv_month.setText(DateTimeFormat.getDate1_1(userEnquiryList.get(position).getCreated_at()));
        myViewHolder.tv_year.setText(DateTimeFormat.getDate1_2(userEnquiryList.get(position).getCreated_at()));



        String status= userEnquiryList.get(position).getStatus();
        if(status.equals("0"))
        {
            myViewHolder.txt_status.setText("Pending");
            myViewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.apptheme));
        }
        else  if(status.equals("1"))
        {
            myViewHolder.txt_status.setText("Complete");
            myViewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.green_a));
        }
        else
        if(status.equals("2"))
        {
            myViewHolder.txt_status.setText("Progress");
            myViewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.primary_orange));
        }
        else   if(status.equals("3"))
        {
            myViewHolder.txt_status.setText("Rejected");
            myViewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.card_view_background.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        }

      /*  myViewHolder.txt_product_name.setText(userEnquiryList.get(position).getProduct_name());
        if (type.getImage() != null && !type.getImage().isEmpty() && !type.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + type.getImage())
                    .skipMemoryCache(true)
                    .into(myViewHolder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .skipMemoryCache(true)
                    .into(myViewHolder.image);
        }*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME,type.getName());
                //Shared_Preferences.setPrefs(context,Constants.BRAND,type.getBrand());
             //   Shared_Preferences.setPrefs(context,Constants.DESCRIPTION,type.getDescription());
               // Shared_Preferences.setPrefs(context,Constants.PRODUCT_IMAGE,type.getImage());
                Shared_Preferences.setPrefs(context,Constants.PURPOSE,userEnquiryList.get(position).getPurpose());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_NAME,userEnquiryList.get(position).getBusiness_name());
                Shared_Preferences.setPrefs(context,Constants.STATUS,userEnquiryList.get(position).getStatus());
                Shared_Preferences.setPrefs(context,Constants.LEAD_ID,userEnquiryList.get(position).getId());
               // Intent intent=new Intent(context, EnquiryDetailsActivity1.class);
                Intent intent=new Intent(context, Activity_My_Enquiry.class);
                intent.putExtra("leadID",type.getId());
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return userEnquiryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MyTextView_Poppins_Bold txt_product_name, txt_status, tv_date, tv_month, tv_year;
        MyTextView_Poppins_Regular txt_contact;
        TextView txt_userName, txt_purpose,txt_address, txt_date;
        ImageView image, iv_delete_user;
        CardView card_view_background;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_date = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.tv_date);
            tv_month = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.tv_month);
            tv_year = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.tv_year);
            txt_userName = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_purpose = (TextView) itemView.findViewById(R.id.txt_purpose);
            txt_contact = (MyTextView_Poppins_Regular) itemView.findViewById(R.id.txt_contact);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_product_name = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.txt_product_name);
            txt_status = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.txt_status);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
//            books = (TextView) itemView.findViewById(R.id.books);
            image = (ImageView) itemView.findViewById(R.id.imageViewpro);
            card_view_background = (CardView) itemView.findViewById(R.id.card_view_background);

        }
    }
}


