package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.windhans.client.forworld.Activities.ActivityProductList;
import com.windhans.client.forworld.Model.ProductDataForBill;
import com.windhans.client.forworld.Model.SubCategory;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.ArrayList;
import java.util.List;

public class ProductBillAdapter extends RecyclerView.Adapter<ProductBillAdapter.MyViewHolder> {
    Context context;
    View view;
  ArrayList<ProductDataForBill> productDataForBillArrayList;



    public ProductBillAdapter(Context context, ArrayList<ProductDataForBill> productDataForBillArrayList) {
        this.context=context;
        this.productDataForBillArrayList=productDataForBillArrayList;
    }

    @Override
    public ProductBillAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        ProductBillAdapter.MyViewHolder holder = new ProductBillAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final ProductBillAdapter.MyViewHolder holder, final int position) {
        ProductDataForBill productDataForBill = productDataForBillArrayList.get(position);

        holder.tv_sr_no.setText(productDataForBillArrayList.get(position).getSrno());
        holder.tv_product_name.setText(productDataForBillArrayList.get(position).getProduct_name());
        holder.tv_quantity.setText(productDataForBillArrayList.get(position).getQuantity());
        holder.tv_amount.setText(productDataForBillArrayList.get(position).getTotal());


    }

    @Override
    public int getItemCount() {
        return productDataForBillArrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyTextView_Poppins_Regular tv_sr_no, tv_product_name, tv_quantity, tv_amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tv_sr_no=itemView.findViewById(R.id.tv_sr_no);
            tv_product_name=itemView.findViewById(R.id.tv_product_name);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
            tv_amount=itemView.findViewById(R.id.tv_amount);



        }

    }

}


