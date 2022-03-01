package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.windhans.client.forworld.Adapter.AdapterLeadListing;
import com.windhans.client.forworld.Adapter.UserEnqiryAdapter;
import com.windhans.client.forworld.Model.LeadData;
import com.windhans.client.forworld.Model.UserEnquiry;
import com.windhans.client.forworld.R;
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

public class ActivityLead extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<LeadData> leadDataArrayList = new ArrayList<>();
    private List<LeadData> filterdpatientList = new ArrayList<>();
    List<UserEnquiry> userEnquiryList=new ArrayList<>();

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private AdapterLeadListing mAdapter;
    private UserEnqiryAdapter mAdapter1;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;

    private ImageView profie_imageView;
    private SearchView mSearchView,et_search;
    private String query = "";
    private Handler mHandler;


    private String s;
    private ImageView imageView;
    String user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

      //  setContentView(R.layout.activity_buisness_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        String user_type=Shared_Preferences.getPrefs(ActivityLead.this,Constants.REG_TYPE);
        if(user_type.equals("2"))
        {
            title.setText(this.getResources().getString(R.string.lead));
        }
        else {
            title.setText(this.getResources().getString(R.string.enquiries));
        }


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
        user_type=(Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_TYPE));
        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);

        et_search=findViewById(R.id.et_search);
        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        leadDataArrayList.clear();
                        mAdapter.notifyDataSetChanged();
                        page_count = 1;
                        remainingCount = 0;
                        progressView.setVisibility(View.VISIBLE);
                        //getOrganiserData();
                        user_type=(Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_TYPE));
                        if(user_type.equals("1"))
                        {
                            getUserEnqiry();
                        }
                        else {
                            getLeadData();
                        }


                    }
                }, 300);
                return false;
            }
        });
        recyclerView=findViewById(R.id.recycler_view);

        if(user_type.equals("1")) {
            userEnquiryList=new ArrayList<>();
            Log.d("Size", "init: "+userEnquiryList.size());
            mAdapter1 = new UserEnqiryAdapter(this,userEnquiryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter1);
        }
        else {
            leadDataArrayList=new ArrayList<>();
            mAdapter = new AdapterLeadListing(leadDataArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }



        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && leadDataArrayList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            // progressBar_endless.setVisibility(View.VISIBLE);
                            if(user_type.equals("1"))
                            {
                                getUserEnqiry();
                            }
                            else {
                                getLeadData();
                            }
                        }
                    }
                }
            }
        });


        //progressView.setVisibility(View.VISIBLE);
        if(user_type.equals("1"))
        {
            userEnquiryList.clear();
            mAdapter1.notifyDataSetChanged();
            getUserEnqiry();
        }
        else {
            leadDataArrayList.clear();
            mAdapter.notifyDataSetChanged();
            getLeadData();
        }
    }

    private void getUserEnqiry() {
        progressDialog.show();
        userEnquiryList.clear();
        String user_id= Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID);
        String buisness_id= Shared_Preferences.getPrefs(ActivityLead.this, Constants.BUSINESS_ID);
        Log.d("Data", "getMyFriendData: "+user_id);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getEnquiry(
                Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID)

               );


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org", "onResponse11: " +  Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID));
                    Log.d("org", "onResponse11: " + query);
                    Log.d("org", "onResponse11: " + page_count);
                    JSONObject jsonObject = new JSONObject(output);
                    Log.d("response", "onResponse: "+output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                       // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("leadData");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                //leadDataArrayList.add(new LeadData(obj));
                                userEnquiryList.add(new UserEnquiry(obj));


                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                    else { noRecordLayout.setVisibility(View.VISIBLE);
                    }

                    progressView.setVisibility(View.GONE);
                    mAdapter1.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);


                    if (userEnquiryList.isEmpty()) {
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

    private void getLeadData()
    { progressDialog.show();
        leadDataArrayList.clear();
        String user_id= Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID);
        String buisness_id= Shared_Preferences.getPrefs(ActivityLead.this, Constants.BUSINESS_ID);
        Log.d("Data11", "getMyFriendData: "+user_id+""+buisness_id);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getRecommended(
            Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID),
            query,
            page_count);


        result.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            String output = "";
            try {
                progressDialog.dismiss();
                output = response.body().string();
                Log.d("org", "onResponse11: " + Shared_Preferences.getPrefs(ActivityLead.this, Constants.REG_ID));
                Log.d("org", "onResponse11: " + query);
                Log.d("org", "onResponse11: " + page_count);
                JSONObject jsonObject = new JSONObject(output);
                Log.d("response", "onResponse11: " + output);
                boolean res = jsonObject.getBoolean("result");

                if (res) {
                    remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                    JSONArray jsonArray = jsonObject.getJSONArray("leadsData");
                    // Parsing json
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            leadDataArrayList.add(new LeadData(obj));
                        } catch (JSONException e) {
                            //Log.d("Progress Dialog","Progress Dialog");
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
                else { noRecordLayout.setVisibility(View.VISIBLE);
                }

                progressView.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


                if (leadDataArrayList.isEmpty()) {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            //orderList.clear();
            check_connection();
        }
    }
    @Override
    public void onRefresh() {
        if (leadDataArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                leadDataArrayList.clear();
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



    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getAllLeadsByVendor")
        public Call<ResponseBody> getRecommended(
                @Field("user_id") String user_id,
                @Field("search") String search,
                @Field("page_no") int page_no
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/myEnquiries")
        public Call<ResponseBody> getEnquiry(
                @Field("user_id") String user_id
        );
    }



    @Override
    public void onResume() {
        super.onResume();
        check_connection();
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
