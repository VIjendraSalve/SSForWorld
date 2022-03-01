package com.windhans.client.forworld.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.LeadDeatilAdapter;
import com.windhans.client.forworld.Model.LeadDetailModel;
import com.windhans.client.forworld.Model.LeadDetails;
import com.windhans.client.forworld.Model.LeadDetailsNew;
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

public class ActivityLeadDetails extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    ImageView imageView1;
    TextView edt_productName,edt_price,edt_desc,edt_businessName,edt_businessDesc,edt_purpose,edt_Contact,edt_address,edt_reason1,edt_name;
    List<LeadDetails> leadDetailsList;
    LeadDeatilAdapter mAdapter;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private Toolbar toolbar;
    private Handler mHandler;
    FloatingActionButton fab;
    int pos;
    String text;
    List<LeadDetailModel> leadDetailsList1=new ArrayList<>();
    ArrayList<LeadDetailsNew> leadDetailsNewArrayList = new ArrayList<>();
    private String LeadStatus = "", prupose="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.leaddetails));
    /*    imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.recycler_view);

        progressView = (ProgressBar)findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) findViewById(R.id.progressBar_endless);
        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout =findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless =findViewById(R.id.progressBar_endless);
        leadDetailsList=new ArrayList<>();
        getviews();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogue();
            }
        });

    }

    private void ViewDialogue() {
        final Dialog dialog = new Dialog(ActivityLeadDetails.this);
        dialog.setContentView(R.layout.dialog_lead_reply);
        dialog.setCancelable(true);
        final Spinner spinner_status;
        final EditText edt_reason;


        spinner_status= dialog.findViewById(R.id.spinner_status);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 text = spinner_status.getSelectedItem().toString();
                Toast.makeText(ActivityLeadDetails.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.equals("Pending"))
                {
                    pos=0;
                }
                else if(text.equals("Completed"))
                {
                    pos=1;
                }
                else if(text.equals("Progress"))
                {
                    pos=2;
                }
                else {
                    pos=3;
                }
                String reason=edt_reason.getText().toString();
                String id= String.valueOf(Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.REG_ID));
                String lead_id=Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.LEAD_ID);
                Log.d("values", "onClick: "+reason+" "+id+" "+lead_id+" "+pos);
                sendReply(id,lead_id,pos,reason);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MaterialDialog; //style id
        dialog.show();
    }

    private void sendReply(String id, String lead_id, int pos, String reason) {
        GetOrderApi getOrderApi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.sendReply(
                id,
                lead_id,
                String.valueOf(pos),
                reason

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    Boolean result=jsonObject.getBoolean("result");
                    String reason=jsonObject.getString("reason");
                    if(result)
                    {
                        Toast.makeText(ActivityLeadDetails.this, reason, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ActivityLeadDetails.this, reason, Toast.LENGTH_SHORT).show();
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


    private void getviews() {
        imageView1=findViewById(R.id.imageView);
        edt_productName=findViewById(R.id.edt_productName);
        edt_price=findViewById(R.id.edt_price);
        edt_desc=findViewById(R.id.edt_desc);
        edt_businessName=findViewById(R.id.edt_businessName);
        edt_businessDesc=findViewById(R.id.edt_businessDesc);
        edt_purpose=findViewById(R.id.edt_purpose);
        edt_Contact=findViewById(R.id.edt_Contact);
        edt_address=findViewById(R.id.edt_address);
        edt_reason1=findViewById(R.id.edt_reason1);
        edt_name=findViewById(R.id.edt_name);
        fab=findViewById(R.id.fab);
    }

    private void getLeadDetails() {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String userId= Shared_Preferences.getPrefs(ActivityLeadDetails.this, Constants.REG_ID);
        String leadID= Shared_Preferences.getPrefs(ActivityLeadDetails.this, Constants.LEAD_ID);
        Call<ResponseBody> responseBodyCall=getOrderApi.getLeadData(userId,leadID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    Boolean result=jsonObject.getBoolean("result");
                    if (result)
                    {
                        JSONObject jsonObject1 =jsonObject.getJSONObject("leadData");

                        LeadStatus = jsonObject1.getString("status");
                        prupose = jsonObject1.getString("purpose");

                        JSONArray jsonArray1=jsonObject1.getJSONArray("leadDetails");
                        for (int i = 0; i <jsonArray1.length() ; i++) {
                            JSONObject object1 = jsonArray1.getJSONObject(i);
                            leadDetailsNewArrayList.add(new LeadDetailsNew(object1));
                        }

                        String image=Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.IMAGE1);
                        Log.d("image", "response: "+image);
                        if (image!= null && !image.isEmpty() && !image.equals("null")) {


                            Glide.with(ActivityLeadDetails.this)
                                    .load(MyConfig.JSON_BusinessImage +image)

                                    .into(imageView1);
                        } else {
                            Glide.with(ActivityLeadDetails.this)
                                    .load(R.drawable.no_image_available)

                                    .into(imageView1);
                        }

                        edt_productName.setText(Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.PRODUCT_NAME));
                        edt_price.setText(Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.PRICE));
                        edt_desc.setText(Shared_Preferences.getPrefs(ActivityLeadDetails.this,Constants.PRODUCT_DEC));
                        edt_purpose.setText(jsonObject1.getString("purpose"));
                        edt_Contact.setText(jsonObject1.getString("contact_no"));
                        edt_address.setText(jsonObject1.getString("address"));




                        //setData(leadDetailsList);
                        progressDialog.dismiss();
                        progressView.setVisibility(View.GONE);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (leadDetailsList != null && !leadDetailsList.isEmpty() && !leadDetailsList.equals("null")) {

                        noRecordLayout.setVisibility(View.GONE);

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

    private void setData(List<LeadDetails> leadDetailsList) {


    }

    @Override
    public void onResume() {
        super.onResume();
        check_connection();
    }
    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {

            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
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
        if (leadDetailsList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                leadDetailsList1.clear();
                mAdapter.notifyDataSetChanged();
                check_connection();
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);

            check_connection();
            progressBar_endless.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);

        }
    }
    private void init() {

        mHandler = new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        leadDetailsList=new ArrayList<>();


        recyclerView =findViewById(R.id.recycler_view);
        mAdapter = new LeadDeatilAdapter(this,leadDetailsNewArrayList, LeadStatus, prupose);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


       /* recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                  getLeadDetails();
                }
            }
        });*/


        leadDetailsList1.clear();
        mAdapter.notifyDataSetChanged();
        getLeadDetails();
    }


    public interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/leadDetails")
        Call<ResponseBody> getLeadData(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id
        );
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/leadReply")
        Call<ResponseBody> sendReply(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id,
                @Field("status") String status,
                @Field("lead_reason") String lead_reason
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
