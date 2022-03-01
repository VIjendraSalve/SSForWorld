package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.Adapter.CampaignAdapter;
import com.windhans.client.forworld.Model.CampaignModel;
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

public class Activity_Campaign extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recycler_view;
    List<CampaignModel> campaignModelList=new ArrayList<>();
    CampaignAdapter campaignAdapter;
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
   String campaign_id,action,user_id,business_id,ads_unit_id,from_date,to_date,ad_banner,is_enable_enquiry,is_locationbased_enquiry,title,description;
    Button btn_create_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__campaign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Campaign");

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout =findViewById(R.id.noConnectionLayout);
        btnRetry =findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout =findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);

        btn_create_new=findViewById(R.id.btn_create_new);
        btn_create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activity_Campaign.this,Activity_Add_Campaign.class);
                startActivity(intent);
            }
        });
        check_connection();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("my_tag", "onQueryTextChange: " +newText);
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        query = newText;
                        campaignModelList.clear();
                        campaignAdapter.notifyDataSetChanged();
                        page_count = 1;
                        remainingCount = 0;
                        progressView.setVisibility(View.VISIBLE);

                        getCampaignData();

                    }
                }, 300);
                return false;
            }
        });
        return true;

    }

    private void getCampaignData() {
        progressDialog.show();
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        campaign_id="";
        action="get";
        user_id= Shared_Preferences.getPrefs(Activity_Campaign.this, Constants.REG_ID);
        Log.d("", "getCampaignData: ");
       // user_id="1";
        business_id="";
        ads_unit_id="";
        from_date="";
        to_date="";
        ad_banner="";
        is_enable_enquiry="";
        is_locationbased_enquiry="";
        title="";
        description="";

        Call<ResponseBody> responseBodyCall=getOrderApi.getCamaignData(campaign_id,action,user_id,business_id,ads_unit_id,from_date,to_date,ad_banner,is_enable_enquiry,is_locationbased_enquiry,title,description,search,page_count);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject object=new JSONObject(output);
                    Log.d("output", "onResponse: "+output);
                    boolean result=object.getBoolean("status");
                    String message=object.getString("message");

                    if(result)
                    {
                        remainingCount=object.getInt("remaining");

                        Toast.makeText(Activity_Campaign.this, message, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray=object.getJSONArray("campaigns");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            campaignModelList.add(new CampaignModel(jsonObject));
                        }
                    }
                    else {

                        Toast.makeText(Activity_Campaign.this, message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    progressView.setVisibility(GONE);

                    campaignAdapter.notifyDataSetChanged();
                    if (campaignModelList != null && !campaignModelList.isEmpty() && !campaignModelList.equals("null")) {

                        noRecordLayout.setVisibility(GONE);

                    } else {

                        noRecordLayout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
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

    public interface GetOrderApi
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/campaignMaster")
        Call<ResponseBody> getCamaignData(
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
    @Override
    public void onRefresh() {
        if (campaignModelList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                campaignModelList.clear();
                campaignAdapter.notifyDataSetChanged();

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
    public void onClick(View view) {
        if (view.getId() == R.id.btnRetry) {
            check_connection();
        }
    }

    private void check_connection() {
        if(CheckNetwork.isInternetAvailable(this))
        {
            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            init();
        }
        else {
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        mHandler=new Handler();

        progressDialog=new ProgressDialog(Activity_Campaign.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);

        recycler_view=findViewById(R.id.recycler_view);
        campaignAdapter=new CampaignAdapter(Activity_Campaign.this,campaignModelList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Activity_Campaign.this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(campaignAdapter);

        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && campaignModelList.size() > 4) {
                        if (remainingCount > 0) {
                            Log.d("data", "onScrollStateChanged: "+remainingCount);
                            Log.d("data", "onScrollStateChanged: "+page_count);
                            Log.d("data", "onScrollStateChanged: "+lastVisibleItemPosition);
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getCampaignData();
                        }
                    }
                }
            }
        });
        campaignModelList.clear();
        campaignAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getCampaignData();

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
