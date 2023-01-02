package com.windhans.client.forworld.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Activities.ActivityLeadDetails;
import com.windhans.client.forworld.Adapter.LeadDeatilAdapter;
import com.windhans.client.forworld.Model.LeadDetailModel;
import com.windhans.client.forworld.Model.LeadDetails;
import com.windhans.client.forworld.Model.LeadDetailsNew;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
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

public class StatusDetails extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    List<LeadDetails> leadDetailsList;
    LeadDeatilAdapter mAdapter;
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
    String text, Remainingstatus;
    private View retView;
    String status;
    List<LeadDetailModel> leadDetailModelList = new ArrayList<>();
    ArrayList<LeadDetailsNew> leadDetailsNewArrayList = new ArrayList<>();
    private String LeadStatus = "", prupose = "";
    private boolean flag=false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.lead_detail_status, container, false);


        recyclerView = retView.findViewById(R.id.recycler_view);

        progressView = (ProgressBar) retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) retView.findViewById(R.id.progressBar_endless);
        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        noConnectionLayout = retView.findViewById(R.id.noConnectionLayout);
        btnRetry = retView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = retView.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless = retView.findViewById(R.id.progressBar_endless);
        fab = retView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogue();
            }
        });
        return retView;
    }

    private void ViewDialogue() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_lead_reply);
        dialog.setCancelable(true);
        final Spinner spinner_status, spinner_remaining_amount;
        final EditText edt_reason, edt_wallet_amount;
        final LinearLayout ll_completed_module;


        ll_completed_module = dialog.findViewById(R.id.ll_completed_module);
        spinner_status = dialog.findViewById(R.id.spinner_status);
        spinner_remaining_amount = dialog.findViewById(R.id.spinner_remaining_amount);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        edt_wallet_amount = dialog.findViewById(R.id.edt_wallet_amount);

        spinner_remaining_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    Remainingstatus = "Cash";
                }else if(position==2){
                    Remainingstatus = "Cheque";
                }else if(position==3){
                    Remainingstatus = "D.D.";
                }else if(position==4){
                    Remainingstatus = "Debit/Credit Card";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner_status.getSelectedItem().toString();
                if (text.equals("Pending")) {
                    pos = 0;
                    ll_completed_module.setVisibility(View.GONE);
                } else if (text.equals("Completed")) {
                    ll_completed_module.setVisibility(View.VISIBLE);
                    pos = 1;
                } else if (text.equals("Progress")) {
                    ll_completed_module.setVisibility(View.GONE);
                    pos = 2;
                } else {
                    ll_completed_module.setVisibility(View.GONE);
                    pos = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.equals("Pending")) {
                    pos = 0;
                    ll_completed_module.setVisibility(View.GONE);
                } else if (text.equals("Completed")) {
                    ll_completed_module.setVisibility(View.VISIBLE);
                    pos = 1;
                } else if (text.equals("Progress")) {
                    ll_completed_module.setVisibility(View.GONE);
                    pos = 2;
                } else {
                    ll_completed_module.setVisibility(View.GONE);
                    pos = 3;
                }
                String reason = edt_reason.getText().toString();
                String id = String.valueOf(Shared_Preferences.getPrefs(getContext(), Constants.REG_ID));
                String lead_id = Shared_Preferences.getPrefs(getContext(), Constants.LEAD_ID);
                if (text.equals("--Select Status--")) {
                    Toast.makeText(getContext(), "Please Select Status", Toast.LENGTH_SHORT).show();
                } else if (reason.isEmpty()) {
                    edt_reason.setError("Enter Reason");

                } else {

                    if (pos != 1) {
                        sendReply(id, lead_id, pos, reason);
                        dialog.dismiss();
                    }else {
                        if(MyValidator.isValidField(edt_wallet_amount) && MyValidator.isValidSpinner(spinner_remaining_amount)){
                            CompleteLead(id, lead_id, pos, reason, edt_wallet_amount.getText().toString(), Remainingstatus);
                            dialog.dismiss();
                        }
                    }
                }
                Log.d("values", "onClick: " + reason + " " + id + " " + lead_id + " " + pos);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MaterialDialog; //style id
        dialog.show();
    }

    private void CompleteLead(String id, String lead_id, int pos, String reason, String amount, String remainingPayment) {
        progressDialog.show();
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.orderComplete(
                id,
                lead_id,
                amount,
                reason,
                remainingPayment

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("VijendraReply", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    Boolean result = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");
                    if (result) {

                        getLeadDetails();
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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

    private void sendReply(String id, String lead_id, int pos, String reason) {
        ActivityLeadDetails.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(ActivityLeadDetails.GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.sendReply(
                id,
                lead_id,
                String.valueOf(pos),
                reason

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("VijendraReply", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    Boolean result = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");
                    if (result) {
                        Toast.makeText(getContext(), " "+reason, Toast.LENGTH_SHORT).show();
                        getLeadDetails();
                    } else {
                        Toast.makeText(getContext(), " "+reason, Toast.LENGTH_SHORT).show();
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

    private void getLeadDetails1() {
        ActivityLeadDetails.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(ActivityLeadDetails.GetOrderApi.class);
        String userId = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String leadID = Shared_Preferences.getPrefs(getContext(), Constants.LEAD_ID);
        Call<ResponseBody> responseBodyCall = getOrderApi.getLeadData(userId, leadID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    Boolean result = jsonObject.getBoolean("result");
                    if (result) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("leadData");

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("leadDetails");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject object = jsonArray1.getJSONObject(i);
                            leadDetailsList.add(new LeadDetails(object));
                            status = jsonObject1.getString("status");
                        }
                        String image = Shared_Preferences.getPrefs(getContext(), Constants.IMAGE1);

                        Log.d("image", "response: " + image);

                        if (status.equals("1")) {
                            fab.setVisibility(View.GONE);
                        } else {
                            fab.setVisibility(View.VISIBLE);
                        }


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

    private void getLeadDetails() {
        leadDetailsNewArrayList.clear();
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String lead_id = Shared_Preferences.getPrefs(getContext(), Constants.LEAD_ID);
        Call<ResponseBody> responseBodyCall = getOrderApi.getLeadData(user_id, lead_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("VijendraLeadDetails", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            JSONObject object = jsonObject.getJSONObject("leadData");
                            LeadStatus = object.getString("status");
                            prupose = object.getString("purpose");

                            JSONArray jsonArray1 = object.getJSONArray("leadDetails");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(i);
                                leadDetailsNewArrayList.add(new LeadDetailsNew(object1));
                                if(object1.getString("ld_status").equals("1")){

                                }else {

                                }
                            }
                            //leadDetailModelList.add(new LeadDetailModel(object));

                            for (int i = 0; i <jsonArray1.length() ; i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(i);
                                if(object1.getString("ld_status").equals("1")){
                                    fab.setVisibility(View.GONE);
                                        break;
                                }else {
                                    fab.setVisibility(View.VISIBLE);
                                }
                            }

                            progressDialog.dismiss();
                            progressView.setVisibility(View.GONE);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (leadDetailsNewArrayList != null && !leadDetailsNewArrayList.isEmpty() && !leadDetailsNewArrayList.equals("null")) {
                            noRecordLayout.setVisibility(View.GONE);
                        } else {
                            noRecordLayout.setVisibility(View.VISIBLE);
                            // progressDialog.dismiss();
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
    public void onResume() {
        super.onResume();
        check_connection();
    }

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(getContext()))  //if connection available
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
        if (leadDetailsNewArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                leadDetailsNewArrayList.clear();
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        leadDetailsList = new ArrayList<>();


        recyclerView = retView.findViewById(R.id.recycler_view);
        mAdapter = new LeadDeatilAdapter(getActivity(), leadDetailsNewArrayList, LeadStatus, prupose);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
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


        leadDetailModelList.clear();
        mAdapter.notifyDataSetChanged();
        getLeadDetails();
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/leadDetails")
        Call<ResponseBody> getLeadData(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/leadReply")
        Call<ResponseBody> sendReply(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id,
                @Field("status") String status,
                @Field("lead_reason") String lead_reason
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/getRedemption")
        Call<ResponseBody> orderComplete(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id,
                @Field("points_redeem") String points_redeem,
                @Field("reason") String reason,
                @Field("payment_type") String payment_type
        );
    }

}
