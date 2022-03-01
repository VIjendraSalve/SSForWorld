package com.windhans.client.forworld.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.windhans.client.forworld.Activities.ActivityUpdateService;
import com.windhans.client.forworld.Activities.Activity_My_Service_Details;
import com.windhans.client.forworld.Model.MyServiceModel;
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

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.MyViewHolder>{
    Context context;
    List<MyServiceModel> myServiceModelList;
    View view;
    OnClickInterface onClickInterface;
    ArrayList<String> imageArraylist=new ArrayList<>();
    private int STORAGE_PERMISSION_CODE=23;
    private final String[] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE};


    private static String IMAGE_URL = "";

    private static ImagePipelineFactory sInstance = null;
    private ImagePipelineConfig imagePipelineConfig;


    public MyServiceAdapter(Context context, List<MyServiceModel> vendorProductDataList) {
        this.context=context;
        this.myServiceModelList=vendorProductDataList;
    }

    @NonNull
    @Override
    public MyServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_item_row, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceAdapter.MyViewHolder myViewHolder, int position) {
        MyServiceModel model=myServiceModelList.get(position);
        myViewHolder.txt_Service_name.setText(model.getName());
        myViewHolder.txt_service_desc.setText(model.getDescription());

       /* DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        myViewHolder.iv_image.getLayoutParams().width = (int) (cardwidth / 1.5);
        myViewHolder.iv_image.getLayoutParams().height = (int) (cardheight / 5);*/

        if (model.getBanner_image() != null && !model.getBanner_image().isEmpty() && !model.getBanner_image().equals("null")) {
            Log.d("image", "response: "+model.getBanner_image());


            Glide.with(context)
                    .load(MyConfig.JSON_ServicePath + model.getBanner_image())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                    .into(myViewHolder.iv_image);


        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                    .into(myViewHolder.iv_image);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Activity_My_Service_Details.class);
                intent.putParcelableArrayListExtra(Constants.Service_LIst, (ArrayList<? extends Parcelable>) myServiceModelList);
                intent.putExtra(Constants.Position, position);
                imageArraylist.add("");
                intent.putStringArrayListExtra(String.valueOf(context),imageArraylist);
                context.startActivity(intent);

            }
        });

        myViewHolder.imageView_menu.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent=new Intent(context, ActivityUpdateService.class);
                            intent.putParcelableArrayListExtra(Constants.Service_LIst, (ArrayList<? extends Parcelable>) myServiceModelList);
                            intent.putExtra(Constants.Position, position);
                            imageArraylist.add("");
                            intent.putStringArrayListExtra(String.valueOf(context),imageArraylist);
                            Shared_Preferences.setPrefs(context,"ServiceName",model.getName());
                            Shared_Preferences.setPrefs(context,"CategoryIDForSend",model.getCategory_id());
                            Shared_Preferences.setPrefs(context,"SubCategoryIDForSend",model.getSubcategory_id());
                            Shared_Preferences.setPrefs(context,"Service_id",model.getId());
                            context.startActivity(intent);

                           // context.startActivity(intent);

                         /*   intent.putParcelableArrayListExtra("VendorArrayList", (ArrayList<? extends Parcelable>) vendorProductDataList);
                            intent.putExtra("Position",String.valueOf(position));
                            context.startActivity(intent);*/

                        }
                        else if(item.getTitle().equals("Share"))
                        {
                          /*  Log.d("title", "onMenuItemClick: " + item.getTitle());
                            IMAGE_URL = MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" + model.getImage();
                            Log.d("image", "onMenuItemClick: " + IMAGE_URL);
                            Bitmap bmp = getBitmapFromUrl(IMAGE_URL);
                            String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", null);*/
                            //  Log.d("image", "onMenuItemClick: "+imgBitmapPath);
                            if (ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                } else {
                                    ActivityCompat.requestPermissions((Activity) context,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            STORAGE_PERMISSION_CODE);


                                }
                            } else {

                                Drawable mDrawable = myViewHolder.iv_image.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "Image Description", null);
                                Uri uri = Uri.parse(path);

                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "The Best");

                                // String next = "<font color='#ff8c00'><strong>" + imageModelArrayList.get(position).BusinessName + "</strong></font>";

                                String text = "Hometown" + "\n\n" + "Service : " + model.getName() + "\n\n" + "Desc : " + model.getDescription()+ "\n\n" +"https://play.google.com/store/apps/details?id="+ context.getApplicationContext().getPackageName();
                                // String sAux = imageModelArrayList.get(position).BusinessName + "\b\n" + imageModelArrayList.get(position).Address + ", " + imageModelArrayList.get(position).Area + ", " + imageModelArrayList.get(position).City + ", " + imageModelArrayList.get(position).PostalCode + ", " + imageModelArrayList.get(position).Country + "\n\n"/* + " , " + "\n" + imageModelArrayList.get(position).ExploreDescription + "\n"*/;
                                //sAux = sAux + IConstant.appUrl + "\n\n";
                                intent.putExtra(Intent.EXTRA_TEXT, text);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(Intent.createChooser(intent, "Share Post"));
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


                        }
                        else
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("Are you sure,You want to Delete Service");
                            alertDialogBuilder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            String product_id=model.getId();
                                           // String user_id=Shared_Preferences.getPrefs(context,Constants.REG_ID);
                                            deleteProduct(product_id,position);

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
        });
    }

    private void deleteProduct(String product_id, int position) {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(context, Constants.REG_ID);

        Call<ResponseBody> responseBodyCall=getOrderApi.deleteService(product_id,user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        String message=jsonObject.getString("reason");
                        if(result)
                        {

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            ((MyServiceAdapter.OnClickInterface)context).onClickDelete(position);;
                            notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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


    public interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/deleteService")
        Call<ResponseBody> deleteService(
                @Field("service_id") String service_id,
                @Field("user_id") String user_id

        );
    }
    @Override
    public int getItemCount() {
        return myServiceModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Service_name, txt_service_desc;
        ImageView iv_image,delete,imageView_edit,imageView_menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Service_name=itemView.findViewById(R.id.txt_Service_name);
            txt_service_desc=itemView.findViewById(R.id.txt_service_desc);
           /* txt_category_name=itemView.findViewById(R.id.txt_category_name);
            txt_sub_category=itemView.findViewById(R.id.txt_sub_category);
*/
            iv_image=itemView.findViewById(R.id.iv_image_service);
            imageView_menu=itemView.findViewById(R.id.imageView_service_menu);
        }
    }
    public interface OnClickInterface
    {
        void onClickDelete(int pos);
    }
}

