package com.windhans.client.forworld.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.ProductsLeadListAdapter;
import com.windhans.client.forworld.Model.LeadDetailModel;
import com.windhans.client.forworld.Model.ProductList;
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


public class ProductListFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProductsLeadListAdapter productsLeadListAdapter;
   // RecyclerView recyclerView;
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
    private View retView;
    String status;
    List<ProductList> leadDetailModelList=new ArrayList<>();
    TextView textview_name,textViewprice,textview_brand,txt_description,txt_description1,textView_originalCost;
    ImageView imageViewpro;



    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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
       // retView = inflater.inflate(R.layout.fragment_product_list, container, false);
        retView = inflater.inflate(R.layout.product_list_item, container, false);


        progressView = (ProgressBar)retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar)retView. findViewById(R.id.progressBar_endless);
        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        noConnectionLayout =retView. findViewById(R.id.noConnectionLayout);
        btnRetry = retView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        //swipeRefreshLayout =retView.findViewById(R.id.refresh);

      //  swipeRefreshLayout.setOnRefreshListener(this);

        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless =retView.findViewById(R.id.progressBar_endless);
        return retView;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        leadDetailModelList=new ArrayList<>();

        textview_name=retView.findViewById(R.id.textview_name);
        textViewprice=retView.findViewById(R.id.textViewprice);
        //   textview_brand=itemView.findViewById(R.id.textview_brand);
        txt_description=retView.findViewById(R.id.txt_description);
        txt_description1=retView.findViewById(R.id.txt_description1);
        imageViewpro=retView.findViewById(R.id.imageViewpro);
      /*  recyclerView=retView.findViewById(R.id.recycler_view_product);
        productsLeadListAdapter = new ProductsLeadListAdapter(getActivity(),leadDetailModelList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsLeadListAdapter);
*/



      /*  leadDetailModelList.clear();
        productsLeadListAdapter.notifyDataSetChanged();*/
        getLeadDetails();
    }
  /*  @Override
    public void onRefresh() {
        if (leadDetailModelList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                leadDetailModelList.clear();
                productsLeadListAdapter.notifyDataSetChanged();
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
    }*/
    private void getLeadDetails() {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String lead_id=Shared_Preferences.getPrefs(getContext(),Constants.LEAD_ID);
        Log.d("ids", "getLeadDetails: "+user_id+","+lead_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.getLeadData(user_id,lead_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("ProductDetails", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if (result)
                        {
                            JSONObject object=jsonObject.getJSONObject("leadData");
                            JSONArray jsonArray = new JSONArray("products");
                            for (int i = 0; i < jsonArray.length() ; i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                leadDetailModelList.add(new ProductList(object));
                            }

                        }
                        setData();
                        progressDialog.dismiss();
                        progressView.setVisibility(GONE);
                        //recyclerView.setAdapter(myProductAdapter);
                    //    productsLeadListAdapter.notifyDataSetChanged();
                        if (leadDetailModelList != null && !leadDetailModelList.isEmpty() && !leadDetailModelList.equals("null")) {

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

    private void setData() {
        for (int i=0;i<leadDetailModelList.size();i++)
        {
            if (leadDetailModelList.get(i).getProduct_name() != null && !leadDetailModelList.get(i).getProduct_name().isEmpty() && !leadDetailModelList.get(i).getProduct_name().equals("null")) {
                textview_name.setText(leadDetailModelList.get(i).getProduct_name());
            }
            else {
                textview_name.setVisibility(GONE);
            }
            if (leadDetailModelList.get(i).getProduct_name() != null && !leadDetailModelList.get(i).getProduct_name().isEmpty() && !leadDetailModelList.get(i).getProduct_name().equals("null")) {
                textViewprice.setText(Constants.rs+leadDetailModelList.get(i).getPrice());
            }
            else {
                textViewprice.setVisibility(GONE);
            }
            if (leadDetailModelList.get(i).getDescription() != null && !leadDetailModelList.get(i).getDescription().isEmpty() && !leadDetailModelList.get(i).getDescription().equals("null")) {
                txt_description.setText(leadDetailModelList.get(i).getDescription());
            }
            else {
                txt_description.setVisibility(GONE);
            }
            // holder.textview_brand.setText(leadDetailModelList.get(position).getBrand());

            DisplayMetrics metricscard = getContext().getResources().getDisplayMetrics();
            int cardwidth = metricscard.widthPixels;
            int cardheight = (int) (metricscard.heightPixels);
            imageViewpro.getLayoutParams().width = (int) (cardwidth / 3);
            imageViewpro.getLayoutParams().height = (int) (cardheight / 5);

            if (leadDetailModelList.get(i).getImage() != null && !leadDetailModelList.get(i).getImage().isEmpty() && !leadDetailModelList.get(i).getImage().equals("null")) {
                Log.d("image", "response: ");

                Glide.with(getContext())
                        .load(MyConfig.JSON_BusinessImage + leadDetailModelList.get(i).getImage())
                        .into(imageViewpro);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.no_image_available)
                        .into(imageViewpro);
            }
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
}
