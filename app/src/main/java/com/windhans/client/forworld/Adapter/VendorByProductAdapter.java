package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.Activities.ActivityBusinessDetail;
import com.windhans.client.forworld.Model.VendorByProduct;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class VendorByProductAdapter extends RecyclerView.Adapter<VendorByProductAdapter.MyViewHolder> {
    Context context;
    View view;
    private ArrayList<VendorByProduct> vendorByProductList;

    
    public VendorByProductAdapter(Context context, ArrayList<VendorByProduct> vendorByProductList) {
        this.context = context;
        this.vendorByProductList = vendorByProductList;

    }


    @Override
    public VendorByProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_as_per_product, parent, false);
        VendorByProductAdapter.MyViewHolder holder = new VendorByProductAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(VendorByProductAdapter.MyViewHolder holder, final int position) {
        VendorByProduct vendorByProduct = vendorByProductList.get(position);
        Log.d("AdapterNew", "onBindViewHolder: "+vendorByProductList.size());
        holder.tv_vendor_name.setText(vendorByProductList.get(position).getBusiness_name());
        holder.tv_vendor_location.setText(vendorByProductList.get(position).getAddress());

        if(vendorByProductList.get(position).getOffername() != null) {
            holder.ll_offer.setVisibility(View.VISIBLE);
            holder.tv_offer_name.setText(vendorByProductList.get(position).getOffername());
            holder.tv_offer_code.setText(vendorByProductList.get(position).getOfferoffer_code());
            holder.tv_offer_validity.setText(DateTimeFormat.getDateNew(vendorByProductList.get(position).getOffervalidity()));
            holder.tv_offer_redemption_count.setText(vendorByProductList.get(position).getOfferoffer_limit());
        }else {
            holder.ll_offer.setVisibility(View.GONE);
        }


        holder.tv_product_selling_price.setText(Constants.rs+" "+vendorByProductList.get(position).getSelling_price() + "/-");
        holder.tv_product_original_price.setText(Constants.rs+" "+vendorByProductList.get(position).getOriginal_cost() + "/-");
        holder.tv_product_original_price.setPaintFlags( holder.tv_product_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBusinessDetail.class);
                intent.putParcelableArrayListExtra(Constants.VendorDetailList, vendorByProductList);
                intent.putExtra(Constants.PositionOfVendor, position);
                context.startActivity(intent);
            }
        });

        holder.btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon..!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ids", "onClick: "
                        +vendorByProductList.get(position).getUser_id()
                        +","+vendorByProductList.get(position).getProdutId());

                addInToCart(
                        vendorByProductList.get(position).getUser_id(),
                        vendorByProductList.get(position).getProdutId(),
                        vendorByProductList.get(position).getSelling_price(),
                        vendorByProductList.get(position).getOfferid()
                        );
            }
        });

    }




    @Override
    public int getItemCount() {
        return vendorByProductList.size();

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyTextView_Poppins_Bold tv_vendor_name,  tv_product_selling_price ;
        MyTextView_Poppins_Regular tv_vendor_location, tv_product_original_price, tv_offer_name, tv_offer_code
                ,tv_offer_validity, tv_offer_redemption_count;
        Button btn_buy_now, btn_add_to_cart;
        LinearLayout ll_offer;

        public MyViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            tv_vendor_name = itemView.findViewById(R.id.tv_vendor_name);
            tv_vendor_location = itemView.findViewById(R.id.tv_vendor_location);
            tv_product_selling_price = itemView.findViewById(R.id.tv_product_selling_price);
            tv_product_original_price = itemView.findViewById(R.id.tv_product_original_price);
            btn_buy_now = itemView.findViewById(R.id.btn_buy_now);
            btn_add_to_cart = itemView.findViewById(R.id.btn_add_to_cart);
            tv_offer_name = itemView.findViewById(R.id.tv_offer_name);
            tv_offer_code = itemView.findViewById(R.id.tv_offer_code);
            tv_offer_validity = itemView.findViewById(R.id.tv_offer_validity);
            tv_offer_redemption_count = itemView.findViewById(R.id.tv_offer_redemption_count);
            ll_offer = itemView.findViewById(R.id.ll_offer);



        }

    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addItemToCart")
        public Call<ResponseBody> addToCart(
                @Field("business_id") String business_id,
                @Field("product_id") String product_id,
                @Field("selling_price") String selling_price,
                @Field("offer_id") String offer_id,
                @Field("user_id") String user_id
        );
    }

    private void addInToCart(String business_id, String product_id, String Selling_price, String offerid) {

        Log.d("OffferPrice", "addInToCart: "+Selling_price);
        Log.d("OffferPrice", "addInToCart: "+offerid);

        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.addToCart(
                business_id,
                product_id,
                Selling_price,
                offerid,
                Shared_Preferences.getPrefs(context, Constants.REG_ID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("AddToCart", "onResponse: "+output);
                    try {
                        JSONObject object = new JSONObject(output);
                        boolean result = object.getBoolean("result");
                        String resaon = object.getString("reason");
                        if (result) {
                            Toast.makeText(context, "Product Added Into Cart Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, resaon, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}

