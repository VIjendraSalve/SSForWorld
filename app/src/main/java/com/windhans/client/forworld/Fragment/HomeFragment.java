package com.windhans.client.forworld.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.windhans.client.forworld.Activities.ActivityBuisnessListing;
import com.windhans.client.forworld.Activities.ActivityProductUniversalList;
import com.windhans.client.forworld.Activities.UpdateBusinessProfile;
import com.windhans.client.forworld.Adapter.BusinessAdapter;
import com.windhans.client.forworld.Adapter.DashboardAdapter;
import com.windhans.client.forworld.Adapter.DashboardAdapterForServices;
import com.windhans.client.forworld.Adapter.UserDashboardAdapter;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.CategoryModel;
import com.windhans.client.forworld.Model.Dashboard;
import com.windhans.client.forworld.Model.District;
import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.Model.ProductList;
import com.windhans.client.forworld.Model.Services;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.GPSTracker;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class HomeFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private View retView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button btnRetry;
    private int page_count;
    RecyclerView recycler_view, recycler_view_subcateory, recycler_view_business, recycler_view_services;
    private Handler mHandler;
    private UserDashboardAdapter mAdapter;
    private DashboardAdapter mAdapter1;
    private DashboardAdapterForServices dashboardAdapterForServices;
    private BusinessAdapter bAdapter;
    private int remainingCount;
    List<Dashboard> dashboardList;
    List<CategoryModel> categoryModelList;
    List<CategoryModel> categoryModelList1;
    List<Services> servicesList;
    private ArrayList<NewProduct> productList = new ArrayList<>();
    private ArrayList<NewProduct> productList_array = new ArrayList<>();
    private List<ProductList> filterdpatientList = new ArrayList<>();
    List<BuisnessModel> buisnessModelList;
    List<BuisnessModel> buisnessModelList1 = new ArrayList<>();
    LinearLayout ll_product_details;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_business_title;
    ImageView imageView;
    private String query = "";
    private EditText mSearchView;
    private ArrayList<String> sliderImages1 = new ArrayList<>();
    private ArrayList<String> sliderImages2 = new ArrayList<>();
    private ArrayList<String> sliderImages3 = new ArrayList<>();

    private HashMap<String, Integer> sliderImages = new HashMap<String, Integer>();
    private int index = 0;
    TextView btn_view_more_business;
    MyTextView_Poppins_Regular btn_view_more;
    CardView cardView;
    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    private LinearLayout ll_search_view;
    private SearchableSpinner spinner_district;
    private String districtId = "";
    private ArrayList<District> districtArrayList = new ArrayList<>();
    private GPSTracker gpsTracker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.fragment_home, container, false);
        gpsTracker = new GPSTracker(getContext());
        // setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        progressView = (ProgressBar) retView.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) retView.findViewById(R.id.progressBar_endless);

        noRecordLayout = retView.findViewById(R.id.noRecordLayout);
        spinner_district = retView.findViewById(R.id.spinner_district);
        noConnectionLayout = retView.findViewById(R.id.noConnectionLayout);
        btnRetry = retView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        imageView = (ImageView) retView.findViewById(R.id.image);
        tv_business_title = (TextView) retView.findViewById(R.id.tv_business_title);
        //swipeRefreshLayout=retView.findViewById(R.id.refresh);
        progressView = retView.findViewById(R.id.progress_view);
        progressBar_endless = retView.findViewById(R.id.progressBar_endless);
        recycler_view = retView.findViewById(R.id.recycler_view);
        btn_view_more = retView.findViewById(R.id.btn_view_more);
        btn_view_more_business = retView.findViewById(R.id.btn_view_more_business);
        cardView = retView.findViewById(R.id.cardView);
        mSearchView = retView.findViewById(R.id.search_view1);
        ll_search_view = retView.findViewById(R.id.ll_search_view);

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityProductUniversalList.class);
                startActivity(intent);
            }
        });

        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityProductUniversalList.class);
                startActivity(intent);
            }
        });
        btn_view_more_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityBuisnessListing.class);
                startActivity(intent);
            }
        });


        prepareSlider();
        prepareSlider2();
        getDistrictData();
        return retView;
    }

    private void ShowDialogue() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Please Select Your Current City to get product");
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    private void getDistrictData() {
        districtArrayList.clear();
        progressDialog.show();
        UpdateBusinessProfile.FileUploadService api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(UpdateBusinessProfile.FileUploadService.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getDistrict(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID),
                "24");
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org", "onResponsecatlist: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        progressDialog.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("state_name");
                        // Parsing json
                        //districtArrayList.add(new District("-1","Select District", "0"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                districtArrayList.add(new District(obj));
                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<District> dataAdapter = new ArrayAdapter<District>(getContext(), android.R.layout.simple_spinner_item, districtArrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_district.setAdapter(dataAdapter);

                        if (Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT_NAME_Location) != null) {
                            for (int i = 0; i < districtArrayList.size(); i++) {
                                if (districtArrayList.get(i).getName().equals(Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT_NAME_Location))) {
                                    spinner_district.setSelection(i);
                                    Shared_Preferences.setPrefs(getContext(), Constants.DISTRICT, districtArrayList.get(i).getId());
                                    if (Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT) != null) {
                                        getBusinessData();
                                    }
                                }
                            }
                        }


                        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //  On selecting a spinner item
                                //String item = adapterView.getItemAtPosition(i).toString();
                                districtId = districtArrayList.get(i).getId();
                                Shared_Preferences.setPrefs(getContext(), Constants.DISTRICT, districtId);
                                getBusinessData();
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void prepareSlider2() {

      /*  sliderImages3.add("http://godapark.com/slider/for1.jpg");
        sliderImages3.add("http://godapark.com/slider/for2.jpg");
        sliderImages3.add("http://godapark.com/slider/for3.jpg");
        sliderImages3.add("http://godapark.com/slider/for4.jpg");
        sliderImages3.add("http://godapark.com/slider/for5.jpg");*/
        /*sliderImages3.add("http://godapark.com/slider/for7.jpeg");
        sliderImages3.add(" http://godapark.com/slider/for8.jpeg");
        sliderImages3.add(" http://godapark.com/slider/for9.jpeg");
*/


    }

    private void prepareSlider() {


        /*sliderImages2.add("http://godapark.com/slider/for1.jpg");
        sliderImages2.add("http://godapark.com/slider/for2.jpg");


        SliderLayout imgSliderLayout = (SliderLayout) retView.findViewById(R.id.imgSliderLayout);
        //ArrayList<String> img_maps = new ArrayList<>();
        Log.d("my_tag", "prepareSlider: " + sliderImages);


        for (int i = 0; i < sliderImages2.size(); i++) {
            index++;
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            // Log.d("image", sliderImages.get(i));
            defaultSliderView
                    //.description(name)
                    .image(sliderImages2.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString("extra", sliderImages2.get(i));

            imgSliderLayout.addSlider(defaultSliderView);
        }


        imgSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        imgSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgSliderLayout.setCustomAnimation(new DescriptionAnimation());
        imgSliderLayout.setDuration(3000);*/
    }

    private void prepareSlider1() {
        Log.d("Slider234", "prepareSlider1: API CAll");
        progressDialog.show();
        index = 0;

        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String buisness_id = Shared_Preferences.getPrefs(getContext(), Constants.BUSINESS_ID);
        //String subcategory=   Shared_Preferences.getPrefs(getContext(), Constants.SUBCATEGORY);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getBannerImageByLatLong(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID)
                /*String.valueOf(gpsTracker.getLatitude()),
                String.valueOf(gpsTracker.getLongitude())*/
        );


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org1234", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        sliderImages1.clear();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("bannerImage");
                        JSONArray jsonArray = jsonObject1.getJSONArray("company");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                {
                                    Log.d("ImagePath12", "onResponse: " + MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                    sliderImages1.add(MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                }

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }


                        //2nd slider
                        sliderImages3.clear();
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("grocery");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            try {
                                JSONObject obj = jsonArray1.getJSONObject(i);
                                {
                                    Log.d("ImagePath12", "Grocery: " + MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                    sliderImages3.add(MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                }

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        //3nd slider
                        sliderImages2.clear();
                        JSONArray jsonArray2 = jsonObject1.getJSONArray("mixed");
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            try {
                                JSONObject obj = jsonArray2.getJSONObject(i);
                                {
                                    Log.d("ImagePath12", "Mixed: " + MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                    sliderImages2.add(MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/slider/" + obj.getString("image"));
                                }

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        //  productList_array=new ArrayList<>();

                        SliderLayout imgSliderLayout = (SliderLayout) retView.findViewById(R.id.imgSliderLayout1);
                        //ArrayList<String> img_maps = new ArrayList<>();
                        Log.d("my_tag", "prepareSlider: " + sliderImages1.size());

                        for (int i = 0; i < sliderImages1.size(); i++) {
                            Log.d("Images1234", "onResponse: " + sliderImages1.get(i));
                        }

                        Log.d("IndexSize", "onResponse: " + index);
                        for (int i = 0; i < sliderImages1.size(); i++) {
                            index++;
                            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
                            // Log.d("image", sliderImages.get(i));
                            defaultSliderView
                                    //.description(name)
                                    .image(sliderImages1.get(i))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            defaultSliderView.bundle(new Bundle());
                            defaultSliderView.getBundle().putString("extra", sliderImages1.get(i));

                            imgSliderLayout.addSlider(defaultSliderView);
                        }


                        imgSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                        imgSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        imgSliderLayout.setCustomAnimation(new DescriptionAnimation());
                        imgSliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                        imgSliderLayout.setDuration(3000);

                        // slider 2
                        SliderLayout imgSliderLayout1 = (SliderLayout) retView.findViewById(R.id.imgSliderLayout);
                        //ArrayList<String> img_maps = new ArrayList<>();
                        Log.d("my_tag", "prepareSlider123: " + sliderImages3.size());


                        for (int i = 0; i < sliderImages3.size(); i++) {
                            index++;
                            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
                            // Log.d("image", sliderImages.get(i));
                            defaultSliderView
                                    //.description(name)
                                    .image(sliderImages3.get(i))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            defaultSliderView.bundle(new Bundle());
                            defaultSliderView.getBundle().putString("extra", sliderImages3.get(i));

                            imgSliderLayout1.addSlider(defaultSliderView);
                        }


                        imgSliderLayout1.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                        imgSliderLayout1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        imgSliderLayout1.setCustomAnimation(new DescriptionAnimation());
                        imgSliderLayout1.setDuration(3000);
                        imgSliderLayout1.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);


                        // slider 3
                        SliderLayout imgSliderLayout3 = (SliderLayout) retView.findViewById(R.id.imgSliderLayout2);
                        //ArrayList<String> img_maps = new ArrayList<>();
                        Log.d("my_tag", "prepareSlider123: " + sliderImages2.size());


                        for (int i = 0; i < sliderImages2.size(); i++) {
                            index++;
                            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
                            // Log.d("image", sliderImages.get(i));
                            defaultSliderView
                                    //.description(name)
                                    .image(sliderImages2.get(i))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            defaultSliderView.bundle(new Bundle());
                            defaultSliderView.getBundle().putString("extra", sliderImages2.get(i));

                            imgSliderLayout3.addSlider(defaultSliderView);
                        }


                        imgSliderLayout3.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                        imgSliderLayout3.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        imgSliderLayout3.setCustomAnimation(new DescriptionAnimation());
                        imgSliderLayout3.setDuration(3000);
                        imgSliderLayout3.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

                        progressDialog.dismiss();
                    } else {
                        //noRecordLayout.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
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
                // swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });


        /* *//*   sliderImages1.add("http://godapark.com/slider/for1.jpg");
        sliderImages1.add("http://godapark.com/slider/for2.jpg");
        sliderImages1.add("http://godapark.com/slider/for3.jpg");
        sliderImages1.add("http://godapark.com/slider/for4.jpg");
        sliderImages1.add("http://godapark.com/slider/for5.jpg");*//*
        sliderImages1.add("http://godapark.com/slider/for7.jpeg");
        sliderImages1.add("http://godapark.com/slider/for3.jpg");
        sliderImages1.add("http://godapark.com/slider/for8.jpeg");
        sliderImages1.add("http://godapark.com/slider/for5.jpg");
        sliderImages1.add("http://godapark.com/slider/for9.jpeg");
        sliderImages1.add("http://godapark.com/slider/for6.jpg");
       // sliderImages1.add("http://godapark.com/slider/for7.jpg");*/


    }

    @Override
    public void onResume() {
        super.onResume();
        if (Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT) == null) {
            //ShowDialogue();
        }
        check_connection();
    }

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        mSearchView =  (SearchView) myActionMenuItem.getActionView();
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
                        //productList_array.clear();
                        mAdapter.notifyDataSetChanged();
                        page_count = 1;
                        remainingCount = 0;
                        progressView.setVisibility(View.VISIBLE);
                        //getOrganiserData();
                        getProductData();

                    }
                }, 300);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return false;

            default:
                break;
        }

        return false;
    }*/

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(getContext()))  //if connection available
        {
            page_count = 1;
            noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);
            init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);


        recycler_view = retView.findViewById(R.id.recycler_view);

        buisnessModelList = new ArrayList<>();
        productList_array = new ArrayList<>();
        mAdapter = new UserDashboardAdapter(getContext(), productList_array);
        // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);

        //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        //recycler_view.setAdapter(mAdapter);


        //productList_array.clear();
        //mAdapter.notifyDataSetChanged();
        getProductData();


        recycler_view_services = retView.findViewById(R.id.recycler_view_services); // vijendra
        servicesList = new ArrayList<>();
        dashboardAdapterForServices = new DashboardAdapterForServices(getContext(), servicesList);
        GridLayoutManager mLayoutManager4 = new GridLayoutManager(getContext(), 3);
        recycler_view_services.setLayoutManager(mLayoutManager4);
        recycler_view_services.setItemAnimator(new DefaultItemAnimator());
        recycler_view_services.setAdapter(dashboardAdapterForServices);


        recycler_view_subcateory = retView.findViewById(R.id.recycler_view_subcateory);


        categoryModelList = new ArrayList<>();
        categoryModelList1 = new ArrayList<>();
        mAdapter1 = new DashboardAdapter(getContext(), categoryModelList1, recycler_view);
        //RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(getContext(), 4);
        recycler_view_subcateory.setLayoutManager(mLayoutManager2);
        recycler_view_subcateory.setItemAnimator(new DefaultItemAnimator());
        recycler_view_subcateory.setAdapter(mAdapter1);


        categoryModelList.clear();
        mAdapter1.notifyDataSetChanged();
        getData();

        buisnessModelList = new ArrayList<>();
        buisnessModelList1 = new ArrayList<>();

        recycler_view_business = retView.findViewById(R.id.recycler_view_business);
        bAdapter = new BusinessAdapter(getContext(), buisnessModelList);
        GridLayoutManager mLayoutManager3 = new GridLayoutManager(getContext(), 2);
        recycler_view_business.setLayoutManager(mLayoutManager3);
        recycler_view_business.setItemAnimator(new DefaultItemAnimator());
        recycler_view_business.setAdapter(bAdapter);

        if (buisnessModelList1.size() < 1) {
            tv_business_title.setVisibility(View.GONE);
            btn_view_more_business.setVisibility(View.GONE);
        } else {
            tv_business_title.setVisibility(View.VISIBLE);
            btn_view_more_business.setVisibility(View.VISIBLE);
        }


        buisnessModelList1.clear();
        buisnessModelList.clear();
        bAdapter.notifyDataSetChanged();
        getBusinessData();

    }

    //category list api
    private void getData() {
        progressDialog.show();

        Log.d("Data123", "getData: ");
        GetOrderAPI getOrderAPI = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        Call<ResponseBody> responseBodyCall = getOrderAPI.getDashboardData(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output;
                try {

                    output = response.body().string();
                    Log.d("Response", "Category: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    JSONArray jsonArray = jsonObject.getJSONArray("categorylist");
                    //String uri = (getResources().getDrawable(R.drawable.view_all));
                    String uri = getURLForResource(R.drawable.view_all);
                    Log.d("view_all", "onResponse: " + uri);
                    categoryModelList.clear();
                    categoryModelList1.clear();
                    categoryModelList.add(0, new CategoryModel("-1", "See All", uri));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        categoryModelList.add(new CategoryModel(object));

                        //Shared_Preferences.setPrefs(getContext(),Constants.REG_ID,object.getString("user_id"));
                    }

                    getServicesList();
                    categoryModelList1.add(0, categoryModelList.get(1));
                    categoryModelList1.add(1, categoryModelList.get(2));
                    categoryModelList1.add(2, categoryModelList.get(3));
                    categoryModelList1.add(3, categoryModelList.get(4));
                    categoryModelList1.add(4, categoryModelList.get(5));
                    categoryModelList1.add(5, categoryModelList.get(6));
                    categoryModelList1.add(6, categoryModelList.get(7));
                    categoryModelList1.add(7, categoryModelList.get(8));

                    //  categoryModelList1.add(12,categoryModelList.get(0));
                    if (categoryModelList1 != null && !categoryModelList1.isEmpty() && !categoryModelList1.equals("null")) {
                        progressDialog.dismiss();
                        noRecordLayout.setVisibility(View.GONE);

                    } else {
                        progressDialog.dismiss();
                        //noRecordLayout.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                    progressView.setVisibility(View.GONE);
                    recycler_view_subcateory.setAdapter(mAdapter1);
                    mAdapter1.notifyDataSetChanged();

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

    private String getURLForResource(int view_all) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + view_all).toString();

    }

    private void getProductData() {
        progressDialog.show();

        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String buisness_id = Shared_Preferences.getPrefs(getContext(), Constants.BUSINESS_ID);
        //String subcategory=   Shared_Preferences.getPrefs(getContext(), Constants.SUBCATEGORY);

        Log.d("Data12345", "getProductData: " + user_id + "," + "," + query + " ," + page_count);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getRecommended(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID),
                ""
               /* query,
                page_count*/);


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        productList_array.clear();
                        productList.clear();
                        // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("bannerImage");
                        JSONArray jsonArray = jsonObject1.getJSONArray("product");
                        // Parsing json
                        String uri = getURLForResource(R.drawable.see_more);
                        productList.add(0, new NewProduct("-1", "See All", uri, ""));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                productList.add(new NewProduct(obj));
                                //   Log.d("Buisness", "onResponse: "+obj.getString("business_id"));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        //  productList_array=new ArrayList<>();


                        Log.d("SizeProList", "onResponse: " + productList.size());
                        Log.d("SizeProList", "productList_arrayB: " + productList_array.size());
                        if (productList.size() < 6) {
                            Log.d("TestLog", "in IF ");
                            for (int i = 0; i < (productList.size() - 1); i++) {
                                productList_array.add(i, productList.get(i + 1));
                            }
                        } else {
                            for (int i = 0; i <= 3; i++) {
                                Log.d("TestLog", "in Else ");
                                productList_array.add(i, productList.get(i + 1));
                            }
                        }

                        Log.d("SizeProList", "productList_arrayA: " + productList_array.size());
                        mAdapter = new UserDashboardAdapter(getContext(), productList_array);
                        recycler_view.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        progressDialog.dismiss();
                        //prepareSlider1();
                    } else {
                        //noRecordLayout.setVisibility(View.VISIBLE);
                    }

                    progressView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                    // swipeRefreshLayout.setRefreshing(false);

                    if (productList_array.isEmpty()) {
                        //noRecordLayout.setVisibility(View.VISIBLE);
                        progressView.setVisibility(View.GONE);
                    } else {
                        progressView.setVisibility(View.GONE);
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
                // swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getServicesList() {
        progressDialog.show();
        servicesList.clear();
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getServices(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID));


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {

                        progressDialog.dismiss();
                        // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("categorylist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                servicesList.add(new Services(obj));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        dashboardAdapterForServices = new DashboardAdapterForServices(getContext(), servicesList);
                        GridLayoutManager mLayoutManager4 = new GridLayoutManager(getContext(), 3);
                        recycler_view_services.setLayoutManager(mLayoutManager4);
                        recycler_view_services.setItemAnimator(new DefaultItemAnimator());
                        recycler_view_services.setAdapter(dashboardAdapterForServices);

                        progressDialog.dismiss();
                        prepareSlider1();
                    } else {
                        //noRecordLayout.setVisibility(View.VISIBLE);
                    }

                    progressView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
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
                // swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getBusinessData() {
        progressDialog.show();
        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        String buisness_id = Shared_Preferences.getPrefs(getContext(), Constants.BUSINESS_ID);
        // String subcategory=   Shared_Preferences.getPrefs(getContext(), Constants.SUBCATEGORY);

        Log.d("Data123", "getBusinessData: " + user_id);
        Log.d("Data123", "getBusinessData: " + Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT));
     /*   Log.d("Data123", "getBusinessData: " + query);
        Log.d("Data123", "getBusinessData: " + page_count);*/

        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getBusinessData(
                Shared_Preferences.getPrefs(getContext(), Constants.REG_ID),
                Shared_Preferences.getPrefs(getContext(), Constants.DISTRICT)
               /* "1",
                query,
                page_count*/);


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("BusinessData", "onResponse: " + output);
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    String reason = jsonObject.getString("reason");
                    if (res) {
                        buisnessModelList.clear();
                        buisnessModelList1.clear();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("bannerImage");
                        JSONArray jsonArray = jsonObject1.getJSONArray("business");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                buisnessModelList.add(new BuisnessModel(obj));
                                buisnessModelList1.add(new BuisnessModel(obj));

                                //   Log.d("Buisness", "onResponse: "+obj.getString("business_id"));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        /*buisnessModelList1.add(0,buisnessModelList.get(0));
                        buisnessModelList1.add(1,buisnessModelList.get(1));
                        buisnessModelList1.add(2,buisnessModelList.get(2));
                        buisnessModelList1.add(3,buisnessModelList.get(3));*/
                        Log.d("VijBus1", "before id: " + buisnessModelList1.size());
                        Log.d("VijBus1", "before id:: " + buisnessModelList.size());

                        /*if (buisnessModelList.size() > 4) {
                            Log.d("asdfg", "in IF ");
                            for (int i = 0; i < 4; i++) {
                                buisnessModelList1.add(i, buisnessModelList.get(i));
                            }
                        } else if (buisnessModelList.size() < 4 && buisnessModelList.size() > 1) {
                            for (int i = 0; i <= buisnessModelList.size(); i++) {
                                Log.d("asdfg", "in Else ");
                                buisnessModelList1.add(i, buisnessModelList.get(i));
                            }
                        }*/

                        if (buisnessModelList.size() < 1) {
                            tv_business_title.setVisibility(View.GONE);
                            btn_view_more_business.setVisibility(View.GONE);
                        } else {
                            tv_business_title.setVisibility(View.VISIBLE);
                            btn_view_more_business.setVisibility(View.VISIBLE);
                        }

                        Log.d("VijBus1", "After id:: " + buisnessModelList1.size());
                        Log.d("VijBus1", "After id:: " + buisnessModelList.size());

                       /* bAdapter = new BusinessAdapter(getContext(),buisnessModelList1);
                        recycler_view_business.setAdapter(bAdapter);
                        bAdapter.notifyDataSetChanged();*/

                        bAdapter = new BusinessAdapter(getContext(), buisnessModelList);
                        GridLayoutManager mLayoutManager3 = new GridLayoutManager(getContext(), 2);
                        recycler_view_business.setLayoutManager(mLayoutManager3);
                        recycler_view_business.setItemAnimator(new DefaultItemAnimator());
                        recycler_view_business.setAdapter(bAdapter);
                        bAdapter.notifyDataSetChanged();



                        progressDialog.dismiss();
                        // prepareSlider1();
                    } else { //noRecordLayout.setVisibility(View.VISIBLE);
                    }


                    progressDialog.dismiss();
                    progressView.setVisibility(View.GONE);

                    // swipeRefreshLayout.setRefreshing(false);


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
                // swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {

            check_connection();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

   /* @Override
    public void onRefresh() {
        if (productList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                productList.clear();
                mAdapter.notifyDataSetChanged();
                page_count = 1;
                remainingCount = 0;
                check_connection();
                progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;
            check_connection();
            progressBar_endless.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);


        }
    }*/

    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getIsfeatured")
        public Call<ResponseBody> getRecommended(
                @Field("user_id") String user_id,
                @Field("district") String district
               /* @Field("subcategory_id") String subcategory_id,
                @Field("search") String search,
                @Field("page_no") int page_no*/
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getIsfeatured")
        public Call<ResponseBody> getBusinessData(
                @Field("user_id") String user_id,
                @Field("district") String district
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/dashboardData")
        Call<ResponseBody> getDashboardData(
                @Field("user_id") String user_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getServiceCategoryListing")
        Call<ResponseBody> getServices(
                @Field("user_id") String user_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getSliderImage")
        Call<ResponseBody> getBannerImageByLatLong(
                @Field("user_id") String user_id
                /*@Field("latitude") String latitude,
                @Field("longitude") String longitude*/
        );
    }



  
    /*private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);

        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, getContext());
    }*/

}
