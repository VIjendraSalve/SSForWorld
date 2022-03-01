package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityBuisnessListing;
import com.windhans.client.forworld.Activities.ActivityBusinessDetail;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.Order;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder>{
    Context context;
    List<Order> orderList;
    View view;

    public OrderListAdapter(Context  context, List<Order> orderList) {
        this.context=context;
        this.orderList=orderList;
    }



    @NonNull
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_view, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.MyViewHolder myViewHolder, int position) {
        Order model=orderList.get(position);

        myViewHolder.tv_cust_name.setText(model.getFirst_name()+" "+model.getLast_name());
        myViewHolder.tv_cust_mobile.setText(model.getMobile());
        myViewHolder.tv_product_total_cost.setText(Constants.rs+" "+model.getTotal_amount());
        myViewHolder.tv_amt_from_wallet.setText(Constants.rs+" "+model.getPoints_redeem());
        myViewHolder.tv_remaining_amount.setText(Constants.rs+" "+model.getRemaining_cash());
        myViewHolder.tv_payment_type.setText(model.getPayment_type());




    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Regular tv_cust_name,tv_cust_mobile,tv_product_total_cost,tv_amt_from_wallet,tv_remaining_amount , tv_payment_type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cust_name=itemView.findViewById(R.id.tv_cust_name);
            tv_cust_mobile=itemView.findViewById(R.id.tv_cust_mobile);
            tv_product_total_cost=itemView.findViewById(R.id.tv_product_total_cost);
            tv_amt_from_wallet=itemView.findViewById(R.id.tv_amt_from_wallet);
            tv_remaining_amount=itemView.findViewById(R.id.tv_remaining_amount);
            tv_payment_type=itemView.findViewById(R.id.tv_payment_type);


        }
    }
}
