package com.windhans.client.forworld.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class LeadDetailsFragment extends Fragment implements View.OnClickListener{
    ImageView imageView1;
   /* TextView edt_productName,edt_price,edt_desc,edt_businessName,edt_businessDesc,edt_purpose,edt_Contact,edt_address,edt_reason1,edt_name,edt_name1;*/
    TextView txt_user_name,txt_contact_number,txt_location,txt_purpose;
    List<LeadDetails> leadDetailsList;
    LeadDeatilAdapter mAdapter;
    RecyclerView recyclerView;
     ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View retView;
    private Handler mHandler;
    List<LeadDetailsNew> leadDetailModelList=new ArrayList<>();
    private String LeadStatus="", prupose="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.lead_detail_fragment, container, false);

        progressView = (ProgressBar)retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) retView.findViewById(R.id.progressBar_endless);
        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        noConnectionLayout = retView.findViewById(R.id.noConnectionLayout);
        btnRetry = retView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless =retView.findViewById(R.id.progressBar_endless);
        leadDetailsList=new ArrayList<>();
        getviews();
        check_connection();
        return retView;
    }

    private void getviews() {

        txt_user_name=retView.findViewById(R.id.txt_user_name);
        txt_contact_number=retView.findViewById(R.id.txt_contact_number);
        txt_location=retView.findViewById(R.id.txt_location);
        txt_purpose=retView.findViewById(R.id.txt_purpose);
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
          //  init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }
    private void init() {

        mHandler = new Handler();
        leadDetailModelList=new ArrayList<>();


        recyclerView =retView.findViewById(R.id.recycler_view);
        mAdapter = new LeadDeatilAdapter(getActivity(),leadDetailModelList, LeadStatus, prupose);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            //orderList.clear();
            check_connection();
        }
    }

    private void getLeadDetails() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
       // progressDialog.show();

        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String lead_id=Shared_Preferences.getPrefs(getContext(),Constants.LEAD_ID);
        Log.d("ids", "getLeadDetails: "+user_id+","+lead_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.getLeadData(user_id,lead_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                progressDialog.dismiss();
                try {
                    output=response.body().string();
                    Log.d("ResponseLeadDetails", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if (result)
                        {
                            JSONObject object=jsonObject.getJSONObject("leadData");
                            LeadStatus = object.getString("status");
                            prupose = object.getString("purpose");

                           // leadDetailModelList.add(new LeadDetailsNew(object));
                            txt_user_name.setText(object.getString("name"));
                            txt_contact_number.setText(object.getString("contact_no"));
                            txt_location.setText(object.getString("address")+", "+object.getString("city"));
                            txt_purpose.setText(object.getString("purpose"));
                            progressDialog.dismiss();

                        }
                       /* if (leadDetailModelList != null && !leadDetailModelList.isEmpty() && !leadDetailModelList.equals("null")) {

                            noRecordLayout.setVisibility(View.GONE);
                            progressDialog.dismiss();

                        } else {

                            noRecordLayout.setVisibility(View.VISIBLE);
                             progressDialog.dismiss();
                        }*/
                        progressDialog.dismiss();
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
}
