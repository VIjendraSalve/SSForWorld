package com.windhans.client.forworld.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.windhans.client.forworld.R;

import java.util.List;

public class OfferAdapter  extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {
    Context context;

    View view;
    List<String> offerarrayList;
    List<Integer> stringImageList;
/*    public OfferAdapter(Context context, List<String> stringList, List<Integer> stringImageList) {
        this.context=context;
        this.stringList=stringList;
        this.stringImageList=stringImageList;
    }*/

    public OfferAdapter(Context context, List<String> offerarrayList, List<Integer> stringImageList) {    this.context=context;
        this.context=context;
        this.offerarrayList=offerarrayList;
        this.stringImageList=stringImageList;

    }


    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_offer_adapter, parent, false);
        OfferAdapter.MyViewHolder holder = new OfferAdapter.MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final OfferAdapter.MyViewHolder holder, final int position) {

        holder.txt_title.setText(offerarrayList.get(position));
        int imag=stringImageList.get(0);
        holder.circle_image.setImageResource((imag));






    }

    @Override
    public int getItemCount() {
        return offerarrayList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView circle_image;
        TextView txt_title;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            circle_image=itemView.findViewById(R.id.circle_image);
            txt_title=itemView.findViewById(R.id.txt_title);



        }

    }

}


