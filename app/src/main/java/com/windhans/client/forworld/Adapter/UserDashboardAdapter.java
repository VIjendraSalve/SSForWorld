package com.windhans.client.forworld.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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
import com.windhans.client.forworld.Model.CategoryModel;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class UserDashboardAdapter extends RecyclerView.Adapter<UserDashboardAdapter.MyViewHolder> {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Context context;
    List<CategoryModel> dashboardList;
    View view;
    int row_index;
    private int position1;
    RecyclerView recycler_view;
    ArrayList<NewProduct> productList1; //vije
    int flag = 1;
    private String IMAGE_URL = "";
    private int STORAGE_PERMISSION_CODE=23;
    ArrayList<String> imageArraylist=new ArrayList<>();
   /* public UserDashboardAdapter(Context context, List<CategoryModel> dashboardList, RecyclerView recycler_view) {
        this.context = context;
        this.dashboardList = dashboardList;
        this.recycler_view = recycler_view;
    }*/

    public UserDashboardAdapter(Context context, ArrayList<NewProduct> productList) { //vije
        this.context = context;
        this.productList1 = productList;

    }


    @Override
    public UserDashboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gridlayout1, parent, false);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid_user_layout, parent, false);
       // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid_user_layout_new, parent, false);
        UserDashboardAdapter.MyViewHolder holder = new UserDashboardAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(UserDashboardAdapter.MyViewHolder holder, final int position) {
        String[] mPermission = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
      /*  DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        Log.d("height", "onBindViewHolder: "+cardwidth+","+cardheight);
        holder.image.getLayoutParams().width = (int) (cardwidth / 1.7);
        holder.image.getLayoutParams().height = (int) (cardheight / 5);*/
        int pos = holder.getAdapterPosition();
        NewProduct productList = productList1.get(pos);
        holder.txt_productDetails.setVisibility(View.VISIBLE);
            holder.txt_location.setVisibility(View.VISIBLE);
            holder.txt_price.setVisibility(View.VISIBLE);
            holder.txt_product_name.setText(productList.getName());
            holder.txt_productDetails.setText(productList.getDescription());
        Log.d("price", "onBindViewHolder: "+productList.getSelling_price()+","+productList.getOriginal_cost());
        if (productList.getSelling_price() != null && !productList.getSelling_price().isEmpty() && !productList.getSelling_price().equals("null")) {
            holder.txt_original_cost.setText(Constants.rs+productList.getSelling_price());
        }
        else {
            holder.txt_original_cost.setVisibility(View.INVISIBLE);
        }

           // holder.txt_location.setText(productList.getAddress());
            holder.txt_price.setText(Constants.rs +productList.getSelling_price());

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




        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Like", "onClick: "+productList1.get(position).getIs_favourite());

                    if (productList1.get(position).getIs_favourite().equals("1")) {
                        holder.image_like.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                        productList1.get(pos).setIs_favourite("0");
                        flag = 0;
                        String product_id = "";
                        for (int i = 0; i < productList1.size(); i++) {
                            String product_name = productList.getName();
                            String product_name1 = productList1.get(i).getName();
                              /*  String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*/
                            Log.d("name", "onClick: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList1.get(i).getId();
                                Log.d("product_id", "onClick: " + product_id);
                            }
                        }
                        sendData(product_id);

                    } else {
                        holder.image_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                        productList1.get(pos).setIs_favourite("1");
                        flag = 1;
                        String product_id = "";

                        for (int i = 0; i < productList1.size(); i++) {
                                /*String product_name=holder.txt_product_name.getText().toString();
                                String product_name1=productList1.get(i).getProduct_name();*/
                            String product_name = productList.getName();
                            String product_name1 = productList1.get(i).getName();
                            Log.d("name", "onClick1: " + product_name + "," + product_name1);
                            if (product_name.equals(product_name1)) {
                                product_id = productList1.get(i).getId();

                            }
                        }
                        sendData(product_id);
                    }

                }
        });
       /* holder.btn_send_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenrateEnquiry.class);
                context.startActivity(intent);
            }
        });*/

      /*  holder.btn_call_now.setOnClickListener(new View.OnClickListener() {
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
        });*/
        holder.image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Drawable mDrawable = holder.image.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "Image Description", null);
                        Uri uri = Uri.parse(path);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "The Best");

                        // String next = "<font color='#ff8c00'><strong>" + imageModelArrayList.get(position).BusinessName + "</strong></font>";

                        String text = "ForWorld" + "\n\n" + "Proudct : " + productList.getName() + "\n\n" + "Desc : " + productList.getDescription() + "\n\nTo buy this best quality products \nhttp://www.forworld.info/" + productList.getId()
                                + "/" + productList.getSelling_price() + "/" + productList.getSub_category() + "/" + productList.getId();
                        //Uri pictureUri = Uri.parse(MyConfig.JSON_PRODUCTS_URL+products.getProduct_banner_image());
                     //   Intent shareIntent = new Intent();
                   //     shareIntent.setType("image/*");
                       // shareIntent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, text);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(Intent.createChooser(intent, "Share images..."));

                              /*  if (imgBitmapPath != null) {
                                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                                    String text = "bizapp" + "\n\n" + "Proudct : " + model.getProduct_name() + "\n\n" + "Desc : " + model.getDescription();
                                    Intent shareIntent = new Intent();
                                    shareIntent.setType("image/*");
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    context.startActivity(Intent.createChooser(shareIntent, "Share images..."));
                                } else {

                                    Toast.makeText(context, "Image is downloading Please wait...", Toast.LENGTH_SHORT).show();

                                }*/
                    }

                    /*IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + productList.getProduct_image();
                    Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                    Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                    String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);
                    //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                    if (imgBitmapPath != null) {
                        Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                        String text = "ForWorld" + "\n\n" + "Proudct : " + productList.getName() + "\n\n" + "Desc : " + productList.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + productList.getId()
                                + "/" + productList.getSelling_price() + "/" + productList.getSub_category() + "/" + productList.getId();
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
                    }*/
                }

              /*  PopupMenu popup = new PopupMenu(context, holder.image_share);
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
                            IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + productList.getProduct_image();
                            Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                            Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                            String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);
                            //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                            if (imgBitmapPath != null) {
                                Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                                String text = "ForWorld" + "\n\n" + "Proudct : " + productList.getName() + "\n\n" + "Desc : " + productList.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + productList.getId()
                                        + "/" + productList.getSelling_price() + "/" + productList.getSub_category() + "/" + productList.getId();
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

                popup.show();*/

        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
              /*  Intent intent=new Intent(context, ActivitySubCategory.class);
                Shared_Preferences.setPrefs(context, Constants.CATEGORY_ID,categoryModel.getCategory_id());
                context.startActivity(intent);*/
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putParcelableArrayListExtra(Constants.ProductList, productList1);
                intent.putExtra(Constants.Position, position);

                imageArraylist.add("");
                intent.putStringArrayListExtra(String.valueOf(context),imageArraylist);
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, productList.getProduct_image());

                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, productList1.get(position).getId());
            //    Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID, productList1.get(position).getBusiness_id());
            //    Shared_Preferences.setPrefs(context, Constants.BUSINESS_NAME, productList1.get(position).getBusiness_name());
            //    Shared_Preferences.setPrefs(context, Constants.BUSINESS_EMAIL, productList1.get(position).getEmail());
            //    Shared_Preferences.setPrefs(context, Constants.BUSINESS_DEC, productList1.get(position).getBuisness_description());
            //    Shared_Preferences.setPrefs(context, Constants.CONTACT_NAME, productList1.get(position).getContact_name());
            //    Shared_Preferences.setPrefs(context, Constants.CONTACT_NO, productList1.get(position).getContact_mobile());
            //    Shared_Preferences.setPrefs(context, Constants.BUSINESS_CONTACT, productList1.get(position).getContact_mobile());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME, productList1.get(position).getName());
                Shared_Preferences.setPrefs(context, Constants.BRAND, productList1.get(position).getBrand());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_DEC, productList1.get(position).getDescription());
             //   Shared_Preferences.setPrefs(context, Constants.BUISNESS_BANNER, productList1.get(position).getBusiness_banner());


             //   Shared_Preferences.setPrefs(context, Constants.OfferID, productList1.get(position).getOffer_id());
             //   Shared_Preferences.setPrefs(context, Constants.OFFER_NAME, productList1.get(position).getOffer_name());
            //    Shared_Preferences.setPrefs(context, Constants.OFFER_VALIDITY, productList1.get(position).getValidity());
             //   Shared_Preferences.setPrefs(context, Constants.OFFER_DETAILS, productList1.get(position).getOffer_description());
            //    Shared_Preferences.setPrefs(context, Constants.OFFER_TYPE, productList1.get(position).getOffer_type());
            //    Shared_Preferences.setPrefs(context, Constants.OFFER_CODE, productList1.get(position).getOffer_code());
                Shared_Preferences.setPrefs(context, Constants.OFFER_PRICE, productList1.get(position).getOffer_price());
             //   Shared_Preferences.setPrefs(context, Constants.OFFER_TERM, productList1.get(position).getOffer_term());
                Shared_Preferences.setPrefs(context, Constants.IS_FAVORITE, productList1.get(position).getIs_favourite());
                context.startActivity(intent);

            }
        });

    }



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

        MyTextView_Poppins_Bold txt_product_name;
        MyTextView_Poppins_Regular  txt_productDetails, txt_location, txt_price,txt_original_cost;
        ImageView image, image_like, image_share;
        Button btn_send_enquiry, btn_call_now;

        public MyViewHolder(View itemView) {
            super(itemView);
            int pos = getAdapterPosition();

            context = itemView.getContext();
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_productDetails = itemView.findViewById(R.id.txt_productDetails);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_price = itemView.findViewById(R.id.txt_price);

            image = itemView.findViewById(R.id.image_product);
            image_like = itemView.findViewById(R.id.image_like);
            image_share = itemView.findViewById(R.id.image_share);
          /*  btn_send_enquiry = itemView.findViewById(R.id.btn_send_enquiry);
            btn_call_now = itemView.findViewById(R.id.btn_call_now);
*/
            txt_original_cost=itemView.findViewById(R.id.txt_original_cost);

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

