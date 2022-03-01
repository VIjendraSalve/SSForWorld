package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityLead;
import com.windhans.client.forworld.Model.UserEnquiry;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.MyConfig;

import java.util.List;

public class UserEnqiryAdapter extends RecyclerView.Adapter<UserEnqiryAdapter.MyViewHolder> {

    private Context context;
    List<UserEnquiry> userEnquiryList;
    private ProgressDialog progressDialog;



    public UserEnqiryAdapter(ActivityLead activityLead, List<UserEnquiry> userEnquiryList) {
        this.context=activityLead;
        this.userEnquiryList=userEnquiryList;
    }



    @Override
    public UserEnqiryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_product_item2, parent, false);
        UserEnqiryAdapter.MyViewHolder holder = new UserEnqiryAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(UserEnqiryAdapter.MyViewHolder holder, final int position) {


        final UserEnquiry type = userEnquiryList.get(position);
        holder.txt_userName.setText(userEnquiryList.get(position).getName());
        holder.txt_purpose.setText(userEnquiryList.get(position).getPurpose());
        holder.txt_contact.setText(userEnquiryList.get(position).getContact_no());
        holder.txt_address.setText(userEnquiryList.get(position).getAddress());
       String status= userEnquiryList.get(position).getStatus();
       if(status.equals("0"))
       {
           holder.txt_status.setText("Pending");
           holder.txt_status.setTextColor(context.getResources().getColor(R.color.blue));
       }
       else  if(status.equals("1"))
       {
           holder.txt_status.setText("Complete");
           holder.txt_status.setTextColor(context.getResources().getColor(R.color.green_a));
       }
       else
       if(status.equals("2"))
       {
           holder.txt_status.setText("Progress");
           holder.txt_status.setTextColor(context.getResources().getColor(R.color.primary_orange));
       }
       else   if(status.equals("3"))
       {
           holder.txt_status.setText("Rejected");
           holder.txt_status.setTextColor(context.getResources().getColor(R.color.red));
       }
        holder.txt_product_name.setText(userEnquiryList.get(position).getProduct_name());
        if (type.getImage() != null && !type.getImage().isEmpty() && !type.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + type.getImage())

                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(holder.image);
        }

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                Shared_Preferences.setPrefs(context, "product_name",type.getProduct_name());
                Shared_Preferences.setPrefs(context, "description",type.getDescription());
                Shared_Preferences.setPrefs(context, "price",type.getPrice());
                Shared_Preferences.setPrefs(context, "image", MyConfig.JSON_BusinessImage + type.getImage());
                Shared_Preferences.setPrefs(context, "product_id",type.getId());
                context.startActivity(intent);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return userEnquiryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_userName, txt_purpose, txt_contact,txt_address,txt_product_name,txt_status;
        ImageView image, iv_delete_user;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            txt_userName = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_purpose = (TextView) itemView.findViewById(R.id.txt_purpose);
            txt_contact = (TextView) itemView.findViewById(R.id.txt_contact);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
//            books = (TextView) itemView.findViewById(R.id.books);
            image = (ImageView) itemView.findViewById(R.id.imageViewpro);


        }

    }



}
