package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.windhans.client.forworld.Adapter.MyProductAdapter;
import com.windhans.client.forworld.Model.ProductDataForBill;
import com.windhans.client.forworld.Model.VendorProductData;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.view.View.GONE;

public class BillActivity extends AppCompatActivity {

    MyTextView_Poppins_Regular tv_vendor_name, tv_vendor_mobile, tv_vendor_address, tv_user_name, tv_user_mobile,
            tv_user_address, tv_total_amount, tv_amt_from_wallet, tv_remaining_amount, tv_remaining_amount_paid_type;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<ProductDataForBill> productDataForBillArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.bill));

        init();
    }

    private void init() {

        tv_vendor_name = findViewById(R.id.tv_vendor_name);
        tv_vendor_mobile = findViewById(R.id.tv_vendor_mobile);
        tv_vendor_address = findViewById(R.id.tv_vendor_address);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_mobile = findViewById(R.id.tv_user_mobile);
        tv_user_address = findViewById(R.id.tv_user_address);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_amt_from_wallet = findViewById(R.id.tv_amt_from_wallet);
        tv_remaining_amount = findViewById(R.id.tv_remaining_amount);
        tv_remaining_amount_paid_type = findViewById(R.id.tv_remaining_amount_paid_type);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);


        recyclerView = findViewById(R.id.recycler_view);


        productDataForBillArrayList = new ArrayList<>();
        //myProductAdapter = new MyProductAdapter(this, productDataForBillArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(myProductAdapter);

        productDataForBillArrayList.clear();
        //myProductAdapter.notifyDataSetChanged();
        getMyProductData();

    }

    private void getMyProductData() {

        String user_id = Shared_Preferences.getPrefs(this, Constants.REG_ID);
        Log.d("user_id", "getMyProductData: "+user_id);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyData(
                "156",
                "3"

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);

                    boolean result = jsonObject.getBoolean("result");

                    if (result) {

                        JSONArray jsonArray = jsonObject.getJSONArray("productData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //productDataForBillArrayList.add(new VendorProductData(object));
                            // Shared_Preferences.setPrefs(ActivityMyProduct.this, Constants.SUB_ID, object.getString("subcategory_id"));
                            //Log.d("sub", "onResponse: " + object.getString("subcategory_id"));

                        }


                    }
                    progressDialog.dismiss();
                   // progressView.setVisibility(GONE);
                    //recyclerView.setAdapter(myProductAdapter);
                   // myProductAdapter.notifyDataSetChanged();
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
        @POST(MyConfig.SSWORLD + "/getBillData")
        Call<ResponseBody> getMyData(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id
               
        );
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
}
