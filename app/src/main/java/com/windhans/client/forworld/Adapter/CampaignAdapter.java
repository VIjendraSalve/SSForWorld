package com.windhans.client.forworld.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Activities.Activity_Campaign;
import com.windhans.client.forworld.Activities.CampaignDetailActivity;
import com.windhans.client.forworld.Activities.UpdateCapanignActivity;
import com.windhans.client.forworld.Model.CampaignModel;
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

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.MyViewHolder>{
    Context context;
    List<CampaignModel> campaignModelList;
    View view;
    String campaign_id,action,user_id,ads_unit_id,from_date,to_date,ad_banner,is_enable_enquiry,is_locationbased_enquiry,title,description,search,business_id;
    int page_no;

    public CampaignAdapter(Activity_Campaign activity_campaign, List<CampaignModel> campaignModelList) {
        this.context=activity_campaign;
        this.campaignModelList=campaignModelList;
    }

    @NonNull
    @Override
    public CampaignAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.compaign_list, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignAdapter.MyViewHolder myViewHolder, int position) {
        CampaignModel model=campaignModelList.get(position);
        myViewHolder.txt_title.setText(model.getTitle());
        myViewHolder.txt_description.setText(model.getDescription());
      /* String date_from=DateTimeFormat.getDate(model.getFrom_date());
       String date_tom=DateTimeFormat.getDate(model.getTo_date());*/

        myViewHolder.txt_from.setText(model.getFrom_date());
        myViewHolder.txt_to.setText(model.getTo_date());
        Log.d("date", "onMenuItemClick: "+model.getFrom_date()+","+model.getTo_date());

        if (model.getAd_banner() != null && !model.getAd_banner().isEmpty() && !model.getAd_banner().equals("null")) {
            Log.d("image", "response: ");

            Glide.with(context)
                    .load(MyConfig.JSON_BusinessImage + model.getAd_banner())
                    .into(myViewHolder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(myViewHolder.imageView);
        }
    myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context,CampaignDetailActivity.class);
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
                        Intent intent=new Intent(context,UpdateCapanignActivity.class);
                        context.startActivity(intent);
                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_TITLE,campaignModelList.get(position).getTitle());
                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_DECRIPTION,campaignModelList.get(position).getDescription());
                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_BANNER,campaignModelList.get(position).getAd_banner());
                        String from_date=campaignModelList.get(position).getFrom_date();
                        String to_date=campaignModelList.get(position).getTo_date();

                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_FROM_DATE,from_date);
                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_TO_DATE,to_date);
                        Shared_Preferences.setPrefs(context,Constants.IS_ENABLE_ENQUIRY,campaignModelList.get(position).getIs_enable_enquiry());
                        Shared_Preferences.setPrefs(context,Constants.IS_LOCATION_BASED_ENQUIRY,campaignModelList.get(position).getIs_locationbased_enquiry());
                        Shared_Preferences.setPrefs(context,Constants.CAMPAIGN_ID,campaignModelList.get(position).getId());
                        Shared_Preferences.setPrefs(context,Constants.BUSINESS_ID,campaignModelList.get(position).getBusiness_id());
                    }
                    else
                    {
                        deleteCampaign(position);
                    }
                    Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();
        }
    });

    }

    private void deleteCampaign(int position) {
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        campaign_id=campaignModelList.get(position).getId();
        action="delete";
        user_id= Shared_Preferences.getPrefs(context, Constants.REG_ID);
        business_id=campaignModelList.get(position).getBusiness_id();
        Log.d("ids", "deleteCampaign: "+user_id+","+business_id+","+campaign_id);
        ads_unit_id="";
        from_date="";
        to_date="";
        ad_banner="";
        is_enable_enquiry="";
        is_locationbased_enquiry="";
        title="";
        description="";
        search="";

        Call<ResponseBody> responseBodyCall=getOrderApi.deleteData(campaign_id,action,user_id,business_id,ads_unit_id,from_date,to_date,ad_banner,is_enable_enquiry,is_locationbased_enquiry,title,description,search,page_no);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject object=new JSONObject(output);
                    boolean result=object.getBoolean("status");
                    String message=object.getString("message");
                    if(result)
                    {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
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


    @Override
    public int getItemCount() {
        return campaignModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title,txt_description,txt_from,txt_to;
        ImageView imageView,imageView_menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title=itemView.findViewById(R.id.txt_title);
            txt_description=itemView.findViewById(R.id.txt_description);
            txt_from=itemView.findViewById(R.id.txt_from);
            txt_to=itemView.findViewById(R.id.txt_to);

            imageView=itemView.findViewById(R.id.imageView);
            imageView_menu=itemView.findViewById(R.id.imageView_menu);

        }
    }

    public interface GetOrderApi
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/campaignMaster")
        Call<ResponseBody> deleteData(
                @Field("campaign_id") String campaign_id,
                @Field("action") String action,
                @Field("user_id") String user_id,
                @Field("business_id") String business_id,
                @Field("ads_unit_id") String ads_unit_id,
                @Field("from_date") String from_date,
                @Field("to_date") String to_date,
                @Field("ad_banner") String ad_banner,
                @Field("is_enable_enquiry") String is_enable_enquiry,
                @Field("is_locationbased_enquiry") String is_locationbased_enquiry,
                @Field("title") String title,
                @Field("description") String description,
                @Field("search") String search,
                @Field("page_no") int page_no

        );
    }
}


