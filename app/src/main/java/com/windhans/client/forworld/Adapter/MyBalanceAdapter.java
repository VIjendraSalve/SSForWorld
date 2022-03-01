package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.windhans.client.forworld.Model.MyWallet;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class MyBalanceAdapter extends RecyclerView.Adapter<MyBalanceAdapter.MyViewHolder> {

    Context context;
    List<MyWallet> earningModels;
    View view;
    OnClickInterface onClickInterface;


    public MyBalanceAdapter(Context context, ArrayList<MyWallet> earningModels) {
        this.context = context;
        this.earningModels = earningModels;
    }

    @NonNull
    @Override
    public MyBalanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_balance, viewGroup, false);
        //   view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_listing_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBalanceAdapter.MyViewHolder myViewHolder, int position) {
        MyWallet model = earningModels.get(position);

        if (model.getTrans_details() != null && !model.getTrans_details().isEmpty() && !model.getTrans_details().equals("null")) {
            myViewHolder.txt_title.setVisibility(View.VISIBLE);
            myViewHolder.txt_title.setText(model.getTrans_details());
        }
        else {
            myViewHolder.txt_title.setVisibility(View.GONE);
        }

        if (model.getAmount() != null && !model.getAmount().isEmpty() && !model.getAmount().equals("null")) {
            myViewHolder.txt_points.setVisibility(View.VISIBLE);
            myViewHolder.txt_points.setText(Constants.rs+""+model.getAmount());
        }
        else {
            myViewHolder.txt_points.setVisibility(View.GONE);
        }

        if (model.getFirst_name() != null && !model.getFirst_name().isEmpty() && !model.getFirst_name().equals("null")) {
            myViewHolder.txt_user_name.setVisibility(View.VISIBLE);
            myViewHolder.txt_user_name.setText(""+model.getFirst_name()+" "+model.getLast_name());
        }
        else {
            myViewHolder.txt_user_name.setVisibility(View.INVISIBLE);
        }

        if (model.getMobile() != null && !model.getMobile().isEmpty() && !model.getMobile().equals("null")) {
            myViewHolder.txt_user_mobile.setVisibility(View.GONE);
            myViewHolder.txt_user_mobile.setText(""+model.getMobile());
        }
        else {
            myViewHolder.txt_user_mobile.setVisibility(View.GONE);
        }

        if(model.getAction().equals("1"))
        {
            //myViewHolder.btn_sign_in.setRotation(140f);
            myViewHolder.iv_plus_minus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black_new_24dp));

        }else {
            myViewHolder.iv_plus_minus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_remove_black_24dp));
           // myViewHolder.btn_sign_in.setRotation(-30f);
        }
        if (model.getCreated_at() != null && !model.getCreated_at().isEmpty() && !model.getCreated_at().equals("null")) {
            myViewHolder.txt_date.setVisibility(View.VISIBLE);
            String date= DateTimeFormat.getDate7(model.getCreated_at());
            myViewHolder.txt_date.setText(date);

        }
        else {
            myViewHolder.txt_date.setVisibility(View.GONE);
        }

      /*  if (model.getProduct_name() != null && !model.getProduct_name().isEmpty() && !model.getProduct_name().equals("null")) {
            myViewHolder.txt_ProductTitle.setText(model.getProduct_name());
        } else {
            myViewHolder.txt_ProductTitle.setVisibility(View.INVISIBLE);
        }*/


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return earningModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Bold txt_title;
        MyTextView_Poppins_Regular  txt_points, txt_date, txt_user_name,txt_user_mobile;
        Button btn_sign_in;
        ImageView iv_plus_minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_sign_in=itemView.findViewById(R.id.btn_sign_in);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_points = itemView.findViewById(R.id.txt_points);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_user_mobile = itemView.findViewById(R.id.txt_user_mobile);
            iv_plus_minus = itemView.findViewById(R.id.iv_plus_minus);

        }
    }

    public interface OnClickInterface {
        void onClickDelete(int pos);
    }
}

