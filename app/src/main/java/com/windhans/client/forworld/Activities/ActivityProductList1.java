package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.ProductsListAdapter;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.Model.ProductList;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ActivityProductList1 extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private ArrayList<NewProduct> productList = new ArrayList<>();
    private List<ProductList> filterdpatientList = new ArrayList<>();
    private List<BuisnessModel> buisnessModelList;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ProductsListAdapter mAdapter;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private Camera camera;
    private ImageView profie_imageView;
    private SearchView mSearchView,et_search;
    private String query = "";
    private Handler mHandler;
    private View retView;
    private ProductList product;
    FloatingActionButton fab_add_post;
    private String s;
    private ImageView imageView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        EditText searchEditText = (EditText) mSearchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("my_tag", "onQueryTextChange: " +s);
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        query = newText;
                        productList.clear();
                        mAdapter.notifyDataSetChanged();
                        page_count = 1;
                        remainingCount = 0;
                        progressView.setVisibility(View.VISIBLE);
                        //getOrganiserData();
                        getProductData();

                    }
                }, 300);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

       // setContentView(R.layout.activity_buisness_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


     /*   imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.order));

        //getSupportActionBar().setTitle(""+ Shared_Preferences.getPrefs(ProductListActivity.this, "name"));



        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout =findViewById(R.id.noConnectionLayout);
        btnRetry =findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout =findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
    }



    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
//            noConnectionLayout.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            init();
        } else {
          /*  Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.internet_not_avilable,
                    Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    v -> check_connection()).show();*/
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }
    private void init() {
        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);


        recyclerView=findViewById(R.id.recycler_view);

        buisnessModelList=new ArrayList<>();
        mAdapter = new ProductsListAdapter(productList);
       // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && productList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            // progressBar_endless.setVisibility(View.VISIBLE);
                            getProductData();
                        }
                    }
                }
            }
        });

        productList.clear();
        mAdapter.notifyDataSetChanged();
        //progressView.setVisibility(View.VISIBLE);
        getProductData();
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
        if (productList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                productList.clear();
                mAdapter.notifyDataSetChanged();
                //page_count = dash1;count = dash1;
                page_count = 1;
                remainingCount = 0;
                //swipe=false;
                //getOrderList();
                check_connection();
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                //swipe=true;
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;
            //get_my_rides(2);
            check_connection();
            progressBar_endless.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        check_connection();
    }



    private void getProductData() {
        progressDialog.show();
        productList.clear();
        String user_id= Shared_Preferences.getPrefs(ActivityProductList1.this, Constants.REG_ID);
        String buisness_id= Shared_Preferences.getPrefs(ActivityProductList1.this, Constants.BUSINESS_ID);
        String subcategory=   Shared_Preferences.getPrefs(ActivityProductList1.this, Constants.SUBCATEGORY);

        Log.d("Data", "getMyFriendData: "+user_id+","+subcategory);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getRecommended(
                Shared_Preferences.getPrefs(ActivityProductList1.this, Constants.REG_ID),
                Shared_Preferences.getPrefs(ActivityProductList1.this, Constants.BUSINESS_ID),
                query,
                page_count);


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                       // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("productData");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                productList.add(new NewProduct(obj));
                                //Log.d("Buisness", "onResponse: "+obj.getString("business_id"));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                    else {

                    }

                    progressView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);


                    if (productList.isEmpty()) {
                        noRecordLayout.setVisibility(View.VISIBLE);
                    } else {
                        noRecordLayout.setVisibility(View.GONE);
                    }
                    progressView.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/productListingByBusiness")
        public Call<ResponseBody> getRecommended(
                @Field("user_id") String user_id,
                @Field("business_id") String category_id,
                @Field("search") String search,
                @Field("page_no") int page_no
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}


