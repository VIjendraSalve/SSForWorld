package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.MyProductAdapter1;
import com.windhans.client.forworld.Model.VendorProductData;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.view.View.GONE;

public class Activity_My_Product extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    List<VendorProductData> vendorProductDataList;
    MyProductAdapter1 myProductAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private String query = "";
    private Handler mHandler;
    private ImageView imageView;
    private String search;
    private String s;
    private SearchView mSearchView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    Button btn_done;
    String ids1 = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        myActionMenuItem.setVisible(false);
        mSearchView = (SearchView) myActionMenuItem.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("my_tag", "onQueryTextChange: " + newText);
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        query = newText;
                        vendorProductDataList.clear();
                        myProductAdapter.notifyDataSetChanged();
                        page_count = 1;
                        remainingCount = 0;
                        progressView.setVisibility(View.VISIBLE);
                        //getOrganiserData();
                        getMyProductData();

                    }
                }, 300);
                return false;
            }
        });
        return true;

    }

    FloatingActionButton fab;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my__product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.myproduct));

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
      /*  fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_My_Product.this,AddMyProduct.class);
                startActivity(intent);
            }
        });*/


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ProductList"));


        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOfferApplyDetails(stringArrayList);
            }
        });

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            stringArrayList = intent.getStringArrayListExtra("productIDList");
            Log.d("list", "onReceive: " + stringArrayList.size());
            ids1 = "";
            for (int i = 0; i < stringArrayList.size(); i++) {

                    Log.d("i", "onReceive: " + i + "," + stringArrayList.get(i));

                    ids1 = ids1 + "," + stringArrayList.get(i);


            }

            Log.d("ids1", "string: " + ids1);
        }
    };

    private void submitOfferApplyDetails(ArrayList<String> stringArrayList) {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id = Shared_Preferences.getPrefs(Activity_My_Product.this, Constants.REG_ID);
        String offer_id = Shared_Preferences.getPrefs(Activity_My_Product.this, Constants.OfferID);
        Log.d("ids", "submitOfferApplyDetails: " + user_id + "," + offer_id + "," + ids1);
        String ids2 = "";
        if (!ids1.equals("null") || !ids1.isEmpty() || ids1 != null) {
            ids2 = ids1.substring(1);
        }

        Log.d("ids", "submitOfferApplyDetails: " + user_id + "," + offer_id + "," + ids2);
        Call<ResponseBody> responseBodyCall = getOrderApi.submitDetails(user_id,
                offer_id,
                ids2
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>(

        ) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject object = new JSONObject(output);
                    boolean result = object.getBoolean("result");
                    String message = object.getString("reason");
                    if (result) {
                        Toast.makeText(Activity_My_Product.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_My_Product.this, ActivityOfferListing.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Activity_My_Product.this, message, Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            check_connection();
        }
    }

    @Override
    public void onRefresh() {
        if (vendorProductDataList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                vendorProductDataList.clear();
                myProductAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
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
        mHandler = new Handler();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);


        recyclerView = findViewById(R.id.recycler_view);


        vendorProductDataList = new ArrayList<>();
        myProductAdapter = new MyProductAdapter1(Activity_My_Product.this, vendorProductDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myProductAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && vendorProductDataList.size() > 4) {
                        if (remainingCount > 0) {
                            Log.d("data", "onScrollStateChanged: " + remainingCount);
                            Log.d("data", "onScrollStateChanged: " + page_count);
                            Log.d("data", "onScrollStateChanged: " + lastVisibleItemPosition);
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getMyProductData();
                        }
                    }
                }
            }
        });

        vendorProductDataList.clear();
        myProductAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getMyProductData();

    }

    private void getMyProductData() {

        String user_id = Shared_Preferences.getPrefs(this, Constants.REG_ID);
        Log.d("ids", "getMyProductData: " + user_id);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyData(
                user_id,
                page_count,
                query
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("Data", "onResponse: " + output);
                    Log.d("Data", "onResponse: " + user_id);
                    Log.d("Data", "onResponse: " + page_count);
                    Log.d("Data", "onResponse: " + query);
                    JSONObject jsonObject = new JSONObject(output);

                    boolean result = jsonObject.getBoolean("result");

                    if (result) {
                        remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        Log.d("data", "Remaining count onResponse: " + remainingCount);
                        JSONArray jsonArray = jsonObject.getJSONArray("productData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            vendorProductDataList.add(new VendorProductData(object));
                            // Shared_Preferences.setPrefs(Activity_My_Product.this,Constants.SUB_ID,object.getString("subcategory_id"));
                            //Log.d("sub", "onResponse: "+object.getString("subcategory_id"));

                        }


                    }
                    progressDialog.dismiss();
                    progressView.setVisibility(GONE);
                    //recyclerView.setAdapter(myProductAdapter);
                    myProductAdapter.notifyDataSetChanged();
                    if (vendorProductDataList != null && !vendorProductDataList.isEmpty() && !vendorProductDataList.equals("null")) {

                        noRecordLayout.setVisibility(GONE);

                    } else {

                        noRecordLayout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
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

    public interface GetOrderApi {
       /* @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/allProductsByVendor")
        Call<ResponseBody> getMyData(
                @Field("user_id") String user_id,
                @Field("page_no") int page_no,
                @Field("search") String search
        );*/

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getAllOfferProductByVendor")
        Call<ResponseBody> getMyData(
                @Field("user_id") String user_id,
                @Field("page_no") int page_no,
                @Field("search") String search
        );


        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/assignOffer")
        Call<ResponseBody> submitDetails(
                @Field("user_id") String user_id,
                @Field("offer_id") String offer_id,
                @Field("product_ids") String ids
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
