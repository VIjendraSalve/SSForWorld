package com.windhans.client.forworld.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.windhans.client.forworld.R;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder>  {
    Context context;
    ArrayList<String> stringArrayList;
    View view;
    public DetailsAdapter(Context homeFragment, ArrayList<String> stringArrayList) {
        this.context=homeFragment;
        this.stringArrayList=stringArrayList;
    }

    @NonNull
    @Override
    public DetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_items,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_country_name.setText(stringArrayList.get(i));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }







    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout viewForeground;
        TextView txt_country_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_country_name=itemView.findViewById(R.id.txt_country_name);
            viewForeground=itemView.findViewById(R.id.list);
        }
    }


}

