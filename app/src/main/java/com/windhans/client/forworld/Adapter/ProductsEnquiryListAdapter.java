package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.Activity_Product_Detail;
import com.windhans.client.forworld.Model.EnquiryDetailModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;

import java.util.List;

public class ProductsEnquiryListAdapter extends RecyclerView.Adapter<ProductsEnquiryListAdapter.MyViewHolder> {

    private Context context;

    private ProgressDialog progressDialog;
    List<EnquiryDetailModel> leadDetailModelList;
    private String isProduct="";

    public ProductsEnquiryListAdapter(Context activity, List<EnquiryDetailModel> leadDetailModelList, String isProduct ) {
        this.context=activity;
        this.leadDetailModelList=leadDetailModelList;
        this.isProduct = isProduct;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item1, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textview_name.setText(leadDetailModelList.get(position).getProduct_name());
        if(leadDetailModelList.get(position).getPrice() == null){
            holder.textViewprice.setVisibility(View.GONE);
        }else {
            holder.textViewprice.setVisibility(View.VISIBLE);
            holder.textViewprice.setText(Constants.rs + " " + leadDetailModelList.get(position).getPrice());
        }

        if(leadDetailModelList.get(position).getQuantity() == null){
            holder.textViewQuantity.setVisibility(View.GONE);
        }else {
            holder.textViewQuantity.setVisibility(View.VISIBLE);
            holder.textViewQuantity.setText("Quantity : " + leadDetailModelList.get(position).getQuantity());
        }

        holder.textview_brand.setText(leadDetailModelList.get(position).getBrand());
        holder.txt_description.setText(leadDetailModelList.get(position).getDescription());

        Log.d("image", "response: "+MyConfig.JSON_BusinessImage + leadDetailModelList.get(position).getImage());


        // isProduct == 1 Service
        // isProduct == 0 Product

        if (leadDetailModelList.get(position).getImage() != null && !leadDetailModelList.get(position).getImage().isEmpty() && !leadDetailModelList.get(position).getImage().equals("null")) {

            if(isProduct.equals("0")) {
                Glide.with(context)
                        .load(MyConfig.JSON_BusinessImage + leadDetailModelList.get(position).getImage())
                        .into(holder.imageViewpro);
            }else {
                Glide.with(context)
                        .load(MyConfig.JSON_ServicePath + leadDetailModelList.get(position).getImage())
                        .into(holder.imageViewpro);
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(holder.imageViewpro);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Activity_Product_Detail.class);
                intent.putExtra("Product_Name",leadDetailModelList.get(position).getProduct_name());
                intent.putExtra("Brand_name",leadDetailModelList.get(position).getBrand());
                intent.putExtra("Price",leadDetailModelList.get(position).getPrice());
                intent.putExtra("Description",leadDetailModelList.get(position).getDescription());
                intent.putExtra("image",leadDetailModelList.get(position).getImage());
                intent.putExtra("IS_PRODUCT_IMAGE",isProduct); // 1 service
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leadDetailModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyTextView_Poppins_Regular textview_brand,txt_description, textViewQuantity;
        MyTextView_Poppins_Bold textview_name,textViewprice;
        ImageView imageViewpro;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            textview_name=itemView.findViewById(R.id.textview_name);
            textViewprice=itemView.findViewById(R.id.textViewprice);
            textview_brand=itemView.findViewById(R.id.textview_brand);
            txt_description=itemView.findViewById(R.id.txt_description);
            textViewQuantity=itemView.findViewById(R.id.textViewQuantity);
            imageViewpro=itemView.findViewById(R.id.imageViewpro);

        }

    }




}

