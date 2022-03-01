package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
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

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.OfferListAdapter;
import com.windhans.client.forworld.Model.OfferModel;
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

public class ActivityOfferListing extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recycler_view_offer;
    FloatingActionButton fab_create_offer;
    List<OfferModel> offerModelList=new ArrayList<>();
    OfferListAdapter offerListAdapter;

    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private String query = "";
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_listing);
        fab_create_offer = findViewById(R.id.fab_create_offer);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.offers));

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
        check_connection();
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

        recycler_view_offer = findViewById(R.id.recycler_view_offer);
        offerModelList = new ArrayList<>();
        offerListAdapter = new OfferListAdapter(this, offerModelList);
    //    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycler_view_offer.setLayoutManager(gridLayoutManager);
        recycler_view_offer.setItemAnimator(new DefaultItemAnimator());
        recycler_view_offer.setAdapter(offerListAdapter);


        recycler_view_offer.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recycler_view_offer.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != recyclerView.getAdapter().getItemCount() - 1 && offerModelList.size() > 4) {
                        if (remainingCount > 0) {

                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getOfferData();
                        }
                    }
                }
            }
        });
        offerModelList.clear();
        getOfferData();
        offerListAdapter.notifyDataSetChanged();
    }

    private void getOfferData() {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String userID = Shared_Preferences.getPrefs(ActivityOfferListing.this, Constants.REG_ID);
        Log.d("ids", "getOfferData: "+userID);
        Call<ResponseBody> responseBodyCall = getOrderApi.getOfferData(userID,
                page_count,
                query
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("OfferList", "onResponse: "+output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean result = jsonObject.getBoolean("result");
                    //remainingCount = jsonObject.getInt("remaining");
                    String message = jsonObject.getString("reason");
                    if (result) {
                        JSONArray array = jsonObject.getJSONArray("offerData");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            offerModelList.add(new OfferModel(object));
                        }

                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        //recyclerView.setAdapter(myProductAdapter);
                        offerListAdapter.notifyDataSetChanged();
                        if (offerModelList.size() > 0) {
                            noRecordLayout.setVisibility(GONE);
                        } else {

                            noRecordLayout.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        swipeRefreshLayout.setVisibility(GONE);
                        noRecordLayout.setVisibility(View.VISIBLE);
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
    public void onRefresh() {
        if (offerModelList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view_offer.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                offerModelList.clear();
                offerListAdapter.notifyDataSetChanged();
                //page_count = dash1;count = dash1;
                page_count = 1;
                remainingCount = 0;

                check_connection();
                progressBar_endless.setVisibility(GONE);
                progressView.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);
                //swipe=true;            }
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
    }
    public interface GetOrderApi
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/offerListingForVendor")
        Call<ResponseBody> getOfferData(
                @Field("user_id") String user_id,
                @Field("page_no") int page_no,
                @Field("search") String search
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


       /* if (item.getItemId() == R.id.reply_thread) {
            ReplayDialogue();
        }*/
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }
}
