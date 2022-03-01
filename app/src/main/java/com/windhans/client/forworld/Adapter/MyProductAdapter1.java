package com.windhans.client.forworld.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ActivityMyProductDetails;
import com.windhans.client.forworld.Activities.ActivityMyProductDetails1;
import com.windhans.client.forworld.Activities.Activity_My_Product;
import com.windhans.client.forworld.Model.VendorProductData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyProductAdapter1 extends RecyclerView.Adapter<MyProductAdapter1.MyViewHolder>{
    Context context;
    List<VendorProductData> vendorProductDataList;
    View view;
    ArrayList<String> stringList=new ArrayList<>();


    public MyProductAdapter1(Activity_My_Product activityMyProduct, List<VendorProductData> vendorProductDataList) {
        this.context=activityMyProduct;
        this.vendorProductDataList=vendorProductDataList;
    }

    @NonNull
    @Override
    public MyProductAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_list_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter1.MyViewHolder myViewHolder, int position) {
        VendorProductData model=vendorProductDataList.get(position);
        myViewHolder.txt_ProductTitle.setText(model.getProduct_name());
        myViewHolder.txt_description.setText(model.getDescription());
        myViewHolder.txt_price.setText(Constants.rs+""+model.getPrice());

        if (model.getImage() != null && !model.getImage().isEmpty() && !model.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + model.getImage())

                    .into(myViewHolder.iv_image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(myViewHolder.iv_image);
        }
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You want to Delete Product");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String product_id=model.getId();
                                String user_id=Shared_Preferences.getPrefs(context,Constants.REG_ID);
                                deleteProduct(product_id,user_id);

                            }
                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);


            }
        });




        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(!stringList.contains(vendorProductDataList.get(position).getId())) {
                        Shared_Preferences.setPrefs(context, "checkBoxClick", String.valueOf(position));
                        Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, vendorProductDataList.get(position).getId());
                        stringList.add(vendorProductDataList.get(position).getId());
                        Intent intent = new Intent("ProductList");
                        intent.putStringArrayListExtra("productIDList", stringList);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }else {
                    stringList.remove(vendorProductDataList.get(position).getId());
                    Intent intent=new Intent("ProductList");
                    intent.putStringArrayListExtra("productIDList",stringList);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
       myViewHolder.imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ActivityMyProductDetails.class);
                context.startActivity(intent);
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID,model.getBusiness_id());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_NAME,model.getProduct_name());
                Shared_Preferences.setPrefs(context,Constants.BRAND,model.getBrand());
                Shared_Preferences.setPrefs(context,Constants.DESCRIPTION,model.getDescription());
                Shared_Preferences.setPrefs(context,Constants.PRICE,model.getPrice());
                Shared_Preferences.setPrefs(context,Constants.SUB_ID,model.getSubcategory_id());
                Log.d("sub", "onClick: "+model.getSubcategory_id());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_IMAGE,model.getImage());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_ID,model.getId());
            }
        });
        myViewHolder.ll_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               if(!myViewHolder.checkBox.isChecked()){

                   myViewHolder.checkBox.setChecked(true);
                   if(!stringList.contains(vendorProductDataList.get(position).getId())) {
                       Shared_Preferences.setPrefs(context, "checkBoxClick", String.valueOf(position));
                       Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, vendorProductDataList.get(position).getId());
                       stringList.add(vendorProductDataList.get(position).getId());
                       Intent intent = new Intent("ProductList");
                       intent.putStringArrayListExtra("productIDList", stringList);
                       LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                   }
               }else {
                   myViewHolder.checkBox.setChecked(false);
                   stringList.remove(vendorProductDataList.get(position).getId());
                   Intent intent=new Intent("ProductList");
                   intent.putStringArrayListExtra("productIDList",stringList);
                   LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
               }
            }
        });
    }

    private void deleteProduct(String product_id, String user_id) {
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);

        Call<ResponseBody> responseBodyCall=getOrderApi.deleteProduct(product_id,user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    boolean res=jsonObject.getBoolean("result");
                    String reason=jsonObject.getString("reason");
                    if(res)
                    {

                        Toast.makeText(context,reason , Toast.LENGTH_SHORT).show();
                   /*     Intent intent=new Intent(context,ActivityMyProduct.class);
                        context.startActivity(intent);*/
                    }
                    else {
                        Toast.makeText(context,reason , Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/deleteProduct")
        Call<ResponseBody> deleteProduct(
                @Field("product_id") String product_id,
                @Field("user_id") String user_id
        );
    }
    @Override
    public int getItemCount() {
        return vendorProductDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ProductTitle,txt_description,txt_price;
        ImageView iv_image,delete,imageView_edit;
        CheckBox checkBox;
       CardView cardview;
       LinearLayout ll_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductTitle=itemView.findViewById(R.id.txt_ProductTitle);
            txt_description=itemView.findViewById(R.id.txt_description);
            txt_price=itemView.findViewById(R.id.txt_price);
            checkBox=itemView.findViewById(R.id.checkBox);
            cardview=itemView.findViewById(R.id.cardview);
            ll_layout=itemView.findViewById(R.id.ll_layout);

            iv_image=itemView.findViewById(R.id.iv_image);
            imageView_edit=itemView.findViewById(R.id.imageView_edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}

