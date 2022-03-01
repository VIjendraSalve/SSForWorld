package com.windhans.client.forworld.Adapter;


import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import com.windhans.client.forworld.Activities.ActivityOfferListing;
import com.windhans.client.forworld.Activities.Activity_offer_details;
import com.windhans.client.forworld.Model.OfferModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
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


public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.MyViewHolder>{
    Context context;
    List<OfferModel> offerModelList;
    View view;




    public OfferListAdapter(ActivityOfferListing activityOfferListing, List<OfferModel> offerModelList) {
        this.context=activityOfferListing;
        this.offerModelList=offerModelList;
    }

    @NonNull
    @Override
    public OfferListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_list_row, viewGroup, false);
        OfferListAdapter.MyViewHolder holder = new OfferListAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferListAdapter.MyViewHolder myViewHolder, int position) {
        OfferModel model=offerModelList.get(position);
        myViewHolder.txt_offerName.setText(model.getName());
        myViewHolder.txt_offerDesc.setText(model.getDescription());
        String validity_date=model.getValidity();
        String date_new= DateTimeFormat.getDateNew(validity_date);
        myViewHolder.txt_validity_date.setText(date_new);

        String offerType=model.getOffer_type();
        /*1="percent wise",2="flat off",3="upto"*/
        if(offerType.equals("1"))
        {
            myViewHolder.txt_offerType.setText(model.getOffer_price()+" "+"% off");
        }
        else if(offerType.equals("2"))
        {

            myViewHolder.txt_offerType.setText(Constants.rs+" "+model.getOffer_price()+" "+"flat off");
        }
        else {
            myViewHolder.txt_offerType.setText(Constants.rs+" "+model.getOffer_price()+" "+"Upto off");
        }
        if (model.getImage() != null && !model.getImage().isEmpty() && !model.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessOffers + model.getImage())
                    .into(myViewHolder.imageView_offer);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(myViewHolder.imageView_offer);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared_Preferences.setPrefs(context,Constants.OfferID,offerModelList.get(position).getId());
                Shared_Preferences.setPrefs(context,Constants.OFFER_NAME,offerModelList.get(position).getName());
                Shared_Preferences.setPrefs(context,Constants.OFFER_TYPE,offerModelList.get(position).getOffer_type());
                Shared_Preferences.setPrefs(context,Constants.OFFER_PRICE,offerModelList.get(position).getOffer_price());
                Shared_Preferences.setPrefs(context,Constants.OFFER_DETAILS,offerModelList.get(position).getDescription());
                Shared_Preferences.setPrefs(context,Constants.OFFER_VALIDITY,offerModelList.get(position).getValidity());
                Shared_Preferences.setPrefs(context,Constants.OFFER_IAMGE,offerModelList.get(position).getImage());
                Shared_Preferences.setPrefs(context,Constants.OFFER_CODE,offerModelList.get(position).getOffer_code());
                Shared_Preferences.setPrefs(context,Constants.OFFER_TERM,offerModelList.get(position).getTerms());
                Shared_Preferences.setPrefs(context,Constants.OFFER_LIMIT,offerModelList.get(position).getOffer_limit());
                Intent intent=new Intent(context, Activity_offer_details.class);
                context.startActivity(intent);
            }
        });

        myViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, you want to delete this Offer");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String product_id=model.getId();
                                // String user_id=Shared_Preferences.getPrefs(context,Constants.REG_ID);
                                deleteoffer(position);

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

    private void deleteoffer(int position) {
        GetOrderAPI getOrderAPI=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        String user_id=Shared_Preferences.getPrefs(context, Constants.REG_ID);
        String offer_id=offerModelList.get(position).getId();
        Log.d("ids", "deleteoffer: "+user_id+","+offer_id);
        Call<ResponseBody> responseBodyCall=getOrderAPI.deleteOffer(user_id,offer_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    try {
                        JSONObject object=new JSONObject(output);
                        Boolean result=object.getBoolean("result");
                        String message=object.getString("reason");
                        if(result)
                        {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }else {
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
        return offerModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView_Poppins_Regular txt_offerName,txt_validity_date,txt_offerType, txt_offerDesc;
        ImageView imageView_offer;
        Button btn_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_offerName=itemView.findViewById(R.id.txt_offerName);
            txt_validity_date=itemView.findViewById(R.id.txt_validity_date);
            txt_offerType=itemView.findViewById(R.id.txt_offerType);
            txt_offerDesc=itemView.findViewById(R.id.txt_offerDesc);
            btn_delete=itemView.findViewById(R.id.btn_delete);
            imageView_offer=itemView.findViewById(R.id.imageView_offer);

        }
    }

    public interface GetOrderAPI
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/deleteOffer")
        Call<ResponseBody> deleteOffer(
                @Field("user_id") String user_id,
                @Field("offer_id") String offer_id

        );
    }
}


