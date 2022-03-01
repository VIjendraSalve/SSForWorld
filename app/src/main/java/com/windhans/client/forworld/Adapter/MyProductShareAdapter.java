package com.windhans.client.forworld.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.windhans.client.forworld.Activities.Activity_Share_Products;
import com.windhans.client.forworld.Model.VendorProductData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;

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

public class MyProductShareAdapter extends RecyclerView.Adapter<MyProductShareAdapter.MyViewHolder>{
    Context context;
    List<VendorProductData> vendorProductDataList;
    List<String> stringList=new ArrayList<>();
    View view;
    private final int SHARE_STORAGE_PERMS_REQUEST_CODE = 900;
    private final int SAVE_STORAGE_PERMS_REQUEST_CODE = 901;

    private final String[] perms = { android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_EXTERNAL_STORAGE};


    private static String IMAGE_URL = "";

    private static ImagePipelineFactory sInstance = null;
    private ImagePipelineConfig imagePipelineConfig;


    public MyProductShareAdapter(Activity_Share_Products activityMyProduct, List<VendorProductData> vendorProductDataList) {
        this.context=activityMyProduct;
        this.vendorProductDataList=vendorProductDataList;
    }

    @NonNull
    @Override
    public MyProductShareAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      //  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.buisness_list1, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_share_listing_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductShareAdapter.MyViewHolder myViewHolder, int position) {
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

        myViewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stringList.add(vendorProductDataList.get(position).getProduct_name());
                    stringList.add(vendorProductDataList.get(position).getPrice());
                    stringList.add(vendorProductDataList.get(position).getImage());

                    Intent intent = new Intent("share-message");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putStringArrayListExtra("share_product", (ArrayList<String>) stringList);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d("string_size", "onBindViewHolder: "+stringList);
                    Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();

                }
                else {

                }
            }
        });

      /*  myViewHolder.imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context,myViewHolder.imageView_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Update"))
                        {
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
                        else if(item.getTitle().equals("Share"))
                        {   IMAGE_URL=MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + model.getImage();
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
                            }



                        }
                        else
                        {
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
                       // Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
            }
        });*/


        /*myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ActivityMyProductDetails1.class);
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
        });*/
    }

   /* private void saveImage(String imageUrl) {
        Bitmap bmp = getBitmapFromUrl(imageUrl);
        if(bmp == null) {
            //Show no bitmap message
            return;
        }

        String folder_name = "Demo";
        String pathname = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/"+folder_name+"/";
        File storageDir = new File(pathname);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        File imageFile = new File(storageDir.getAbsolutePath() + "/" + imageFileName + ".png");
        Uri photoURI;

//        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N))
        photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile);
//        else
//            photoURI = Uri.fromFile(imageFile);
//

        if(photoURI == null) {
            Toast.makeText(context, "image_could_not_be_created", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            //Refreshing image on gallery
            MediaScannerConnection.scanFile(context, new String[] { imageFile.getPath() }, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri)
                {

                }
            });

            Toast.makeText(context,"iamge", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e("msg", "SavePublicImageFromPost: ", e);
        } catch (IOException e) {
            Log.e("msg1", "SavePublicImageFromPost: ", e);
        }

    }*/




    @SuppressLint("RestrictedApi")
   /* private Bitmap getBitmapFromUrl(String url1) {
*//*
        Bitmap image = null;
        try {
       *//**//*     URL url = new URL(url1);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());*//**//*

            URL url = new URL(url1);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            image = BitmapFactory.decodeStream(input);
        } catch(IOException e) {
            Log.d("image", "getBitmapFromUrl: "+e.getMessage());
        }*//*
        final Bitmap[] bitmap = {null};
        Glide.with(context)
                .load(url1)
                .asBitmap()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bimtapImage=resource;
                        bitmap[0] =bimtapImage;
                        //Convert this bimtapImage to byte array
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
        return bitmap[0];
    }*/
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
        ImageView iv_image,delete,imageView_edit,imageView_menu;
        CheckBox checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductTitle=itemView.findViewById(R.id.txt_ProductTitle);
            txt_description=itemView.findViewById(R.id.txt_description);
            txt_price=itemView.findViewById(R.id.txt_price);

            iv_image=itemView.findViewById(R.id.iv_image);
            imageView_menu=itemView.findViewById(R.id.imageView_menu);
            checkbox=itemView.findViewById(R.id.checkbox);
           /* imageView_edit=itemView.findViewById(R.id.imageView_edit);
            delete=itemView.findViewById(R.id.delete);*/
        }
    }
}

