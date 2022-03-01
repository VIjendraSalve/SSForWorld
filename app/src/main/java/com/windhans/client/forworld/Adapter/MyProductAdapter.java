package com.windhans.client.forworld.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.windhans.client.forworld.Activities.ActivityMyProduct;
import com.windhans.client.forworld.Activities.ActivityMyProductDetails1;
import com.windhans.client.forworld.BuildConfig;
import com.windhans.client.forworld.Model.VendorProductData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {
    Context context;
    List<VendorProductData> vendorProductDataList;
    View view;

    private int STORAGE_PERMISSION_CODE = 23;
    private final String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private static String IMAGE_URL = "";
    private static ImagePipelineFactory sInstance = null;
    private ImagePipelineConfig imagePipelineConfig;


    public MyProductAdapter(ActivityMyProduct activityMyProduct, List<VendorProductData> vendorProductDataList) {
        this.context = activityMyProduct;
        this.vendorProductDataList = vendorProductDataList;
    }

    @NonNull
    @Override
    public MyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_listing_row, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_listing_row_new, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.MyViewHolder myViewHolder, int position) {
        VendorProductData model = vendorProductDataList.get(position);
        myViewHolder.txt_ProductTitle.setText(model.getProduct_name());
        myViewHolder.txt_description.setText(model.getDescription());
        if (model.getSelling_price() != null && !model.getSelling_price().isEmpty() && !model.getSelling_price().equals("null")) {
            myViewHolder.txt_price.setText(Constants.rs + "" + model.getSelling_price());
        }
        else {
            myViewHolder.txt_price.setVisibility(View.INVISIBLE);
        }
        if (model.getPrice() != null && !model.getPrice().isEmpty() && !model.getPrice().equals("null")) {
            myViewHolder.txt_original_cost.setText(Constants.rs + "" + model.getPrice());
            myViewHolder.txt_original_cost.setPaintFlags(myViewHolder.txt_original_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            myViewHolder.txt_original_cost.setVisibility(View.INVISIBLE);
        }
/*
        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
       // myViewHolder.iv_image.getLayoutParams().width = (int) (cardwidth / 1.5);
        myViewHolder.iv_image.getLayoutParams().height = (int) (cardheight / 5);*/
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

        myViewHolder.tv_delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You want to Delete Service");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                deleteProduct(vendorProductDataList.get(position).getId(), position);
                                arg0.dismiss();

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



      /*  myViewHolder.imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, myViewHolder.imageView_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Update")) {
                            Intent intent = new Intent(context, ActivityMyProductDetails.class);
                            context.startActivity(intent);
                            Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, model.getBusiness_id());
                            Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME, model.getProduct_name());
                            Shared_Preferences.setPrefs(context, Constants.BRAND, model.getBrand());
                            Shared_Preferences.setPrefs(context, Constants.DESCRIPTION, model.getDescription());
                            Shared_Preferences.setPrefs(context, Constants.PRICE, model.getPrice());
                            Shared_Preferences.setPrefs(context, Constants.SUB_ID, model.getSubcategory_id());
                            Log.d("sub", "onClick: " + model.getSubcategory_id());
                            Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, model.getImage());
                            Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID, model.getId());
                        } else if (item.getTitle().equals("Share")) {
                            *//*IMAGE_URL=MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + model.getImage();
                            Log.d("image", "onMenuItemClick: "+IMAGE_URL);
                            Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                            String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);
                        //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                            if (imgBitmapPath != null) {
                                Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                                String text = "ForWorld" + "\n\n" + "Proudct : " + model.getProduct_name() + "\n\n" + "Desc : " + model.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + model.getBusiness_id()
                                        + "/" + model.getPrice() + "/" +model.getSubcategory_id() + "/" + model.getId();
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
                            }*//*

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
                               *//* Log.d("title", "onMenuItemClick: " + item.getTitle());
                                IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + model.getImage();
                                Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                                Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                                String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);
                                //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                                if (imgBitmapPath != null) {
                                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                                    String text = "ForWorld" + "\n\n" + "Proudct : " + model.getProduct_name() + "\n\n" + "Desc : " + model.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + model.getBusiness_id()
                                            + "/" + model.getPrice() + "/" + model.getSubcategory_id() + "/" + model.getId();
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
                                }*//*
                                Drawable mDrawable = myViewHolder.iv_image.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "Image Description", null);
                                Uri uri = Uri.parse(path);

                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "The Best");

                                // String next = "<font color='#ff8c00'><strong>" + imageModelArrayList.get(position).BusinessName + "</strong></font>";

                                String text = "ForWorld" + "\n\n" + "Proudct : " + model.getProduct_name() + "\n\n" + "Desc : " + model.getDescription() + "\n\nTo buy this best quality products \nhttp://www.ssforworld.info/" + model.getBusiness_id()
                                        + "/" + model.getPrice() + "/" + model.getSubcategory_id() + "/" + model.getId();
                                //Uri pictureUri = Uri.parse(MyConfig.JSON_PRODUCTS_URL+products.getProduct_banner_image());
                                Intent shareIntent = new Intent();
                                shareIntent.setType("image/*");
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(Intent.createChooser(shareIntent, "Share images..."));
                            }


                            //Share facebook
                *//*ShareDialog shareDialog;
                FacebookSdk.sdkInitialize(getContext());
                shareDialog = new ShareDialog(getActivity());
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(products.getProduct_name())
                        .setContentDescription(
                                products.getProduct_description()+"\n\n Download App Now \nhttps://play.google.com/store/apps/details?id=com.windhans.client.libas")
                        .setContentUrl(Uri.parse(MyConfig.PRODUCT_IMG_URL+products.getProduct_banner_image()))
                        .build();
                shareDialog.show(linkContent);*//*


                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("Are you sure,You want to Delete Product");
                            alertDialogBuilder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            String product_id = model.getId();
                                            String user_id = Shared_Preferences.getPrefs(context, Constants.REG_ID);
                                            deleteProduct(product_id, user_id);

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
                        // Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
            }
        });*/

        myViewHolder.tv_share_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + model.getImage();
                Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                Drawable mDrawable = myViewHolder.iv_image.getDrawable();
                //Bitmap mBitmap = Bitmap.createBitmap(((BitmapDrawable) mDrawable).getBitmap());
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "The Best");

                String next = model.getProduct_name();
                String url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                String sAux = next +"\b\n"+ Shared_Preferences.getPrefs(context, Constants.REG_NAME)
                        +"\b\n"+ Shared_Preferences.getPrefs(context, Constants.VendorAddress)
                        +"\b\n"+ Shared_Preferences.getPrefs(context, Constants.REG_MOBILE)
                        +"\b\n"+ Shared_Preferences.getPrefs(context, Constants.REG_EMAIL)
                        +" \b\n " +model.getDescription() + "\b\n" + url;
                // sAux = sAux + IConstant.appUrl + "\n\n";
                intent.putExtra(Intent.EXTRA_TEXT, sAux);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, "Share Post"));
            }
        });


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityMyProductDetails1.class);
                intent.putParcelableArrayListExtra("productList", (ArrayList<? extends Parcelable>) vendorProductDataList);
                intent.putExtra("Position",String.valueOf(position));
                context.startActivity(intent);
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_ID, model.getBusiness_id());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_NAME, model.getProduct_name());
                Shared_Preferences.setPrefs(context, Constants.BRAND, model.getBrand());
                Shared_Preferences.setPrefs(context, Constants.DESCRIPTION, model.getDescription());
                Shared_Preferences.setPrefs(context, Constants.PRICE, model.getPrice());
                Shared_Preferences.setPrefs(context, Constants.SUB_ID, model.getSubcategory_id());
                Log.d("sub", "onClick: " + model.getSubcategory_id());
                Shared_Preferences.setPrefs(context, Constants.PRODUCT_IMAGE, model.getImage());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ID, model.getId());
            }
        });
    }


    private void deleteProduct(String product_id, int position) {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);

        Call<ResponseBody> responseBodyCall = getOrderApi.deleteProduct(product_id,
                Shared_Preferences.getPrefs(context, Constants.REG_ID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("DeleteProduct", "onResponse: "+output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");
                    if (res) {

                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                        vendorProductDataList.remove(position);
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/deleteProduct")
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
        MyTextView_Poppins_Regular txt_ProductTitle, txt_description, txt_price,txt_original_cost, tv_delete_product, tv_share_product;
        ImageView iv_image, delete, imageView_edit, imageView_menu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductTitle = itemView.findViewById(R.id.txt_ProductTitle);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_original_cost = itemView.findViewById(R.id.txt_original_cost);
            tv_delete_product = itemView.findViewById(R.id.tv_delete_product);
            tv_share_product = itemView.findViewById(R.id.tv_share_product);

            iv_image = itemView.findViewById(R.id.iv_image);
            imageView_menu = itemView.findViewById(R.id.imageView_menu);
           /* imageView_edit=itemView.findViewById(R.id.imageView_edit);
            delete=itemView.findViewById(R.id.delete);*/
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}

