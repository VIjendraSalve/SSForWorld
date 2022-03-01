package com.windhans.client.forworld.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.MyConfig;

import java.util.ArrayList;

public class MultipleImagesAdapter extends RecyclerView.Adapter<MultipleImagesAdapter.MyViewHolder> {
    Context context;
    View view;
    private ArrayList<String> imageList;
    private RecyclerViewItemInterface viewItemInterface;

    public interface RecyclerViewItemInterface {

        void onItemClick(int position, String path);

    }

    public MultipleImagesAdapter(Context context, ArrayList<String> imageList, RecyclerViewItemInterface recyclerViewItemInterface) {
        this.context = context;
        this.imageList = imageList;
        this.viewItemInterface = recyclerViewItemInterface;

    }

    public MultipleImagesAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }


    @Override
    public MultipleImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multipleimage_list_view, parent, false);
        MultipleImagesAdapter.MyViewHolder holder = new MultipleImagesAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MultipleImagesAdapter.MyViewHolder holder, final int position) {


        Log.d("ImagePath1", "instantiateItem: "+ MyConfig.JSON_BusinessImage+imageList.get(position));
        Glide.with(context)
                .load(MyConfig.JSON_BusinessImage+imageList.get(position))

                .into(holder.iv_images);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ImageViewZoom.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imageList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
                viewItemInterface.onItemClick(position,imageList.get(position) );


            }
        });


    }




    @Override
    public int getItemCount() {
        return imageList.size();

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

       private  ImageView iv_images;

        public MyViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            iv_images = itemView.findViewById(R.id.iv_images);




        }

    }



}

