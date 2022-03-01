package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Model.FavoriteModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
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

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.MyViewHolder>   {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Context context;
    View view;
    List<FavoriteModel> favoriteModelList;
    int flag=1;

     AdapterCallback adapterCallback;

    public MyFavoriteAdapter(Context context, List<FavoriteModel> favoriteModelList) {
        this.context=context;
        this.favoriteModelList=favoriteModelList;
        adapterCallback = ((AdapterCallback) context);
    }

    @Override
    public MyFavoriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

     //   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_favorite_row, parent, false);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_favorite_row1, parent, false);
        MyFavoriteAdapter.MyViewHolder holder = new MyFavoriteAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyFavoriteAdapter.MyViewHolder holder, final int position) {
        int adapter_pos=holder.getAdapterPosition();
        final FavoriteModel favoriteModel=favoriteModelList.get(position);
        holder.txt_product_name.setText(favoriteModel.getProduct_name());
        holder.txt_vendor_name.setText(favoriteModel.getDescription());
        holder.txt_description.setText(favoriteModel.getDescription());
       /* holder.txt_location.setText(favoriteModel.getAddress());
        holder.txt_price.setText(favoriteModel.getPrice());*/

        if (favoriteModel.getImage() != null && !favoriteModel.getImage().isEmpty() && !favoriteModel.getImage().equals("null")) {
            Log.d("image", "response: "+MyConfig.JSON_BusinessImage +favoriteModel.getImage());
            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + favoriteModel.getImage())
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(holder.image);
        }
        if (favoriteModel.getOriginal_cost() != null && !favoriteModel.getOriginal_cost().isEmpty() && !favoriteModel.getOriginal_cost().equals("null")) {
            holder.txt_original_cost.setText(Constants.rs+" "+favoriteModel.getOriginal_cost());
            holder.txt_original_cost.setPaintFlags( holder.txt_original_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.txt_original_cost.setVisibility(View.INVISIBLE);
        }
        if (favoriteModel.getSelling_price() != null && !favoriteModel.getSelling_price().isEmpty() && !favoriteModel.getSelling_price().equals("null")) {
            holder.txt_selling_cost.setText(Constants.rs+" "+favoriteModel.getSelling_price());
        }
        else {
            holder.txt_selling_cost.setVisibility(View.INVISIBLE);
        }
        holder.txt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData(favoriteModelList.get(adapter_pos).getProduct_id());
                Log.d("product_id", "onClick: "+favoriteModelList.get(position).getProduct_id());

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }





    @Override
    public int getItemCount() {
        return favoriteModelList.size();

    }
    public static interface AdapterCallback {
        void onMethodCallback();
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
        MyTextView_Poppins_Regular txt_description, txt_remove;
        TextView txt_vendor_name,txt_location,txt_selling_cost,txt_original_cost;
        ImageView image,image_like,image_dislike;
        LinearLayout btn_send_enquiry,btn_call_now;
        public MyViewHolder(View itemView) {
            super(itemView);
            int pos=getAdapterPosition();

            context = itemView.getContext();
            txt_product_name=itemView.findViewById(R.id.txt_product_name);
            txt_vendor_name=itemView.findViewById(R.id.txt_vendor_name);
            txt_location=itemView.findViewById(R.id.txt_location);
            txt_remove=itemView.findViewById(R.id.txt_remove);
            txt_selling_cost=itemView.findViewById(R.id.txt_selling_cost);
            txt_original_cost=itemView.findViewById(R.id.txt_original_cost);
            txt_description=itemView.findViewById(R.id.txt_description);

            image=itemView.findViewById(R.id.image);
          //  image_dislike=itemView.findViewById(R.id.image_dislike);
            image_like=itemView.findViewById(R.id.image_like);
          //  btn_send_enquiry=(LinearLayout) itemView.findViewById(R.id.btn_send_enquiry);
           // btn_call_now=(LinearLayout)itemView.findViewById(R.id.btn_call_now);


        }

    }

    private void sendData(String product_id) {
        String user_id = Shared_Preferences.getPrefs(context,Constants.REG_ID);
        Log.d("id", "sendData: "+user_id+","+product_id);
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.sendData(
                user_id,
                product_id
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output="";
                    output=response.body().string();

                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        String reason=jsonObject.getString("reason");
                        if(result)
                        {
                            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                            adapterCallback.onMethodCallback();
                        }
                        else {
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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/addRemoveFavourite")
        Call<ResponseBody> sendData(
                @Field("user_id") String user_id,
                @Field("product_id") String product_id
        );
    }


}

