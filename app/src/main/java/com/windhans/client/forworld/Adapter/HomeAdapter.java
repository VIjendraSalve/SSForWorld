package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windhans.client.forworld.Activities.ActivityLead;
import com.windhans.client.forworld.Activities.BuisnessHome;
import com.windhans.client.forworld.Activities.ProfileActivity;
import com.windhans.client.forworld.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    Context context;

    View view;
 List<String> stringList;
 List<Integer> stringImageList;
    public HomeAdapter(Context context, List<String> stringList, List<Integer> stringImageList) {
        this.context=context;
        this.stringList=stringList;
        this.stringImageList=stringImageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_buisnessName.setText(stringList.get(position));
        if(stringList.get(position).equals("Product List"))
        {
            int imag=stringImageList.get(0);
            holder.imageView.setImageResource((imag));
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringList.get(position).equals("Product List"))
                {
                    Intent intent=new Intent(context, BuisnessHome.class);
                    context.startActivity(intent);

                }
                if(stringList.get(position).equals("Lead Listing")||stringList.get(position).equals("My Enquiry"))
                {
                    Intent intent=new Intent(context, ActivityLead.class);
                    context.startActivity(intent);

                }
                if(stringList.get(position).equals("Earning"))
                {
                    /*Intent intent=new Intent(context, ActivityEarning.class);
                    context.startActivity(intent);*/
                }
                if(stringList.get(position).equals("Profile"))
                {
                    Intent intent=new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return stringList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView txt_buisnessName;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView=itemView.findViewById(R.id.imageView);
            txt_buisnessName=itemView.findViewById(R.id.txt_buisnessName);



        }

    }

}

