package com.windhans.client.forworld.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Model.Service_Images;
import com.windhans.client.forworld.R;
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

public class ServiceUpdateImagesAdapter extends RecyclerView.Adapter<ServiceUpdateImagesAdapter.MyViewHolder> {
    Context context;
    View view;
    private ArrayList<Service_Images> imageList;
    private RecyclerViewItemInterface viewItemInterface;




    public interface RecyclerViewItemInterface {

        void onItemClick(int position, String path);

    }

    public ServiceUpdateImagesAdapter(Context context, ArrayList<Service_Images> imageList, RecyclerViewItemInterface recyclerViewItemInterface) {
        this.context = context;
        this.imageList = imageList;
        this.viewItemInterface = recyclerViewItemInterface;

    }



    @Override
    public ServiceUpdateImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multipleimage_service_update_list_view, parent, false);
        ServiceUpdateImagesAdapter.MyViewHolder holder = new ServiceUpdateImagesAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceUpdateImagesAdapter.MyViewHolder holder, final int position) {


        Log.d("ImagePath1", "instantiateItem: "+ MyConfig.JSON_ServicePath+imageList.get(position).getImage_name());
        Glide.with(context)
                .load(MyConfig.JSON_ServicePath+imageList.get(position).getImage_name())
                .into(holder.iv_images);


      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  Intent intent = new Intent(context, ImageViewZoom.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imageList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);*//*
                viewItemInterface.onItemClick(position,imageList.get(position).getImage_name() );


            }
        });*/
        holder.iv_images_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You want to Delete Service");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String product_id=imageList.get(position).getImage_id();
                                Log.d("product_id", "onClick: "+product_id);
                                String user_id= Shared_Preferences.getPrefs(context, Constants.REG_ID);
                                viewItemInterface.onItemClick(position,imageList.get(position).getImage_id() );
                                deleteImage(product_id);

                               // notifyDataSetChanged();
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

    }

    private void deleteImage(String image_id) {
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(context, Constants.REG_ID);
        Log.d("ids", "deleteImage: "+image_id+","+user_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.deleteServiceImage(image_id,user_id);
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


    @Override
    public int getItemCount() {
        return imageList.size();

    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/deleteServiceImage")
        Call<ResponseBody> deleteServiceImage(
                @Field("serviceImage_id") String serviceImage_id,
                @Field("user_id") String user_id
        );

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

       private ImageView iv_images,iv_images_close;

        public MyViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            iv_images = itemView.findViewById(R.id.iv_images);
            iv_images_close = itemView.findViewById(R.id.iv_images_close);




        }

    }



}

