package com.windhans.client.forworld.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.windhans.client.forworld.Activities.GenrateEnquiry;

import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.R;
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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    Context context;
    View view;
    RecyclerView recycler_view;
    List<NewProduct> productList1;
    int flag = 1;
    private String IMAGE_URL = "";
    private int STORAGE_PERMISSION_CODE=23;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE=2;

    public ProductListAdapter(Context context, List<NewProduct> dashboardList, RecyclerView recycler_view) {
        this.context = context;
        this.productList1 = dashboardList;
        this.recycler_view = recycler_view;
    }



    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gridlayout1, parent, false);
      //  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid_user_layout, parent, false);
        ProductListAdapter.MyViewHolder holder = new ProductListAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductListAdapter.MyViewHolder holder, final int position) {
        String[] mPermission = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        Log.d("height", "onBindViewHolder: "+cardwidth+","+cardheight);
        holder.image.getLayoutParams().width = (int) (cardwidth / 2.3);
        holder.image.getLayoutParams().height = (int) (cardheight / 5);
        int pos = holder.getAdapterPosition();
        NewProduct productList = productList1.get(pos);
        holder.txt_vendor_name.setVisibility(View.VISIBLE);
            holder.txt_location.setVisibility(View.VISIBLE);
            holder.txt_price.setVisibility(View.VISIBLE);
            holder.txt_product_name.setText(productList.getName());
            holder.txt_vendor_name.setText(productList.getName());
           // holder.txt_location.setText(productList.getAddress());
            holder.txt_price.setText(productList.getSelling_price());
        String url=MyConfig.JSON_BusinessImage + productList.getProduct_image();
       // getDropboxIMGSize(Uri.parse(url));
            if (productList.getProduct_image() != null && !productList.getProduct_image().isEmpty() && !productList.getProduct_image().equals("null")) {
                Log.d("image", "response: ");

                Glide.with(context)
                        .load(MyConfig.JSON_BusinessImage + productList.getProduct_image())

                        .into(holder.image);
            } else {
                Glide.with(context)
                        .load(R.drawable.no_image_available)

                        .into(holder.image);
            }


            String is_fav = productList1.get(position).getIs_favourite();
            Log.d("favorite", "onBindViewHolder: " + is_fav);

            if (is_fav.equals("1")) {

                    holder.image_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));

            } else {

                    holder.image_like.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));

            }




       /* holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (flag == 1) {
                        holder.image_like.setImageDrawable(context.getDrawable(R.drawable.like_white));
                        flag = 0;
                        String product_id = "";
                        for (int i = 0; i < productList1.size(); i++) {
                            String product_name = productList.getProduct_name();
                            String product_name1 = productList1.get(i).getProduct_name();
                              *//*  String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            Log.d("name", "onClick: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList1.get(i).getId();
                                Log.d("product_id", "onClick: " + product_id);
                            }
                        }
                        sendData(product_id);

                    } else {
                        holder.image_like.setImageDrawable(context.getDrawable(R.drawable.dislike_48));
                        flag = 1;
                        String product_id = "";

                        for (int i = 0; i < productList1.size(); i++) {
                                *//*String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*//*
                            String product_name = productList.getProduct_name();
                            String product_name1 = productList1.get(i).getProduct_name();
                            Log.d("name", "onClick1: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList1.get(i).getId();

                            }
                        }
                        sendData(product_id);
                    }

                }
            }
        });*/

        holder.image_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (flag == 1) {
                        holder.image_unlike.setImageDrawable(context.getDrawable(R.drawable.like_white));
                        flag = 0;
                        String product_id = Shared_Preferences.getPrefs(context,Constants.PRODUCT_ID);
                        sendData(product_id);

                    } else {
                        holder.image_unlike.setImageDrawable(context.getDrawable(R.drawable.dislike_48));
                        flag = 1;
                        String product_id = Shared_Preferences.getPrefs(context,Constants.PRODUCT_ID);
                        sendData(product_id);
                    }

                }
            }
        });
        holder.btn_send_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenrateEnquiry.class);
                context.startActivity(intent);
            }
        });

        holder.btn_call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    // callPhone();
                    String number = Shared_Preferences.getPrefs(context, Constants.CONTACT_NO);
                    Log.d("contact_number", "onClick: " + number);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    context.startActivity(callIntent);
                }

            }
        });
       /* holder.image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.image_share);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.share_product, popup.getMenu());
                MenuItem item = popup.getMenu().findItem(R.id.shareProduct);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permission is not granted
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        STORAGE_PERMISSION_CODE);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        } else {
                            // Permission has already been granted
                            Log.d("title", "onMenuItemClick: " + item.getTitle());
                            IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + productList.getImage();
                            Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                            Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                            String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);
                            //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                            if (imgBitmapPath != null) {
                                Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                                String text = "ForWorld" + "\n\n" + "Proudct : " + productList.getProduct_name() + "\n\n" + "Desc : " + productList.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + productList.getBusiness_id()
                                        + "/" + productList.getPrice() + "/" + productList.getSubcategory_id() + "/" + productList.getId();
                                //Uri pictureUri = Uri.parse(MyConfig.JSON_PRODUCTS_URL+products.getProduct_banner_image());
                                Intent shareIntent = new Intent();
                                shareIntent.setType("image/*");
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(Intent.createChooser(shareIntent, "Share images..."));
                            } else {
                                Toast.makeText(context, "Image is downloading Please wait...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
              /*  Intent intent=new Intent(context, ActivitySubCategory.class);
                Shared_Preferences.setPrefs(context, Constants.CATEGORY_ID,categoryModel.getCategory_id());
                context.startActivity(intent);*/

                /*Log.d("Vijendra", "onClick: "+productList.getContact_mobile());

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, productList.getImage());

                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, productList.getId());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID, productList.getBusiness_id());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_NAME, productList.getBusiness_name());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_EMAIL, productList.getEmail());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DEC, productList.getBuisness_description());
                Shared_Preferences.setPrefs(context, Constants.CONTACT_NAME, productList.getContact_name());
                Shared_Preferences.setPrefs(context, Constants.CONTACT_NO, productList.getContact_mobile());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CONTACT, productList.getContact_mobile());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME, productList.getProduct_name());
                Shared_Preferences.setPrefs(context, Constants.BRAND, productList.getBrand());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_DEC, productList.getDescription());
                Shared_Preferences.setPrefs(context, Constants.BUISNESS_BANNER, productList.getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_IMAGE, productList.getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CITY,productList.getCity());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DISTRICT,productList.getDistrict());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_STATE,productList.getState());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_PINCODE,productList.getPincode());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_EMAIL,productList.getEmail());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ADDRESS,productList.getAddress());

                Shared_Preferences.setPrefs(context, Constants.OfferID, productList.getOffer_id());
                Shared_Preferences.setPrefs(context, Constants.OFFER_NAME, productList.getOffer_name());
                Shared_Preferences.setPrefs(context, Constants.OFFER_VALIDITY, productList.getValidity());
                Shared_Preferences.setPrefs(context, Constants.OFFER_DETAILS, productList.getOffer_description());
                Shared_Preferences.setPrefs(context, Constants.OFFER_TYPE, productList.getOffer_type());
                Shared_Preferences.setPrefs(context, Constants.OFFER_CODE, productList.getOffer_code());
                Shared_Preferences.setPrefs(context, Constants.OFFER_PRICE, productList.getOffer_price());
                Shared_Preferences.setPrefs(context, Constants.OFFER_TERM, productList.getOffer_term());
                Shared_Preferences.setPrefs(context, Constants.IS_FAVORITE, productList.getIsFavorite());
                context.startActivity(intent);*/

            }
        });

    }
    /*private void getDropboxIMGSize(Uri uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getLastPathSegment()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.d("image", "getDropboxIMGSize: "+imageHeight+","+imageWidth);

    }*/



    private void sendData(String product_id) {
        String user_id = Shared_Preferences.getPrefs(context, Constants.REG_ID);
        Log.d("id", "sendData: " + user_id + "," + product_id);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.sendData(user_id, product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = "";
                    output = response.body().string();
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
        return productList1.size();

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

        TextView txt_product_name, txt_vendor_name, txt_location, txt_price;
        ImageView image, image_like, image_unlike;
        LinearLayout btn_send_enquiry, btn_call_now;

        public MyViewHolder(View itemView) {
            super(itemView);
            int pos = getAdapterPosition();

            context = itemView.getContext();
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_vendor_name = itemView.findViewById(R.id.txt_vendor_name);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_price = itemView.findViewById(R.id.txt_price);

            image = itemView.findViewById(R.id.image);
            image_like = itemView.findViewById(R.id.image_like);
            image_unlike = itemView.findViewById(R.id.image_unlike);
           // image_share = itemView.findViewById(R.id.image_share);
            btn_send_enquiry = itemView.findViewById(R.id.btn_send_enquiry);
            btn_call_now = itemView.findViewById(R.id.btn_call_now);

        }

    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addRemoveFavourite")
        Call<ResponseBody> sendData(
                @Field("user_id") String user_id,
                @Field("product_id") String product_id

        );
    }

}

