package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import com.windhans.client.forworld.Adapter.SubServicesAdapter;
import com.windhans.client.forworld.Model.SubService;
import com.windhans.client.forworld.R;
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

public class ActivitySubServices extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private int page_count;
    private RecyclerView recycler_view;
    private Handler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<SubService> subServiceArrayList;
    private SubServicesAdapter subCategoryAdapter;
    private int remainingCount;
    private ImageView imageView;
    private String serviceId = "", serviceName = "";
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        // setContentView(R.layout.activity_buisness_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        serviceId = getIntent().getStringExtra(Constants.ServiceId);
        serviceName = getIntent().getStringExtra(Constants.ServiceName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("" + serviceName);

        progressView = (ProgressBar) findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) findViewById(R.id.progressBar_endless);

        recycler_view = findViewById(R.id.recycler_view);

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
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
            page_count = 1;
            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
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
        progressDialog.setCancelable(true);

        recycler_view = findViewById(R.id.recycler_view);


        subServiceArrayList = new ArrayList<>();
        subCategoryAdapter = new SubServicesAdapter(this, subServiceArrayList);
        //  RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 1);
        recycler_view.setLayoutManager(mLayoutManager2);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(subCategoryAdapter);


        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && subServiceArrayList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            getSubserviceList();
                        }
                    }
                }
            }
        });


        subServiceArrayList.clear();
        subCategoryAdapter.notifyDataSetChanged();
        getSubserviceList();
    }

    private void getSubserviceList() {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.getSubServices(
                Shared_Preferences.getPrefs(this, Constants.REG_ID),
                serviceId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    Log.d("SubcategoryList", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            JSONArray jsonArray = jsonObject.getJSONArray("subCategories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                subServiceArrayList.add(new SubService(object));

                            }
                        }
                        progressDialog.dismiss();
                        progressView.setVisibility(View.GONE);
                        recycler_view.setAdapter(subCategoryAdapter);
                        subCategoryAdapter.notifyDataSetChanged();

                        if (subServiceArrayList != null && !subServiceArrayList.isEmpty() && !subServiceArrayList.equals("null")) {

                            noRecordLayout.setVisibility(View.GONE);

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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getServiceSubCategoryListing")
        Call<ResponseBody> getSubServices(
                @Field("user_id") String user_id,
                @Field("category_id") String category_id
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            //orderList.clear();
            check_connection();
        }
    }

    @Override
    public void onRefresh() {
        if (subServiceArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                subServiceArrayList.clear();
                subCategoryAdapter.notifyDataSetChanged();

                page_count = 1;
                remainingCount = 0;

                check_connection();
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;
            check_connection();
            progressBar_endless.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);

        }
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

        if(Shared_Preferences.getPrefs(ActivitySubServices.this, Constants.REG_TYPE).equals("2")){
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
                Intent intent = new Intent(ActivitySubServices.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
               onBackPressed();
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
        String user_id= Shared_Preferences.getPrefs(ActivitySubServices.this, Constants.REG_ID);
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
