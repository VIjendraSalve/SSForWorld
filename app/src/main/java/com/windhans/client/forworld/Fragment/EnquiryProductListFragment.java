package com.windhans.client.forworld.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.ProductsEnquiryListAdapter;
import com.windhans.client.forworld.Model.EnquiryDetailModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
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


public class EnquiryProductListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProductsEnquiryListAdapter productsLeadListAdapter;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout, ll_total_amt;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private Toolbar toolbar;
    private Handler mHandler;
    FloatingActionButton fab;
    int pos;
    String text;
    private View retView;
    String status;
    private MyTextView_Poppins_Bold tv_total_amount;
  //  List<LeadDetailModel> leadDetailModelList=new ArrayList<>();
    List<EnquiryDetailModel> enquiryDetailModelArrayList=new ArrayList<>();
    private String isProduct="", totalAmount="";

    public static EnquiryProductListFragment newInstance(String param1, String param2) {
        EnquiryProductListFragment fragment = new EnquiryProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        check_connection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.fragment_product_list_enquiry, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);

        progressView = (ProgressBar)retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar)retView. findViewById(R.id.progressBar_endless);
        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        noConnectionLayout =retView. findViewById(R.id.noConnectionLayout);
        ll_total_amt =retView. findViewById(R.id.ll_total_amt);
        btnRetry = retView.findViewById(R.id.btnRetry);
        tv_total_amount = retView.findViewById(R.id.tv_total_amount);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout =retView.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless =retView.findViewById(R.id.progressBar_endless);
        return retView;


    }



    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {

            check_connection();
        }
    }

    private void check_connection() {
        if (CheckNetwork.isInternetAvailable(getContext()))  //if connection available
        {

            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {

        mHandler = new Handler();
        enquiryDetailModelArrayList=new ArrayList<>();

        recyclerView=retView.findViewById(R.id.recycler_view_enquiry_product);
     //   recyclerView =retView.findViewById(R.id.recycler_view);
        productsLeadListAdapter = new ProductsEnquiryListAdapter(getActivity(),enquiryDetailModelArrayList,"0");
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsLeadListAdapter);


     /*   recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                  getLeadDetails();
                }
            }
        });
*/

        enquiryDetailModelArrayList.clear();
        productsLeadListAdapter.notifyDataSetChanged();
        getLeadDetails();
    }
    @Override
    public void onRefresh() {
        if (enquiryDetailModelArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                enquiryDetailModelArrayList.clear();
                productsLeadListAdapter.notifyDataSetChanged();
                getLeadDetails();
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);

            getLeadDetails();
            progressBar_endless.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);

        }
    }

    private void getLeadDetails() {
        //progressDialog.show();
        enquiryDetailModelArrayList.clear();
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String lead_id = Shared_Preferences.getPrefs(getContext(),Constants.LEAD_ID);
        Call<ResponseBody> responseBodyCall=getOrderApi.getEnquiryData(user_id,lead_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("VijendraS", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if (result)
                        {
                            JSONObject object=jsonObject.getJSONObject("leadDetails");
                            if(object.has("leadTotal")) {
                                ll_total_amt.setVisibility(View.VISIBLE);
                                tv_total_amount.setText(Constants.rs + " " + object.getString("leadTotal"));
                            }else {
                                ll_total_amt.setVisibility(GONE);
                            }
                            isProduct = object.getString("service_id");
                            if(!object.getString("service_id").equals("0")){
                                JSONArray array = object.getJSONArray("service");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    enquiryDetailModelArrayList.add(new EnquiryDetailModel(object1));
                                }
                            }else {

                                JSONArray array = object.getJSONArray("productDetails");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    enquiryDetailModelArrayList.add(new EnquiryDetailModel(object1));
                                }
                            }

                            progressDialog.dismiss();

                        }else {
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        // isProduct == 1 Service
                        // isProduct == 0 Product
                        productsLeadListAdapter = new ProductsEnquiryListAdapter(getActivity(),enquiryDetailModelArrayList, isProduct);
                        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager2);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(productsLeadListAdapter);

                        if (enquiryDetailModelArrayList != null && !enquiryDetailModelArrayList.isEmpty() && !enquiryDetailModelArrayList.equals("null")) {

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
    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/enquiryDetails")
        Call<ResponseBody> getEnquiryData(
                @Field("user_id") String user_id,
                @Field("lead_id") String lead_id
        );
    }

}
