package com.windhans.client.forworld.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Model.LeadDetails1;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

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

public class EnquiryDetailFragment extends Fragment implements View.OnClickListener {
    ImageView imageView1;
    TextView edt_productName, edt_price, edt_desc, edt_businessName, edt_businessDesc,  edt_reason1,  edt_name1;
    List<LeadDetails1> leadDetailsList;
    MyTextView_Poppins_Regular txt_progress, edt_purpose, edt_address, edt_Contact, edt_name;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View retView;
    private Handler mHandler;
    FloatingActionButton fab;
    int pos;
    String text;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.enquiry_detail_fragment1, container, false);

        progressView = (ProgressBar) retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) retView.findViewById(R.id.progressBar_endless);
        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        noConnectionLayout = retView.findViewById(R.id.noConnectionLayout);
        btnRetry = retView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless = retView.findViewById(R.id.progressBar_endless);
        leadDetailsList = new ArrayList<>();
        getviews();
        check_connection();
        fab = retView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogue();
            }
        });

        return retView;
    }

    private void getviews() {
        //       imageView1=retView.findViewById(R.id.imageView);
        //      edt_productName=retView.findViewById(R.id.edt_productName);
        //       edt_price=retView.findViewById(R.id.edt_price);
//        edt_businessName=retView.findViewById(R.id.edt_businessName);
        //      edt_businessDesc=retView.findViewById(R.id.edt_businessDesc);
        edt_purpose = retView.findViewById(R.id.edt_purpose);
        edt_Contact = retView.findViewById(R.id.edt_Contact);
        edt_address = retView.findViewById(R.id.edt_address);
        edt_reason1 = retView.findViewById(R.id.edt_reason1);
        edt_name = retView.findViewById(R.id.edt_name);
        //     edt_name1=retView.findViewById(R.id.edt_name1);
        txt_progress = retView.findViewById(R.id.txt_progress);
    }

    private void ViewDialogue() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_lead_reply);
        dialog.setCancelable(true);
        final Spinner spinner_status;
        final EditText edt_reason;


        spinner_status = dialog.findViewById(R.id.spinner_status);
        edt_reason = dialog.findViewById(R.id.edt_reason);
        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner_status.getSelectedItem().toString();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
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
                } else if (text.equals("Completed")) {
                    pos = 1;
                } else if (text.equals("Progress")) {
                    pos = 2;
                } else {
                    pos = 3;
                }
                String reason = edt_reason.getText().toString();
                String id = String.valueOf(Shared_Preferences.getPrefs(getContext(), Constants.REG_ID));
                String lead_id = Shared_Preferences.getPrefs(getContext(), Constants.LEAD_ID);
                Log.d("values", "onClick: " + reason + " " + id + " " + lead_id + " " + pos);
                sendReply(id, lead_id, pos, reason);
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
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
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
                    JSONObject jsonObject = new JSONObject(output);
                    Boolean result = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");
                    if (result) {
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
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
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String userId = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String leadID = Shared_Preferences.getPrefs(getContext(), Constants.LEAD_ID);
        Log.d("ids", "getLeadDetails: " + userId + "," + leadID);
        Call<ResponseBody> responseBodyCall = getOrderApi.getEnquiryData(userId, leadID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("Purpose", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    Boolean result = jsonObject.getBoolean("result");
                    if (result) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("leadDetails");
                        //   leadDetailsList.add(new LeadDetails1(jsonObject1));
                        edt_name.setText(jsonObject1.getString("name"));
                        edt_purpose.setText(jsonObject1.getString("purpose"));
                        edt_Contact.setText(jsonObject1.getString("contact_no"));
                        edt_address.setText(jsonObject1.getString("address")+", "+jsonObject1.getString("city"));
                        String status = jsonObject1.getString("status");
                        if (status.equals("0")) {
                            txt_progress.setText(getContext().getResources().getString(R.string.pending));
                        } else if (status.equals("1")) {
                            txt_progress.setText(getContext().getResources().getString(R.string.complete));
                        } else {
                            txt_progress.setText(getContext().getResources().getString(R.string.progress));
                        }

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
    public void onResume() {
        super.onResume();
        check_connection();
    }

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(getContext()))  //if connection available
        {

            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            getLeadDetails();
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

  /*  @Override
    public void onRefresh() {
        if (leadDetailsList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                leadDetailsList.clear();
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
*/

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/enquiryDetails")
        Call<ResponseBody> getEnquiryData(
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
    }
}

