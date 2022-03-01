package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Adapter.MultipleImagesForAddingProductAdapter;
import com.windhans.client.forworld.Adapter.ProductsListAdapter;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ActivityAddProductNewFlow extends AppCompatActivity implements View.OnClickListener {


    private List<NewProduct> productList = new ArrayList<>();
    private List<BuisnessModel> buisnessModelList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ProductsListAdapter mAdapter;
    private LinearLayout noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry, btn_submit;
    private Camera camera;
    private ImageView profie_imageView;
    private SearchView mSearchView, et_search;
    private String query = "";
    private Handler mHandler;
    private View retView;
    private FloatingActionButton fab_add_post;
    private String s;
    private ImageView imageView;
    private AutoCompleteTextView ac_product_name;
    private TextInputEditText edt_category, edt_subcategory, edt_brand, edt_description, edt_features, edt_sales_cost,
            edt_original_cost, edt_how_much;
    private Spinner spinner_offer_type;
    private String productId , offerType;
    private Float salesCost;
    private ImageView iv_product_image;
    private RecyclerView recycler_view_images;
    private MultipleImagesForAddingProductAdapter mAdapterImages;


   /* @Override
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
                Log.d("my_tag", "onQueryTextChange: " +s);
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        query = newText;
                        productList.clear();
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
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add_new_flow);

        // setContentView(R.layout.activity_buisness_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

     /*   imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(ActivityAddProductNewFlow.this.getResources().getString(R.string.addProduct));

        //getSupportActionBar().setTitle(""+ Shared_Preferences.getPrefs(ProductListActivity.this, "name"));


        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
    }


    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
            noConnectionLayout.setVisibility(View.GONE);
            init();
        } else {

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {

        edt_category = findViewById(R.id.edt_category);
        edt_subcategory = findViewById(R.id.edt_subcategory);
        edt_brand = findViewById(R.id.edt_brand);
        edt_description = findViewById(R.id.edt_description);
        edt_features = findViewById(R.id.edt_features);
        edt_sales_cost = findViewById(R.id.edt_sales_cost);
        edt_original_cost = findViewById(R.id.edt_original_cost);
        edt_how_much = findViewById(R.id.edt_how_much);
        spinner_offer_type = findViewById(R.id.spinner_offer_type);
        btn_submit = findViewById(R.id.btn_submit);
        iv_product_image = findViewById(R.id.iv_product_image);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    AddProduct();
                }
            }
        });

        spinner_offer_type.setSelection(1);
        offerType = "1";

        spinner_offer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if (position == 1) {
                    offerType = "1"; // flat discount
                    edt_sales_cost.setText("");
                }
                else if (position == 2) {
                    offerType = "2"; // percentage discount
                    edt_sales_cost.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edt_original_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(offerType.equals("1")){
                    edt_sales_cost.setText(edt_original_cost.getText().toString());
                }
            }
        });


        edt_how_much.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edt_how_much.getText().toString().isEmpty() && !edt_original_cost.getText().toString().isEmpty()) {
                    if (offerType.equals("1")) {
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) - Float.parseFloat(edt_how_much.getText().toString());

                    } else  if (offerType.equals("2")){
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) * (Float.parseFloat(edt_how_much.getText().toString()) / 100);
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) - salesCost;
                    }
                    edt_sales_cost.setText(""+salesCost);
                }else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        ac_product_name = findViewById(R.id.ac_product_name);
        productList.clear();

        ac_product_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getProductData(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayAdapter<NewProduct> adapter = new ArrayAdapter<NewProduct>
                        (getBaseContext(), android.R.layout.simple_dropdown_item_1line, productList);
                ac_product_name.setThreshold(1);//will start working from first character
                ac_product_name.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                ac_product_name.setTextColor(Color.BLACK);
                ac_product_name.setTextSize(12);
            }
        });

        ac_product_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityAddProductNewFlow.this, "Selected" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                Object item = parent.getItemAtPosition(position);
                if (item instanceof NewProduct) {
                    NewProduct product = (NewProduct) item;
                    edt_category.setText(product.getCategory_name());
                    edt_subcategory.setText(product.getSub_category());
                    edt_brand.setText(product.getBrand_name());
                    edt_description.setText(product.getDescription());
                    edt_features.setText(product.getFeatures());
                    productId = product.getId();

                    if (!(MyConfig.JSON_BusinessImage + product.getProduct_image()).equals("null") &&
                            !(MyConfig.JSON_BusinessImage + product.getProduct_image()).isEmpty() &&
                            (MyConfig.JSON_BusinessImage + product.getProduct_image()) != null) {
                        Glide
                                .with(ActivityAddProductNewFlow.this)
                                .load(MyConfig.JSON_BusinessImage + product.getProduct_image())

                                .into(iv_product_image);
                    } else {
                        Glide
                                .with(ActivityAddProductNewFlow.this)
                                .load(R.drawable.no_image_available)

                                .into(iv_product_image);
                    }

                    ArrayList aList= new ArrayList(Arrays.asList(product.getMultiple_images().split(",")));
                    for(int i=0;i<aList.size();i++)
                    {
                        Log.d("Multiple", "onCreate: "+aList.get(i));
                    }


                    recycler_view_images = findViewById(R.id.recycler_view_images);
                    mAdapterImages = new MultipleImagesForAddingProductAdapter(ActivityAddProductNewFlow.this, aList);
                    // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
                    recycler_view_images.setLayoutManager(new LinearLayoutManager(ActivityAddProductNewFlow.this, LinearLayoutManager.HORIZONTAL, false));
                    recycler_view_images.setItemAnimator(new DefaultItemAnimator());
                    recycler_view_images.setAdapter(mAdapterImages);
                    mAdapterImages.notifyDataSetChanged();

                }

            }
        });
    }

    private boolean isValid() {
        boolean res = true;
        if (!MyValidator.isValidSpinner(spinner_offer_type)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_sales_cost)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_original_cost)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_how_much)) {
            res = false;
        }

        return res;
    }

    private void AddProduct() {

        progressDialog.show();

        //Log.d("Data", "getMyFriendData: " + user_id + "," + subcategory);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.sendProductDetails(
                Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.REG_ID),
                productId,
                edt_sales_cost.getText().toString().trim(),
                edt_original_cost.getText().toString().trim(),
                offerType,
                edt_how_much.getText().toString().trim()
        );


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {

                        try {
                            Intent intent = new Intent(ActivityAddProductNewFlow.this, ActivityAddProductNewFlow.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ActivityAddProductNewFlow.this, "" + jsonObject.getString("reason"), Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            //Log.d("Progress Dialog","Progress Dialog");
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(ActivityAddProductNewFlow.this, "" + jsonObject.getString("reason"), Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
        check_connection();
    }


    private void getProductData(String query) {

        //progressDialog.show();

        String user_id = Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.REG_ID);
        String buisness_id = Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.BUSINESS_ID);
        //String subcategory = Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.parent_categoryID);
        String subcategory = Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.subcategoryID);

        Log.d("Data", "getMyFriendData: " + user_id + "," + subcategory);
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getRecommended(
                Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.REG_ID),
                Shared_Preferences.getPrefs(ActivityAddProductNewFlow.this, Constants.subcategoryID),
                query,
                page_count);


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        productList.clear();
                        // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("productData");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                productList.add(new NewProduct(obj));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();

                        ArrayAdapter<NewProduct> adapter = new ArrayAdapter<NewProduct>
                                (getBaseContext(), android.R.layout.simple_dropdown_item_1line, productList);
                        //Getting the instance of AutoCompleteTextView

                        ac_product_name.setThreshold(1);//will start working from first character
                        ac_product_name.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                        ac_product_name.setTextColor(Color.BLACK);
                        ac_product_name.setTextSize(12);


                    } else {
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
                t.printStackTrace();
            }
        });
    }

    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/productListingForVendor")
        public Call<ResponseBody> getRecommended(
                @Field("user_id") String user_id,
                @Field("category_id") String subcategory_id,
                @Field("search") String search,
                @Field("page_no") int page_no
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addProduct")
        public Call<ResponseBody> sendProductDetails(
                @Field("user_id") String user_id,
                @Field("product_id") String product_id,
                @Field("selling_price") String selling_price,
                @Field("original_price") String original_price,
                @Field("offer_type") String offer_type,
                @Field("offer_amount") String offer_amount

        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}


