package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.Activity_Product_Detail;
import com.windhans.client.forworld.Model.LeadDetailModel;
import com.windhans.client.forworld.Model.ProductList;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.MyConfig;

import java.util.List;

public class ProductsLeadListAdapter extends RecyclerView.Adapter<ProductsLeadListAdapter.MyViewHolder> {

    private Context context;

    private ProgressDialog progressDialog;
    List<ProductList> leadDetailModelList;


    public ProductsLeadListAdapter(FragmentActivity activity, List<ProductList> leadDetailModelList) {
        this.context=activity;
        this.leadDetailModelList=leadDetailModelList;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item1, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textview_name.setText(leadDetailModelList.get(position).getProduct_name());
        holder.textViewprice.setText(leadDetailModelList.get(position).getPrice());
       // holder.textview_brand.setText(leadDetailModelList.get(position).getBrand());
        holder.txt_description.setText(leadDetailModelList.get(position).getDescription());
        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        holder.imageViewpro.getLayoutParams().width = (int) (cardwidth / 3);
        holder.imageViewpro.getLayoutParams().height = (int) (cardheight / 5);

        if (leadDetailModelList.get(position).getImage() != null && !leadDetailModelList.get(position).getImage().isEmpty() && !leadDetailModelList.get(position).getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + leadDetailModelList.get(position).getImage())
                    .into(holder.imageViewpro);
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
                intent.putExtra("IS_PRODUCT_IMAGE","null");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leadDetailModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textview_name,textViewprice,textview_brand,txt_description,textView_originalCost;
        ImageView imageViewpro;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            textview_name=itemView.findViewById(R.id.textview_name);
            textViewprice=itemView.findViewById(R.id.textViewprice);
         //   textview_brand=itemView.findViewById(R.id.textview_brand);
            txt_description=itemView.findViewById(R.id.txt_description);
            imageViewpro=itemView.findViewById(R.id.imageViewpro);

        }

    }




}

