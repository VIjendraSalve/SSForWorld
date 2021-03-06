package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.ssforword.Model.GenrateEnquiryList;

import com.windhans.client.forworld.Adapter.EnquiryAdapter;
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
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;




public class EnquiryActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private List<GenrateEnquiryList> enquiryListsList = new ArrayList<>();
    private List<GenrateEnquiryList> filterdsubCategoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private EnquiryAdapter mAdapter;
    private String CategoryAdd = "";
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    //public static final String IMAGE_URL = "http://annadata.windhans.in/";

    private Bundle bundle;

  //  private LeadList product;
    private SearchView mSearchView;
    private String query = "";
    private Handler mHandler;
    private View retView;
    private Dialog dialog;
    private Toolbar toolbar;
    private String Categoryspiner = "", SubCategoryspiner = "", Productspiner = "";
    FloatingActionButton fab_add_post;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.enquiry));

        /*imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noRecordLayout = (LinearLayout)findViewById(R.id.noRecordLayout);
        noConnectionLayout = (LinearLayout)findViewById(R.id.noConnectionLayout);
        btnRetry = (Button) findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);


        progressView = (ProgressBar) findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar)findViewById(R.id.progressBar_endless);
        fab_add_post = findViewById(R.id.fbtn_addName);
        fab_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);

//        final FragmentManager fragmentManager = getFragmentManager();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_lead);


        mAdapter = new EnquiryAdapter(enquiryListsList);
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
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && enquiryListsList.size() > 3) {
                        if (remainingCount > 0) {
                            page_count++;
                            // progressBar_endless.setVisibility(View.VISIBLE);
                             getEnquiriesData();
//                            if (Shared_Preferences.getPrefs(getContext(), Constants.ROLE_ID).equals("2")) {
//                                getRecommendedvenderData();
//
//                            } else {
//                                getEnquiriesData();
//                            }
                        }
                    }
                }
            }
        });

        enquiryListsList.clear();
        filterdsubCategoryList.clear();
        mAdapter.notifyDataSetChanged();

//        if (Shared_Preferences.getPrefs(getContext(), Constants.ROLE_ID).equals("2")) {
//            getRecommendedvenderData();
//
//        } else {
           getEnquiriesData();
//        }
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
        if (enquiryListsList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                enquiryListsList.clear();
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

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        check_connection();
    }



    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/leadListing")
        public Call<ResponseBody> getlead(
                @Field("user_id") String user_id

        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/myEnquiries")
        public Call<ResponseBody> MyEnquiries(
                @Field("user_id") String user_id

        );

    }

    private void getEnquiriesData() {

        progressDialog.show();
        filterdsubCategoryList.clear();
        EnquiryActivity.GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(EnquiryActivity.GetOrderAPI.class);
       final Call<ResponseBody> result = api.MyEnquiries(Shared_Preferences.getPrefs(this, Constants.REG_ID));
      //  final Call<ResponseBody> result = api.MyEnquiries("1");
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                 //   String output = response.body().string();
                    Log.d("id", "onResponse1: " + output);
                    output = response.body().string();
                    Log.d("org", "onResponseuser: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    if (res) {
                        JSONArray jsonArray = jsonObject.getJSONArray("leadData");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                enquiryListsList.add(new GenrateEnquiryList(obj));
                                // filterdsubCategoryList.add(new LeadList(obj));
                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(EnquiryActivity.this, "" + jsonObject.getString("reason"), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();


                    if (enquiryListsList != null && !enquiryListsList.isEmpty() && !enquiryListsList.equals("null")) {
                        Log.d("friend list", "" + enquiryListsList.toString());
                        noRecordLayout.setVisibility(View.GONE);


                    } else {
                        noRecordLayout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }

                    progressView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();


                    if (enquiryListsList.isEmpty()) {
                        noRecordLayout.setVisibility(View.VISIBLE);
                    } else {
                        noRecordLayout.setVisibility(View.GONE);
                    }
                    progressView.setVisibility(View.GONE);

                    progressDialog.dismiss();

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

