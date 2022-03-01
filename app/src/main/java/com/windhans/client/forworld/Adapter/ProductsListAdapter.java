package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.ProductDetailsActivity;
import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
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

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NewProduct> productList;
    private ProgressDialog progressDialog;
    int flag = 1;

    public ProductsListAdapter(ArrayList<NewProduct> productList) {
        this.productList = productList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_list_only, parent, false);
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item1, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NewProduct newProduct = productList.get(position);


        holder.txt_product_name.setText(newProduct.getName());
        holder.txt_productDetails.setText(newProduct.getDescription());
        // holder.textViewdesc.setText(productList.get(position).getDescription());
        //holder.txt_price.setText(newProduct.getSelling_price() + "/-");
        //holder.txt_location.setText(productList.get(position).getAddress());
        //holder.txt_buisnessNAme.setText(productList.get(position).getBusiness_name());
        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        holder.image.getLayoutParams().width = (int) (cardwidth / 2.8);
        holder.image.getLayoutParams().height = (int) (cardheight / 5);

        Log.d("image", "onBindViewHolder: " + MyConfig.JSON_BusinessImage + newProduct.getProduct_image());
        if (newProduct.getProduct_image() != null && !newProduct.getProduct_image().isEmpty() && !newProduct.getProduct_image().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + newProduct.getProduct_image())
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(holder.image);
        }
        String is_fav = productList.get(position).getIs_favourite();
        Log.d("favorite", "onBindViewHolder: " + is_fav);
        Log.d("favorite", "onBindViewHolder: " + productList.get(position).getName());
      /*  holder.image_like.getLayoutParams().width = (int) (cardwidth / 7);
        holder.image_like.getLayoutParams().height = (int) (cardheight / 9);*/

        if (is_fav != null) {
            if (is_fav.equals("1")) {

                holder.image_like.setVisibility(View.VISIBLE);
                holder.image_unlike.setVisibility(View.GONE);

            } else {

                holder.image_like.setVisibility(View.GONE);
                holder.image_unlike.setVisibility(View.VISIBLE);

            }
        } else {
            holder.image_like.setVisibility(View.GONE);
            holder.image_unlike.setVisibility(View.VISIBLE);
        }


        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_like.setVisibility(View.GONE);
                holder.image_unlike.setVisibility(View.VISIBLE);
                flag = 0;
                String product_id = "";
                for (int i = 0; i < productList.size(); i++) {
                    String product_name = newProduct.getName();
                    String product_name1 = productList.get(i).getName();
                              /*  String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*/
                    Log.d("name", "onClick: " + product_name + "," + product_name1);
                    if (product_name.equals(product_name1)) {
                        product_id = productList.get(i).getId();
                        Log.d("product_id", "onClick: " + product_id);
                    }
                }
                sendData(product_id);
              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (flag == 1) {
                     //  holder.image_like.setImageDrawable(context.getDrawable(R.drawable.like_new));
                        holder.image_like.setVisibility(View.GONE);
                        holder.image_unlike.setVisibility(View.VISIBLE);
                        flag = 0;
                        String product_id = "";
                        for (int i = 0; i < productList.size(); i++) {
                            String product_name = newProduct.getProduct_name();
                            String product_name1 = productList.get(i).getProduct_name();
                              *//*  String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            Log.d("name", "onClick: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList.get(i).getId();
                                Log.d("product_id", "onClick: " + product_id);
                            }
                        }
                        sendData(product_id);

                    } else {
                        //holder.image_like.setImageDrawable(context.getDrawable(R.drawable.dislike_new_logo));
                        holder.image_like.setVisibility(View.VISIBLE);
                        holder.image_unlike.setVisibility(View.GONE);
                        flag = 1;
                        String product_id = "";

                        for (int i = 0; i < productList.size(); i++) {
                                *//*String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            String product_name = newProduct.getProduct_name();
                            String product_name1 = productList.get(i).getProduct_name();
                            Log.d("name", "onClick1: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList.get(i).getId();

                            }
                        }
                        sendData(product_id);
                    }*/

                // }
            }
        });
        holder.image_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_like.setVisibility(View.VISIBLE);
                holder.image_unlike.setVisibility(View.GONE);
                flag = 1;
                String product_id = "";

                for (int i = 0; i < productList.size(); i++) {
                                /*String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*/
                    String product_name = newProduct.getName();
                    String product_name1 = productList.get(i).getName();
                    Log.d("name", "onClick1: " + product_name + "," + product_name1);
                    if (product_name.equals(product_name1)) {
                        product_id = productList.get(i).getId();

                    }
                }
                sendData(product_id);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (flag == 1) {
                        //  holder.image_like.setImageDrawable(context.getDrawable(R.drawable.like_new));
                        holder.image_like.setVisibility(View.GONE);
                        holder.image_unlike.setVisibility(View.VISIBLE);
                        flag = 0;
                        String product_id = "";
                        for (int i = 0; i < productList.size(); i++) {
                            String product_name = newProduct.getProduct_name();
                            String product_name1 = productList.get(i).getProduct_name();
                              *//*  String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            Log.d("name", "onClick: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList.get(i).getId();
                                Log.d("product_id", "onClick: " + product_id);
                            }
                        }
                        sendData(product_id);

                    } else {
                        //holder.image_like.setImageDrawable(context.getDrawable(R.drawable.dislike_new_logo));
                        holder.image_like.setVisibility(View.VISIBLE);
                        holder.image_unlike.setVisibility(View.GONE);
                        flag = 1;
                        String product_id = "";

                        for (int i = 0; i < productList.size(); i++) {
                                *//*String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            String product_name = newProduct.getProduct_name();
                            String product_name1 = productList.get(i).getProduct_name();
                            Log.d("name", "onClick1: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList.get(i).getId();

                            }
                        }
                        sendData(product_id);
                    }

                }*/
            }
        });

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+Shared_Preferences.getPrefs(context, Constants.BUSINESS_IDFORCART)
                        +"   "+productList.get(position).getId(), Toast.LENGTH_SHORT).show();

                addInToCart(Shared_Preferences.getPrefs(context, Constants.BUSINESS_IDFORCART),productList.get(position).getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putParcelableArrayListExtra(Constants.ProductList, productList);
                intent.putExtra(Constants.Position, position);
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, productList.get(position).getProduct_image());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, productList.get(position).getId());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME, productList.get(position).getName());
                Shared_Preferences.setPrefs(context, Constants.BRAND, productList.get(position).getBrand());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_DEC, productList.get(position).getDescription());
                Shared_Preferences.setPrefs(context, Constants.OFFER_PRICE, productList.get(position).getOffer_price());
                Shared_Preferences.setPrefs(context, Constants.IS_FAVORITE, productList.get(position).getIs_favourite());
                context.startActivity(intent);
               /* Shared_Preferences.setPrefs(context, "product_name",newProduct.getProduct_name());
                Shared_Preferences.setPrefs(context, "description",newProduct.getDescription());
                Log.d("Image", "onClick: "+newProduct.getDescription());
                Shared_Preferences.setPrefs(context, "price",newProduct.getPrice());
                Log.d("Image", "onClick: "+newProduct.getBusiness_banner());*/
                // Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, MyConfig.JSON_BusinessImage +newProduct.getImage());
               /* Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, newProduct.getImage());

                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID,productList.get(position).getId());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_ID,productList.get(position).getBusiness_id());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_EMAIL,productList.get(position).getEmail());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_NAME,productList.get(position).getBusiness_name());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_DEC,productList.get(position).getBuisness_description());
                Shared_Preferences.setPrefs(context,Constants.CONTACT_NAME,productList.get(position).getContact_name());
                Shared_Preferences.setPrefs(context,Constants.CONTACT_NO,productList.get(position).getContact_mobile());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_CONTACT,productList.get(position).getContact_mobile());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_ADDRESS,productList.get(position).getAddress());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_CITY,productList.get(position).getCity());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_DISTRICT,productList.get(position).getDistrict());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_PINCODE,productList.get(position).getPincode());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_NAME,productList.get(position).getProduct_name());
                Shared_Preferences.setPrefs(context,Constants.BRAND,productList.get(position).getBrand());
                Shared_Preferences.setPrefs(context,Constants.PRODUCT_DEC,productList.get(position).getDescription());
                Shared_Preferences.setPrefs(context,Constants.BUISNESS_BANNER,productList.get(position).getBusiness_banner());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_ADDRESS,productList.get(position).getAddress());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_CITY,productList.get(position).getCity());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_DISTRICT,productList.get(position).getDistrict());
                Shared_Preferences.setPrefs(context,Constants.BUSINESS_PINCODE,productList.get(position).getPincode());



                Shared_Preferences.setPrefs(context,Constants.OfferID,productList.get(position).getOffer_id());
                Shared_Preferences.setPrefs(context,Constants.OFFER_NAME,productList.get(position).getOffer_name());
                Shared_Preferences.setPrefs(context,Constants.OFFER_VALIDITY,productList.get(position).getValidity());
                Shared_Preferences.setPrefs(context,Constants.OFFER_DETAILS,productList.get(position).getOffer_description());
                Shared_Preferences.setPrefs(context,Constants.OFFER_TYPE,productList.get(position).getOffer_newProduct());
                Shared_Preferences.setPrefs(context,Constants.OFFER_CODE,productList.get(position).getOffer_code());
                Shared_Preferences.setPrefs(context,Constants.OFFER_PRICE,productList.get(position).getOffer_price());
                Shared_Preferences.setPrefs(context,Constants.OFFER_TERM,productList.get(position).getOffer_term());
                Shared_Preferences.setPrefs(context,Constants.IS_FAVORITE,productList.get(position).getIsFavorite());

                Log.d("buisnessID", "onClick: "+productList.get(position).getBusiness_id());
                context.startActivity(intent);*/

            }
        });

    }

    private void sendData(String product_id) {
        String user_id = Shared_Preferences.getPrefs(context, Constants.REG_ID);
        Log.d("id", "sendData: " + user_id + "," + product_id);
        UserDashboardAdapter.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(UserDashboardAdapter.GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.sendData(user_id, product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = "";
                    output = response.body().string();
                    Log.d("LikeUnlike", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        String reason = jsonObject.getString("reason");
                        if (result) {
                            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        /*  TextView textViewtitle, textViewdesc, textViewprice,txt_buisnessNAme,txt_offerName;
          ImageView image, iv_delete_user;*/
        MyTextView_Poppins_Bold txt_product_name;
        MyTextView_Poppins_Regular txt_productDetails;
        TextView txt_location, txt_price;
        ImageView image, image_like, image_unlike;
        Button btn_add_to_cart;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            txt_product_name = (MyTextView_Poppins_Bold) itemView.findViewById(R.id.txt_product_name);
            txt_productDetails = (MyTextView_Poppins_Regular) itemView.findViewById(R.id.txt_productDetails);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            /*txt_buisnessNAme = (TextView) itemView.findViewById(R.id.txt_buisnessNAme);*/
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
//          books = (TextView) itemView.findViewById(R.id.books);
            image = (ImageView) itemView.findViewById(R.id.image);
            image_like = (ImageView) itemView.findViewById(R.id.image_like);
            image_unlike = (ImageView) itemView.findViewById(R.id.image_unlike);
            btn_add_to_cart = (Button) itemView.findViewById(R.id.btn_add_to_cart);
            //iv_delete_user = (ImageView) itemView.findViewById(R.id.iv_delete_user);

        }

    }

    private void DeleteTYPE(String product_id, final int posotion) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


        CRUDAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CRUDAPI.class);
        Call<ResponseBody> result = api.checkTYPE(product_id,
                Shared_Preferences.getPrefs(context, Constants.REG_ID));
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    String reason = jsonObject.getString("reason");

                    if (res) {
                        productList.remove(posotion);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d("Retrofit Error:",t.getMessage());
                //progressDialog.dismiss();
            }
        });
    }

    public interface CRUDAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/deleteProduct")
        Call<ResponseBody> checkTYPE(
                @Field("product_id") String product_id,
                @Field("user_id") String user_id

        );/*product_id
user_id*/
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addItemToCart")
        public Call<ResponseBody> addToCart(
                @Field("business_id") String business_id,
                @Field("product_id") String product_id,
                @Field("user_id") String user_id
        );
    }

    private void addInToCart(String business_id, String product_id) {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.addToCart(
                business_id,
                product_id,
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

