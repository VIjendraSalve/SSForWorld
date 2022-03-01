package com.windhans.client.forworld.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.Activity_My_Cart;
import com.windhans.client.forworld.Model.MyCartModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {
    Context context;

    View view;
    List<MyCartModel> myCartModelList;
    AdapterCallback adapterCallback;
    private ProgressDialog progressDialog;

    public MyCartAdapter(Activity_My_Cart activity_my_cart, List<MyCartModel> myCartModelList) {
        this.context = activity_my_cart;
        this.myCartModelList = myCartModelList;
        adapterCallback = ((AdapterCallback) context);
    }


    @Override
    public MyCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

     //   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_cart, parent, false);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_cart1, parent, false);
        MyCartAdapter.MyViewHolder holder = new MyCartAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final MyCartAdapter.MyViewHolder holder, final int position) {

        holder.txt_product_name.setText(myCartModelList.get(position).getProduct_name());
        holder.txt_product_price.setText(Constants.rs+" "+myCartModelList.get(position).getSelling_price());
        holder.txt_product_desc.setText(myCartModelList.get(position).getDescription());
        holder.txt_product_original_cost.setText(Constants.rs+" "+myCartModelList.get(position).getOriginal_cost());
        holder.edt_product_quantity.setText(myCartModelList.get(position).getQuantity());


        holder.txt_product_original_cost.setPaintFlags(holder.txt_product_original_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
       /* DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        Log.d("height", "onBindViewHolder: "+cardwidth+","+cardheight);
        holder.image_view.getLayoutParams().width = (int) (cardwidth / 2.3);
        holder.image_view.getLayoutParams().height = (int) (cardheight / 5);*/
        //   holder.txt_product_name.setText(myCartModelList.get(position).getProduct_name());
        if (myCartModelList.get(position).getImage() != null && !myCartModelList.get(position).getProduct_name().isEmpty() && !myCartModelList.get(position).getProduct_name().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + myCartModelList.get(position).getImage())
                    .into(holder.image_view);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(holder.image_view);
        }
        String user_id = Shared_Preferences.getPrefs(context, Constants.REG_ID);
        String product_id = myCartModelList.get(position).getProduct_id();

        holder.btn_removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You want to delete product from cart");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                removeCartItem(user_id, product_id);

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



        holder.ll_increment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(holder.edt_product_quantity.getText().toString()) >= 1){
                    int quantity = Integer.parseInt(holder.edt_product_quantity.getText().toString()) + 1;
                    updateCartItem(user_id, myCartModelList.get(position).getCart_id(),String.valueOf(quantity), position);
                }
            }
        });

        holder.ll_decrement_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(holder.edt_product_quantity.getText().toString()) > 1){
                    int quantity = Integer.parseInt(holder.edt_product_quantity.getText().toString()) - 1;
                    updateCartItem(user_id, myCartModelList.get(position).getCart_id(),String.valueOf(quantity), position);
                }else {
                    Toast.makeText(context, "Product Cant Be less than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void removeCartItem(String user_id, String product_id) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.removeFromCart(user_id, product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    boolean result=jsonObject.getBoolean("result");
                    String message=jsonObject.getString("reason");
                    if(result)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        adapterCallback.onMethodCallback();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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

    @Override
    public int getItemCount() {
        return myCartModelList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image_view;
        EditText edt_product_quantity;
        LinearLayout  ll_increment_count, ll_decrement_count;
        MyTextView_Poppins_Regular  txt_offer, txt_product_desc, txt_product_original_cost;
        ImageView btn_removeFromCart;
        MyTextView_Poppins_Bold txt_product_name, txt_product_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            image_view = itemView.findViewById(R.id.image_view);
            btn_removeFromCart = itemView.findViewById(R.id.btn_removeFromCart);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_product_price = itemView.findViewById(R.id.txt_product_price);
            txt_product_desc = itemView.findViewById(R.id.txt_product_desc);
            txt_product_original_cost = itemView.findViewById(R.id.txt_product_original_cost);
            txt_offer = itemView.findViewById(R.id.txt_offer);
            edt_product_quantity = itemView.findViewById(R.id.edt_product_quantity);
            ll_increment_count = itemView.findViewById(R.id.ll_increment_count);
            ll_decrement_count = itemView.findViewById(R.id.ll_decrement_count);


        }

    }

    private void updateCartItem(String user_id, String cartID, String quantity, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("UpdateCart", "updateCartItem: "+user_id+" "+cartID+" "+quantity);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.updateFromCart(
                user_id,
                cartID,
                quantity
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("UpdateCart", "onResponse: "+output);
                    JSONObject jsonObject=new JSONObject(output);
                    boolean result=jsonObject.getBoolean("result");
                    String message=jsonObject.getString("reason");
                    if(result)
                    {
                        progressDialog.dismiss();
                        myCartModelList.get(position).setQuantity(String.valueOf(quantity));
                        notifyDataSetChanged();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        adapterCallback.onMethodCallback();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/removeCartItem")
        Call<ResponseBody> removeFromCart(
                @Field("user_id") String user_id,
                @Field("product_id") String product_id);

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/updateItemToCart")
        Call<ResponseBody> updateFromCart(
                @Field("user_id") String user_id,
                @Field("cart_id") String cart_id,
                @Field("quantity") String quantity
        );
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }

}
