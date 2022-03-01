package com.windhans.client.forworld.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.Activity_offer_details;
import com.windhans.client.forworld.Model.ProductData;
import com.windhans.client.forworld.R;
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

public class ProductDataAdapter  extends RecyclerView.Adapter<ProductDataAdapter.MyViewHolder>{
    Context context;
    List<ProductData> productDataList;
    View view;
    private ProgressDialog progressDialog;




    public ProductDataAdapter(Activity_offer_details activityOfferListing, List<ProductData> offerModelList) {
        this.context=activityOfferListing;
        this.productDataList=offerModelList;
    }

    @NonNull
    @Override
    public ProductDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_row, viewGroup, false);
        ProductDataAdapter.MyViewHolder holder = new ProductDataAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDataAdapter.MyViewHolder myViewHolder, int position) {
        ProductData model=productDataList.get(position);
        myViewHolder.txt_productName.setText(model.getProduct_name());
        myViewHolder.txt_productDetails.setText(model.getDescription());

        myViewHolder.iv_remove_product_from_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure,You want to remove this product from offer");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String product_id=productDataList.get(position).getProduct_id();
                                // String user_id=Shared_Preferences.getPrefs(context,Constants.REG_ID);
                                removeProductFromOffer(product_id,position);

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

        if (model.getImage() != null && !model.getImage().isEmpty() && !model.getImage().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + model.getImage())
                    .into(myViewHolder.imageView_product);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(myViewHolder.imageView_product);
        }


    }




    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_productName,txt_productDetails;
        ImageView imageView_product, iv_remove_product_from_offer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_productName=itemView.findViewById(R.id.txt_productName);
            txt_productDetails=itemView.findViewById(R.id.txt_productDetails);
            iv_remove_product_from_offer=itemView.findViewById(R.id.iv_remove_product_from_offer);

            imageView_product=itemView.findViewById(R.id.imageView_product);

        }
    }

    private void removeProductFromOffer(String product_id, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d("RemoveProduct", "removeProductFromOffer: "+Shared_Preferences.getPrefs(context, Constants.REG_ID));
        Log.d("RemoveProduct", "removeProductFromOffer: "+Shared_Preferences.getPrefs(context, Constants.OfferID));
        Log.d("RemoveProduct", "removeProductFromOffer: "+product_id);

        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.deleteProductOfferMapping(
                Shared_Preferences.getPrefs(context, Constants.REG_ID),
                Shared_Preferences.getPrefs(context, Constants.OfferID),
                product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    boolean result=jsonObject.getBoolean("result");
                    String message=jsonObject.getString("reason");
                    if(result)
                    {
                        productDataList.remove(position);
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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
        @POST(MyConfig.SSWORLD+"/deleteProductOfferMapping")
        Call<ResponseBody> deleteProductOfferMapping(
                @Field("user_id") String user_id,
                @Field("offer_id") String offer_id,
                @Field("product_id") String product_id
        );

    }
}
