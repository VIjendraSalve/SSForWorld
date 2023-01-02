package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.windhans.client.forworld.Adapter.MyBalanceAdapter;
import com.windhans.client.forworld.Model.MyWallet;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.view.View.GONE;

public class Activity_My_Balance extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recycler_view_transaction;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRetry;
    private ProgressDialog progressDialog;
    ArrayList<MyWallet> earningModelArrayList;
    MyBalanceAdapter myBalanceAdapter;
    private int page_count = 1, remainingCount = 0;
    MyTextView_Poppins_Bold txt_total_balance;
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;
    private LinearLayout ll_main_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my__balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.mybalance));

      /*  imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);
        ll_main_class = findViewById(R.id.ll_main_class);
        ll_main_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_My_Balance.this, ActivityProductList.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
      /*  btn_sign_in3=findViewById(R.id.btn_sign_in3);
        btn_sign_in1=findViewById(R.id.btn_sign_in1);
        btn_sign_in2=findViewById(R.id.btn_sign_in);

        btn_sign_in1.setRotation(-30f);
        btn_sign_in2.setRotation(140f);
        btn_sign_in3.setRotation(-30f);*/
        recycler_view_transaction=findViewById(R.id.recycler_view_transaction);
        recycler_view_transaction.setNestedScrollingEnabled(true);
        txt_total_balance=findViewById(R.id.txt_total_balance);
        //getEarningData();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        getMyCartData();
        check_connection();
    }
    
    public void check_connection() {
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(this.getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);


        recycler_view_transaction = findViewById(R.id.recycler_view_transaction);


        earningModelArrayList = new ArrayList<>();
        myBalanceAdapter = new MyBalanceAdapter(this, earningModelArrayList);
         //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recycler_view_transaction.setLayoutManager(mLayoutManager);
        recycler_view_transaction.setItemAnimator(new DefaultItemAnimator());
        recycler_view_transaction.setAdapter(myBalanceAdapter);

        recycler_view_transaction.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && earningModelArrayList.size() > 4) {
                        if (remainingCount > 0) {
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getEarningData();
                        }
                    }
                }
            }
        });

        earningModelArrayList.clear();
        myBalanceAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getEarningData();


    }

    private void getEarningData() {
        earningModelArrayList.clear();
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.getData(
                Shared_Preferences.getPrefs(Activity_My_Balance.this, Constants.REG_ID));

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("Vijendra", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        if(result)
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("myAllTransaction");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                earningModelArrayList.add(new MyWallet(object));
                              //  total_balace= total_balace+Integer.parseInt(object.getString("amount"));

                            }
                            txt_total_balance.setText(Constants.rs+""+(jsonObject.getString("walletBalance")));
                        }
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        recycler_view_transaction.setAdapter(myBalanceAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        myBalanceAdapter.notifyDataSetChanged();
                        if (earningModelArrayList != null && !earningModelArrayList.isEmpty() && !earningModelArrayList.equals("null")) {

                            noRecordLayout.setVisibility(GONE);

                        } else {

                            noRecordLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
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
    public void onClick(View view) {
        if (view.getId() == R.id.btnRetry) {
            check_connection();
        }
    }

    @Override
    public void onRefresh() {
        if (earningModelArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view_transaction.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                earningModelArrayList.clear();
                myBalanceAdapter.notifyDataSetChanged();
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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/myWallet")
        Call<ResponseBody> getData(
                @Field("user_id") String user_id

        );

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        if(Shared_Preferences.getPrefs(Activity_My_Balance.this, Constants.REG_TYPE).equals("2")){
            menuItem.setVisible(false);
        }else {
            menuItem.setVisible(true);
        }

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                Intent intent = new Intent(Activity_My_Balance.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
               /* Intent intent = new Intent(Activity_My_Balance.this, Activity_My_Cart.class);
                startActivity(intent);*/
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void getMyCartData() {
        Activity_My_Cart.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(Activity_My_Cart.GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(Activity_My_Balance.this, Constants.REG_ID);
        Log.d("user_id", "getMyCartData: "+user_id);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyCart(user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("ResponseCart", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if(result)
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("cart_data");
                            JSONArray array=jsonObject1.getJSONArray("product");
                            Log.d("Badge", "onResponse: "+array.length());
                            mCartItemCount = array.length();
                            setupBadge();
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
    
}
