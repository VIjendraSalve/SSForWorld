package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.Adapter.MyFavoriteAdapter;
import com.windhans.client.forworld.Model.FavoriteModel;
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

public class ActivityMyFavorite extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyFavoriteAdapter.AdapterCallback {

    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private Handler mHandler;
    RecyclerView recycler_view_favorite;
    MyFavoriteAdapter myFavoriteAdapter;
   // List<NewProduct> favoriteModelList=new ArrayList<>();
    List<FavoriteModel> favoriteModelList=new ArrayList();
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farorite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.myfavorite));

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


        recycler_view_favorite = findViewById(R.id.recycler_view_favorite);
        favoriteModelList = new ArrayList();
        myFavoriteAdapter = new MyFavoriteAdapter(this, favoriteModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view_favorite.setLayoutManager(mLayoutManager);
        recycler_view_favorite.setItemAnimator(new DefaultItemAnimator());
        recycler_view_favorite.setAdapter(myFavoriteAdapter);

        recycler_view_favorite.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && favoriteModelList.size() > 4) {
                        if (remainingCount > 0) {
                            Log.d("data", "onScrollStateChanged: " + remainingCount);
                            Log.d("data", "onScrollStateChanged: " + page_count);
                            Log.d("data", "onScrollStateChanged: " + lastVisibleItemPosition);
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                           getMyFavroiteDate();

                        }
                    }
                }
            }
        });

        favoriteModelList.clear();
        myFavoriteAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getMyFavroiteDate();



    }
    
    @Override
    public void onResume() {
        super.onResume();
        getMyCartData();
        check_connection();
    }

    private void getMyFavroiteDate() {
        progressDialog.show();
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String reg_id= Shared_Preferences.getPrefs(ActivityMyFavorite.this, Constants.REG_ID);
        Log.d("reg", "getMyFavroiteDate: "+reg_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.getData(reg_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("reg", "getMyFavroiteList: "+output);
                    try {
                        progressDialog.dismiss();
                        JSONObject object=new JSONObject(output);
                        boolean result=object.getBoolean("result");
                        Log.d("favorite", "onResponse: "+result);
                        String reason=object.getString("reason");
                        if(result)
                        {
                            JSONArray array=object.getJSONArray("favouriteList");
                         //   JSONArray array=object.getJSONArray("product_details");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject jsonObject=array.getJSONObject(i);
                                favoriteModelList.add(new FavoriteModel(jsonObject));

                            }
                         //   myFavoriteAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(ActivityMyFavorite.this, reason, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        recycler_view_favorite.setAdapter(myFavoriteAdapter);
                        myFavoriteAdapter.notifyDataSetChanged();

                        if (favoriteModelList != null && !favoriteModelList.isEmpty() && !favoriteModelList.equals("null")) {

                            noRecordLayout.setVisibility(GONE);

                        } else {

                            noRecordLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    
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
    public void onRefresh() {
        if (favoriteModelList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view_favorite.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                favoriteModelList.clear();
                myFavoriteAdapter.notifyDataSetChanged();
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
       onRefresh();
      //  getMyFavroiteDate();
    }

    public interface GetOrderApi {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/getFavourites")
        Call<ResponseBody> getData(
                @Field("user_id") String user_id
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        if(Shared_Preferences.getPrefs(ActivityMyFavorite.this, Constants.REG_TYPE).equals("2")){
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
                Intent intent = new Intent(ActivityMyFavorite.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
               /* Intent intent = new Intent(ActivityMyFavorite.this, Activity_My_Cart.class);
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
        String user_id= Shared_Preferences.getPrefs(ActivityMyFavorite.this, Constants.REG_ID);
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
