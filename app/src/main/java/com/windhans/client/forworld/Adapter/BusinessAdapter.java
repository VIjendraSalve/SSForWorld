package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.windhans.client.forworld.Activities.ActivityBusinessDetailFromHomePage;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {
    Context context;
    List<BuisnessModel> vendorProductDataList;
    View view;

    private static ImagePipelineFactory sInstance = null;
    private ImagePipelineConfig imagePipelineConfig;


    public BusinessAdapter(Context activityMyProduct, List<BuisnessModel> vendorProductDataList) {
        this.context = activityMyProduct;
        this.vendorProductDataList = vendorProductDataList;
    }

    @NonNull
    @Override
    public BusinessAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.buisness_list1, viewGroup, false);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.business_listing_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessAdapter.MyViewHolder myViewHolder, int position) {
        BuisnessModel model = vendorProductDataList.get(position);
        myViewHolder.txt_business_name.setText(model.getBusiness_name());
        myViewHolder.txt_business_product_count.setText(model.getCity() );
        //  myViewHolder.txt_description.setText(model.getDescription());
        //    myViewHolder.txt_price.setText(Constants.rs+""+model.getPrice());
        DisplayMetrics metricscard = context.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;

        int cardheight = (int) (metricscard.heightPixels);
        myViewHolder.imageView_businessBanner.getLayoutParams().width = (int) (cardwidth / 1.4);
        myViewHolder.imageView_businessBanner.getLayoutParams().height = (int) (cardheight / 5);
        if (model.getBusiness_banner() != null && !model.getBusiness_banner().isEmpty() && !model.getBusiness_banner().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessPath + model.getBusiness_banner())

                    .into(myViewHolder.imageView_businessBanner);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)

                    .into(myViewHolder.imageView_businessBanner);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivityBusinessDetailFromHomePage.class);
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_IMAGE, vendorProductDataList.get(position).getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_IDNEW, vendorProductDataList.get(position).getUser_id());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_IDFORCART, vendorProductDataList.get(position).getUser_id());
                Shared_Preferences.setPrefs(context, Constants.BUISNESS_BANNER, vendorProductDataList.get(position).getBusiness_banner());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_NAME, vendorProductDataList.get(position).getBusiness_name());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DEC, vendorProductDataList.get(position).getDescription());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CONTACT, vendorProductDataList.get(position).getContact_mobile());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_EMAIL, vendorProductDataList.get(position).getEmail());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_ADDRESS, vendorProductDataList.get(position).getAddress());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_CITY, vendorProductDataList.get(position).getCity());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_DISTRICT, vendorProductDataList.get(position).getDistrict());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_STATE, vendorProductDataList.get(position).getState());
                Shared_Preferences.setPrefs(context, Constants.BUSINESS_PINCODE, vendorProductDataList.get(position).getPincode());
                Shared_Preferences.setPrefs(context, Constants.TOTAL_PRODUCT, vendorProductDataList.get(position).getTotal_product());
                context.startActivity(intent);
            }
        });


    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/deleteProduct")
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
        MyTextView_Poppins_Regular txt_business_name, txt_business_product_count;
        ImageView imageView_businessBanner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_business_name = itemView.findViewById(R.id.txt_business_name);
            txt_business_product_count = itemView.findViewById(R.id.txt_business_product_count);
            imageView_businessBanner = itemView.findViewById(R.id.imageView_businessBanner);

        }
    }
}

