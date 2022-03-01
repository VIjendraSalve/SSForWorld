package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.gallery_view.ImageViewZoom;
import com.windhans.client.forworld.my_library.MyConfig;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MultipleImagesProductAdapter extends RecyclerView.Adapter<MultipleImagesProductAdapter.MyViewHolder> {

    Context context;
    View view;
    private ArrayList<String> imageList;
    private RecyclerViewItemInterface viewItemInterface;



  /*  public MultipleImagesProductAdapter(Context context, ArrayList<String> imageList, RecyclerViewItemInterface recyclerViewItemInterface) {
        this.context = context;
        this.imageList = imageList;
        this.viewItemInterface = recyclerViewItemInterface;

    }*/

    public MultipleImagesProductAdapter(Context context, ArrayList<String> imageList, RecyclerViewItemInterface recyclerViewItemInterface) {
        this.context = context;
        this.imageList = imageList;
        this.viewItemInterface = recyclerViewItemInterface;
    }


    @Override
    public MultipleImagesProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multipleimage_list_view1, parent, false);
        MultipleImagesProductAdapter.MyViewHolder holder = new MultipleImagesProductAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MultipleImagesProductAdapter.MyViewHolder holder, final int position) {


        Log.d("ImagePath1", "instantiateItem: "+ MyConfig.JSON_BusinessImage+imageList.get(position));
        Glide.with(context)
                .load(MyConfig.JSON_BusinessImage+imageList.get(position))

                .into(holder.iv_images);

       /* holder.iv_images_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("image_name", "onClick: "+imageList.get(position));
                Toast.makeText(context, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                String name=imageList.get(position);
                //deleteProductImage(imageList.get(position));

                notifyDataSetChanged();
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("imagePath", "onClick: "+imageList.get(position));
                Intent intent = new Intent(context, ImageViewZoom.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imageList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
               // viewItemInterface.onItemClick(position,imageList.get(position));


            }
        });


    }



    public interface RecyclerViewItemInterface {

        void onItemClick(String path);

    }


    @Override
    public int getItemCount() {
        return imageList.size();

    }

    public interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/deleteProductImage")
        Call<ResponseBody> deleteProduct(
                @Field("ProductImageName") String service_id,
                @Field("product_id") String user_id,
                @Field("user_id") String product_id

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

       private  ImageView iv_images,iv_images_close;

        public MyViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            iv_images = itemView.findViewById(R.id.iv_images);
          //  iv_images_close = itemView.findViewById(R.id.iv_images_close);




        }

    }



}

