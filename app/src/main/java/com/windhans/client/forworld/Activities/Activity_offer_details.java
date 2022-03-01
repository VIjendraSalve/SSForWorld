package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Adapter.ProductDataAdapter;
import com.windhans.client.forworld.Model.ProductData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class Activity_offer_details extends AppCompatActivity {
    ImageView imageView;
    MyTextView_Poppins_Regular txt_offerName,txt_offerPrice,txt_offerDate,txt_offerCode,txt_offerDetails;
    Button btn_applyNow;
    String offerId,offerName,validity,details,offer_price,terms,image1,o_code;
    ProgressDialog progressDialog;
    RecyclerView recycleView_ProductData;
    List<ProductData> productDataList=new ArrayList<>();
    ProductDataAdapter productDataAdapter;
    ImageView imageview_expand;
    int flag=1;
    private LinearLayout ll_product_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.offersdetails));

        progressDialog=new ProgressDialog(Activity_offer_details.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        recycleView_ProductData=findViewById(R.id.recycleView_ProductData);
        ll_product_details=findViewById(R.id.ll_product_details);

        getViews();
        getOfferDeatils();

        imageview_expand.setImageDrawable(Activity_offer_details.this.getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        ll_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==1)
                {
                    recycleView_ProductData.setVisibility(View.VISIBLE);
                    imageview_expand.setImageDrawable(Activity_offer_details.this.getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
                    flag=0;
                }
                else {
                    recycleView_ProductData.setVisibility(View.GONE);
                    imageview_expand.setImageDrawable(Activity_offer_details.this.getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                    flag=1;
                }
            }
        });
        btn_applyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_offer_details.this,Activity_My_Product.class);
                startActivity(intent);
            }
        });
    }

    private void getOfferDeatils() {
        progressDialog.show();
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String userID=Shared_Preferences.getPrefs(Activity_offer_details.this,Constants.REG_ID);
        String offer_id=Shared_Preferences.getPrefs(Activity_offer_details.this,Constants.OfferID);
        Log.d("ids", "getOfferDeatils: "+userID+","+offer_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.getDetails(userID,offer_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject object=new JSONObject(output);
                    Log.d("output", "onResponse: "+object);
                    boolean result=object.getBoolean("result");
                    String message=object.getString("reason");
                    if (result)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_offer_details.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=object.getJSONObject("offersData");
                        Shared_Preferences.setPrefs(Activity_offer_details.this,Constants.OfferID,jsonObject.getString("id"));
                        offerId=jsonObject.getString("id");
                         offerName=jsonObject.getString("name");
                         validity=jsonObject.getString("validity");
                         details=jsonObject.getString("description");
                         offer_price=jsonObject.getString("offer_price");
                         terms=jsonObject.getString("terms");
                         image1=jsonObject.getString("image");
                         o_code=jsonObject.getString("offer_code");
                         setValues();

                        JSONArray jsonArray=jsonObject.getJSONArray("products");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            productDataList.add(new ProductData(jsonObject1));
                        }
                        productDataAdapter=new ProductDataAdapter(Activity_offer_details.this,productDataList);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Activity_offer_details.this);
                        recycleView_ProductData.setLayoutManager(linearLayoutManager);
                        recycleView_ProductData.setItemAnimator(new DefaultItemAnimator());
                        recycleView_ProductData.setAdapter(productDataAdapter);
                        productDataAdapter.notifyDataSetChanged();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_offer_details.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setValues() {

        txt_offerName.setText(offerName);
        txt_offerDate.setText(validity);
        txt_offerCode.setText(o_code);
        txt_offerDetails.setText(details);
        txt_offerPrice.setText(Constants.rs+" " +offer_price);
        if(!image1.equals("null")&&!image1.isEmpty()&& image1 != null)
        {
            Glide
                    .with(Activity_offer_details.this)
                    .load(MyConfig.JSON_BusinessOffers+image1)
                    .into(imageView);
        }
      else {
            Glide
                    .with(Activity_offer_details.this)
                    .load(R.drawable.no_image_available)

                    .into(imageView);
        }
    }

    private void getViews() {
        imageView=findViewById(R.id.imageView);
        txt_offerName=findViewById(R.id.txt_offerName);
        txt_offerPrice=findViewById(R.id.txt_offerPrice);
        txt_offerDate=findViewById(R.id.txt_offerDate);
        txt_offerCode=findViewById(R.id.txt_offerCode);
        txt_offerDetails=findViewById(R.id.txt_offerDetails);
        btn_applyNow=findViewById(R.id.btn_applyNow);
        imageview_expand=findViewById(R.id.imageview_expand);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }


       /* if (item.getItemId() == R.id.reply_thread) {
            ReplayDialogue();
        }*/
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }

    public interface GetOrderApi
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/offerDetailsForVendor")
        Call<ResponseBody> getDetails(
                @Field("user_id") String userid,
                @Field("offer_id") String offer_id

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.edit);
        myActionMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                updateOfferDetails();
                return false;
            }
        });
        return true;
    }

    private void updateOfferDetails() {
        Intent intent=new Intent(Activity_offer_details.this,Update_Offer_Detail.class);
        intent.putExtra( Constants.OfferIDForUpdate, offerId);
        startActivity(intent);
    }
}
