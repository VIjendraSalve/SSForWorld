package com.windhans.client.forworld.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windhans.client.forworld.Activities.ActivityAdd;
import com.windhans.client.forworld.Model.ModelAdd;
import com.windhans.client.forworld.R;

import java.util.List;

public class AddAdpter extends RecyclerView.Adapter<AddAdpter.MyViewHolder> {
    Context context;
    List<ModelAdd> modelAddList;
    View view;


    public AddAdpter(ActivityAdd activityAdd, List<ModelAdd> addList) {
        this.context = activityAdd;
        this.modelAddList = addList;
    }


    @NonNull
    @Override
    public AddAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_items1, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddAdpter.MyViewHolder myViewHolder, int position) {

        myViewHolder.txt_startDate.setText(modelAddList.get(position).getStartDate());
        myViewHolder.txt_end_date.setText(modelAddList.get(position).getEndDate());
        myViewHolder.txt_total.setText(modelAddList.get(position).getTotal());
        myViewHolder.txt_remaining.setText(modelAddList.get(position).getRemaining());
        myViewHolder.txt_status.setText(modelAddList.get(position).getStatus());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelAddList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_startDate, txt_end_date, txt_total, txt_remaining, txt_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_startDate = itemView.findViewById(R.id.txt_startDate);
            txt_end_date = itemView.findViewById(R.id.txt_end_date);
            txt_total = itemView.findViewById(R.id.txt_total);
            txt_remaining = itemView.findViewById(R.id.txt_remaining);
            txt_status = itemView.findViewById(R.id.txt_status);

        }
    }


}
