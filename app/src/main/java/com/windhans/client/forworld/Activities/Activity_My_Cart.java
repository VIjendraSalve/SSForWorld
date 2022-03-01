package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.windhans.client.forworld.Adapter.MyCartAdapter;
import com.windhans.client.forworld.Model.MyCartModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.my_library.CheckNetwork;
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

import static android.view.View.GONE;

public class Activity_My_Cart extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyCartAdapter.AdapterCallback {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout, ll_cart;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private String query = "";
    private Handler mHandler;
    List<MyCartModel> myCartModelList = new ArrayList<>();
    MyCartAdapter myCartAdapter;
    Button btn_submit;
    String product_id_string,business_id, TotalAmount="";
    private MyTextView_Poppins_Bold tv_total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my__cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.mycart));

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        ll_cart = findViewById(R.id.ll_cart);
        progressBar_endless = findViewById(R.id.progressBar_endless);
        btn_submit=findViewById(R.id.btn_submit);
        tv_total_amount=findViewById(R.id.tv_total_amount);

        check_connection();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Activity_My_Cart.this,GenrateEnquiry.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            check_connection();
        }
    }

    private void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
            noConnectionLayout.setVisibility(GONE);
            noRecordLayout.setVisibility(GONE);
            init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        mHandler = new Handler();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);


        recyclerView = findViewById(R.id.recycler_view);


        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(Activity_My_Cart.this, myCartModelList);
     //   RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(Activity_My_Cart.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myCartAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && myCartModelList.size() > 4) {
                        if (remainingCount > 0) {
                            Log.d("data", "onScrollStateChanged: " + remainingCount);
                            Log.d("data", "onScrollStateChanged: " + page_count);
                            Log.d("data", "onScrollStateChanged: " + lastVisibleItemPosition);
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getMyCartData();
                        }
                    }
                }
            }
        });

        myCartModelList.clear();
        myCartAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getMyCartData();


    }

    private void getMyCartData() {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(Activity_My_Cart.this, Constants.REG_ID);
        Log.d("user_id", "getMyCartData: "+user_id);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyCart(user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("Response", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if(result)
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("cart_data");
                            TotalAmount = jsonObject1.getString("cartAmount");
                            tv_total_amount.setText(Constants.rs +" "+TotalAmount);
                            JSONArray array=jsonObject1.getJSONArray("product");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object=array.getJSONObject(i);
                                myCartModelList.add(new MyCartModel(object));
                                product_id_string="";

                            }
                        }
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        //recyclerView.setAdapter(myProductAdapter);
                        myCartAdapter.notifyDataSetChanged();
                        if (myCartModelList != null && !myCartModelList.isEmpty() && !myCartModelList.equals("null")) {

                            ll_cart.setVisibility(View.VISIBLE);
                            noRecordLayout.setVisibility(GONE);
                            for(int j=0;j<myCartModelList.size();j++)
                            {
                                //  product_id_string=myCartModelList.get(i).getId();
                                product_id_string+=myCartModelList.get(j).getProduct_id()+",";
                                business_id=myCartModelList.get(j).getBusiness_id();
                                Shared_Preferences.setPrefs(Activity_My_Cart.this,Constants.PRODUCT_ID_STRING, product_id_string);
                                Shared_Preferences.setPrefs(Activity_My_Cart.this,Constants.BUSINESS_ID1, business_id);
                            }
                        } else {
                            ll_cart.setVisibility(GONE);
                            noRecordLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }

                        if (myCartModelList.size()>0){
                            btn_submit.setVisibility(View.VISIBLE);
                        }else {
                            btn_submit.setVisibility(GONE);
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
    public void onRefresh() {
        if (myCartModelList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                myCartModelList.clear();
                myCartAdapter.notifyDataSetChanged();
                page_count = 1;
                remainingCount = 0;
                check_connection();
                progressBar_endless.setVisibility(GONE);
                progressView.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;

            check_connection();
            progressBar_endless.setVisibility(GONE);
            progressView.setVisibility(GONE);
            noRecordLayout.setVisibility(GONE);


        }
    }

    @Override
    public void onMethodCallback() {
        init();
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getCartItems")
        Call<ResponseBody> getMyCart(
                @Field("user_id") String user_id
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

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
