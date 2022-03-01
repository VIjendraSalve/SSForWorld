package com.windhans.client.forworld.Activities;

import android.animation.Animator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.windhans.client.forworld.Adapter.MultipleImagesAdapter;
import com.windhans.client.forworld.Adapter.VendorByProductAdapter;
import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.Model.NewProduct;
import com.windhans.client.forworld.Model.VendorByProduct;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.gallery_view.ImageViewZoom;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ProductDetailsActivity extends AppCompatActivity implements MultipleImagesAdapter.RecyclerViewItemInterface {
    TextView edt_name, edt_businessName, edt_businessDesc, edt_ContactNo,
             contactName, txt_offer, txt_offerName, txt_offerDescription, txt_offerValid,
            txt_offerDiscount, imageView_offerDetails;
    MyTextView_Poppins_Bold tv_all_sellers, tv_features_lable, edt_productName, edt_price;
    MyTextView_Poppins_Regular edt_features, edt_desc;
    ImageView imageView, imageView1, imageView_buisness;
    Button btn_add_to_enquriy, btn_add_to_cart;
    private Dialog dialog;
    private Context context;
    private Animator mCurrentAnimatorEffect;
    private int mShortAnimationDurationEffect;
    LinearLayout ll_offerDetails, offerDetail;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    int flag = 1;
    CardView cardView_offer;
    RecyclerView recycler_view_product, recycler_view_images;
    private ArrayList<VendorByProduct> vendorByProductList = new ArrayList<>();
    private VendorByProductAdapter mAdapter;
    private MultipleImagesAdapter mAdapterImages;
    private int remainingCount, page_count;
    private String query;
    private ProgressDialog progressDialog;
    private Handler mHandler;
    ImageView image_like, image_dislike;
    Button btn_business_details;
    // SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> sliderImages1 = new ArrayList<>();
    private ArrayList<String> productMultipleImageslist;
    private int index;
    private ArrayList<String> descimagelist = new ArrayList<>();
    private ArrayList<NewProduct> productArrayList = new ArrayList<>();
    private int position, positionForImage=0;
    private LinearLayout ll_features;
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
//vijendra product screen
        productArrayList = this.getIntent().getParcelableArrayListExtra(Constants.ProductList);
        position = this.getIntent().getIntExtra(Constants.Position, 0);
        progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        getProductData();
        Log.d("SizeOfList", "onCreate: "+productArrayList.size());
        Log.d("SizeOfList", "Position: "+position);
        Log.d("SizeOfList", "Position: "+productArrayList.get(position).getId());
        title.setText(productArrayList.get(position).getName());

       /* imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  swipeRefreshLayout=findViewById(R.id.refresh);
        edt_name = findViewById(R.id.edt_Name);
        edt_desc = findViewById(R.id.edt_desc);
        edt_features = findViewById(R.id.edt_features);
        tv_features_lable = findViewById(R.id.tv_features_lable);
        edt_price = findViewById(R.id.edt_price);
        edt_businessName = findViewById(R.id.edt_businessName);
        edt_businessDesc = findViewById(R.id.edt_businessDesc);
        edt_ContactNo = findViewById(R.id.edt_ContactNo);
        contactName = findViewById(R.id.contactName);
        imageView1 = findViewById(R.id.imageView);
        imageView_buisness = findViewById(R.id.imageView_buisness);
        edt_productName = findViewById(R.id.edt_productName);
        btn_add_to_enquriy = findViewById(R.id.btn_add_to_enquriy);
        cardView_offer = findViewById(R.id.cardView_offer);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        btn_business_details = findViewById(R.id.btn_business_details);
        tv_all_sellers = findViewById(R.id.tv_all_sellers);
        ll_features = findViewById(R.id.ll_features);
        //    prepareSlider();
        btn_business_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ActivityBusinessDetail.class);
                startActivity(intent);
            }
        });
        if (Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_TYPE).equals("1")) {
            btn_add_to_enquriy.setVisibility(View.VISIBLE);
            btn_add_to_cart.setVisibility(View.VISIBLE);

        } else {
            btn_add_to_enquriy.setVisibility(View.GONE);
            btn_add_to_cart.setVisibility(View.GONE);
        }

        ArrayList aList= new ArrayList(Arrays.asList(productArrayList.get(position).getMultiple_images().split(",")));
        aList.add(0,  productArrayList.get(position).getProduct_image());
        for(int i=0;i<aList.size();i++)
        {
            Log.d("Multiple", "onCreate: "+aList.get(i));
        }

        recycler_view_product = findViewById(R.id.recycler_view_vendor);
        recycler_view_images = findViewById(R.id.recycler_view_images);

        mAdapterImages = new MultipleImagesAdapter(ProductDetailsActivity.this, aList, this);
        // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
        recycler_view_images.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view_images.setItemAnimator(new DefaultItemAnimator());
        recycler_view_images.setAdapter(mAdapterImages);
        mAdapterImages.notifyDataSetChanged();

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ImageViewZoom.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", aList);
                bundle.putInt("position", positionForImage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        edt_name.setText(productArrayList.get(position).getName());
        edt_businessName.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.BUSINESS_NAME));
        edt_businessDesc.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.BUSINESS_DEC));
        edt_ContactNo.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.CONTACT_NO));
        edt_productName.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.PRODUCT_NAME));
        edt_desc.setText(productArrayList.get(position).getDescription());

        if(!productArrayList.get(position).getFeatures().equals("")) {
            ll_features.setVisibility(View.VISIBLE);
            edt_features.setText(productArrayList.get(position).getFeatures());
        }else {
            ll_features.setVisibility(View.GONE);
        }
        if(productArrayList.get(position).getFeatures().isEmpty()){
            tv_features_lable.setVisibility(View.GONE);
        }else {
            tv_features_lable.setVisibility(View.VISIBLE);
        }
        contactName.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.CONTACT_NAME));
        String image = productArrayList.get(position).getProduct_image();
        Log.d("Image", "onCreate: " + image);

        DisplayMetrics metricscard = getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        // imageView1.getLayoutParams().width = (int) (cardwidth / 1.5);
        imageView1.getLayoutParams().height = (int) (cardheight / 2);
        if (image.equals("") || image.equals(null) || image.equals("null")) {

            Glide.with(ProductDetailsActivity.this)
                    .load(R.drawable.no_image_available)

                    .into(imageView1);
        } else {
            Glide.with(ProductDetailsActivity.this)
                    .load(MyConfig.JSON_BusinessImage + image)

                    .into(imageView1);
        }

       /* String image_banner = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.BUISNESS_BANNER);
        if (image_banner.equals("") || image_banner.equals(null) || image_banner.equals("null")) {
            Glide.with(ProductDetailsActivity.this)
                    .load(R.drawable.no_image_available)
                    .skipMemoryCache(true)
                    .into(imageView_buisness);
        } else {
            Glide.with(ProductDetailsActivity.this)
                    .load(MyConfig.JSON_BusinessPath + image_banner)
                    .skipMemoryCache(true)
                    .into(imageView_buisness);
        }*/
        //recycler_view_product = findViewById(R.id.recycler_view_product);
        init();
        btn_add_to_enquriy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent=new Intent(ProductDetailsActivity.this,EnquiryActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(ProductDetailsActivity.this, GenrateEnquiry.class);
                startActivity(intent);
            }
        });
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInToCart();

            }
        });
        txt_offer = findViewById(R.id.txt_offer);
        String offer_name = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_NAME);
        String offer_type = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_TYPE);
        if (offer_type == null) {
            cardView_offer.setVisibility(View.GONE);
        } else {
            cardView_offer.setVisibility(View.VISIBLE);
            if (offer_type.equals("1")) {
                String offer_price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offer.setText(offer_price + "%" + "off");
            } else if (offer_type.equals("2")) {
                String offer_price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offer.setText(offer_price + "%" + "Upto off");
            } else {
                String offer_price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offer.setText(offer_price + "off");
            }
        }
        image_like = findViewById(R.id.image_like);
        //   image_dislike=findViewById(R.id.image_dislike);

        String is_fav = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.IS_FAVORITE);

        if (is_fav != null && !is_fav.equals("null") && !is_fav.isEmpty()) {
            if (is_fav.equals("1")) {
                flag = 1;
                image_like.setImageDrawable(ProductDetailsActivity.this.getResources().getDrawable(R.drawable.like));
            } else {
                flag = 0;
                image_like.setImageDrawable(ProductDetailsActivity.this.getResources().getDrawable(R.drawable.unlike));

            }
        }
     /*   else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_like.setImageDrawable(ProductDetailsActivity.this.getDrawable(R.drawable.like_white));
            }
        }*/

      /*  image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(flag==0)
                    {
                        Toast.makeText(ProductDetailsActivity.this, "click", Toast.LENGTH_SHORT).show();
                        image_dislike.setVisibility(View.VISIBLE);
                        image_like.setVisibility(View.GONE);
                        //image_like.setImageDrawable(ProductDetailsActivity.this.getDrawable(R.drawable.like_white));
                        flag=1;
                        String product_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this,Constants.PRODUCT_ID);

                        sendData(product_id);

                    }

                }
            }
        });*/
        image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (flag == 1) {
                    image_like.setImageDrawable(ProductDetailsActivity.this.getResources().getDrawable(R.drawable.unlike));
                    flag = 0;
                    String product_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.PRODUCT_ID);
                    sendData(product_id);

                } else {
                    image_like.setImageDrawable(ProductDetailsActivity.this.getResources().getDrawable(R.drawable.like));
                    flag = 1;
                    String product_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.PRODUCT_ID);
                    sendData(product_id);
                }

            }
        });

        ll_offerDetails = findViewById(R.id.ll_offerDetails);
        ll_offerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  showPopUp();
            }
        });
        imageView_offerDetails = findViewById(R.id.imageView_offerDetails);
        offerDetail = findViewById(R.id.offerDetail);
        imageView_offerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 0) {
                    offerDetail.setVisibility(View.VISIBLE);
                    flag = 1;
                } else {
                    offerDetail.setVisibility(View.GONE);
                    flag = 0;
                }
            }
        });

        txt_offerName = findViewById(R.id.txt_offerName);
        txt_offerDescription = findViewById(R.id.txt_offerDescription);
        txt_offerValid = findViewById(R.id.txt_offerValid);
        txt_offerDiscount = findViewById(R.id.txt_offerDiscount);

        txt_offerName.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_NAME));
        txt_offerDescription.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_DETAILS));
        txt_offerValid.setText("Offer valid till" + Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_VALIDITY));
        String offerType = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_TYPE);
        if (offerType == null) {
            cardView_offer.setVisibility(View.GONE);
        } else {
            cardView_offer.setVisibility(View.VISIBLE);
            if (offerType.equals("1")) {
                String price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offerDiscount.setText("Maximum Discount " + price + "%");
            } else if (offerType.equals("2")) {
                String price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offerDiscount.setText("Maximum Discount " + "Rs." + price);
            } else {
                String price = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE);
                txt_offerDiscount.setText("Maximum Discount " + "Upto " + price + "%");
            }
        }



    }

    private void prepareSlider() {
        sliderImages1.add("http://godapark.com/jmbr/assets/img/app_slider/jmbr_slide1.jpg");
        sliderImages1.add(" http://godapark.com/jmbr/assets/img/app_slider/jmbr_slide2.jpg");


        SliderLayout imgSliderLayout = (SliderLayout) findViewById(R.id.imgSliderLayout1);
        //ArrayList<String> img_maps = new ArrayList<>();
        //  Log.d("my_tag", "prepareSlider: " + sliderImages);


        for (int i = 0; i < sliderImages1.size(); i++) {
            index++;
            DefaultSliderView defaultSliderView = new DefaultSliderView(ProductDetailsActivity.this);
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
        imgSliderLayout.setDuration(3000);
    }

    private void sendData(String product_id) {
        String user_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_ID);
        Log.d("id", "sendData: " + user_id + "," + product_id);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.sendData(user_id, product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = "";
                    output = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        String reason = jsonObject.getString("reason");
                        if (result) {
                            Toast.makeText(ProductDetailsActivity.this, reason, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, reason, Toast.LENGTH_SHORT).show();
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

    private void addInToCart() {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_ID);
        String business_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.BUSINESS_ID);
        String product_id = Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.PRODUCT_ID);

        Call<ResponseBody> responseBodyCall = getOrderApi.addToCart(business_id, product_id, user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    try {
                        JSONObject object = new JSONObject(output);
                        boolean result = object.getBoolean("result");
                        String resaon = object.getString("reason");
                        if (result) {
                            getMyCartData();
                            Toast.makeText(ProductDetailsActivity.this, "Product Added Into Cart Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, resaon, Toast.LENGTH_SHORT).show();

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

    private void init() {
        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);


        sliderImages1.add("http://godapark.com/jmbr/assets/img/app_slider/jmbr_slide1.jpg");
        sliderImages1.add("http://godapark.com/jmbr/assets/img/app_slider/jmbr_slide1.jpg");

        Log.d("Vijendra", "init: "+sliderImages1.size());
        getMyCartData();

    }

    @Override
    public void onItemClick(int position, String path) {
        positionForImage = position;
        if (path.equals("") || path.equals(null) || path.equals("null")) {

            Glide.with(ProductDetailsActivity.this)
                    .load(R.drawable.no_image_available)

                    .into(imageView1);
        } else {
            Glide.with(ProductDetailsActivity.this)
                    .load(MyConfig.JSON_BusinessImage + path)

                    .into(imageView1);
        }
    }
    
    private void getProductData() {
        progressDialog.show();
        vendorByProductList.clear();

        GetOrderApi api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getRecommended(
                Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_ID),
                Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.DISTRICT),
                productArrayList.get(position).getId()
                );

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org123", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("vendorsList");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                vendorByProductList.add(new VendorByProduct(obj));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }


                        // edt_desc.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this,"description"));
                        edt_price.setText(Constants.rs + "" + vendorByProductList.get(0).getSelling_price() + "/-");




                    } else {

                    }

                    Log.d("VijeAdpterCall", "onResponse: "+vendorByProductList.size());
                    mAdapter = new VendorByProductAdapter(ProductDetailsActivity.this, vendorByProductList);
                    // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
                    recycler_view_product.setLayoutManager(mLayoutManager);
                    recycler_view_product.setItemAnimator(new DefaultItemAnimator());
                    recycler_view_product.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    progressDialog.dismiss();
                    if(vendorByProductList.size()==0){
                        tv_all_sellers.setVisibility(View.GONE);
                    }else {
                        tv_all_sellers.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
             /*   progressBar_endless.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);*/
                //  swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

   /* @Override
    public void onRefresh() {
        if (productList1List.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recycler_view_product.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                productList1List.clear();
                mAdapter.notifyDataSetChanged();
                page_count = 1;
                remainingCount = 0;
                init();
             //   check_connection();
             //   progressBar_endless.setVisibility(View.GONE);
             //   progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;
            init();
         //   check_connection();
           // progressBar_endless.setVisibility(View.GONE);
           // progressView.setVisibility(View.GONE);
           // noRecordLayout.setVisibility(View.GONE);


        }
    }*/
   
    private void showPopUp() {
        dialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);
        View layoutView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        TextView offerName, txt_offerType, txt_validity, txt_price, txt_terms, txt_code;
        offerName = layoutView.findViewById(R.id.offerName);
        txt_offerType = layoutView.findViewById(R.id.txt_offerType);
        txt_validity = layoutView.findViewById(R.id.txt_validity);
        txt_price = layoutView.findViewById(R.id.txt_price);
        txt_terms = layoutView.findViewById(R.id.txt_terms);
        txt_code = layoutView.findViewById(R.id.txt_code);

        offerName.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_NAME));
        txt_offerType.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_TYPE));
        txt_validity.setText("Offer Expires" + " " + Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_VALIDITY));
        txt_price.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_PRICE));
        txt_code.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_CODE));
        txt_terms.setText(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.OFFER_TERM));
        Button dialogButton = layoutView.findViewById(R.id.btn_ok);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        //   alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addItemToCart")
        public Call<ResponseBody> addToCart(
                @Field("business_id") String business_id,
                @Field("product_id") String product_id,
                @Field("user_id") String user_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/vendorsListByProduct")
        public Call<ResponseBody> getRecommended(
                @Field("user_id") String user_id,
                @Field("district") String district,
                @Field("product_id") String product_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/addRemoveFavourite")
        Call<ResponseBody> sendData(
                @Field("user_id") String user_id,
                @Field("product_id") String product_id

        );
    }

    private void ViewDialogue() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogue_zoom_image);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        final ImageView imageView_Zoom = (ImageView) dialog.findViewById(R.id.imageView_Zoom);
        Glide.with(ProductDetailsActivity.this)
                .load(Shared_Preferences.getPrefs(ProductDetailsActivity.this, "image"))

                .into(imageView_Zoom);

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView_Zoom);
        pAttacher.update();

        dialog.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


        //Action(MyConfig.JSON_BASE_URL + MyConfig.SUB_URL + "Api_Trans_Opportunity/Add_Won", dialog,1);*/
        //  dialog.dismiss();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MaterialDialog; //style id
        dialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == android.view.KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            //overridePendingTransition(R.animator.left_right, R.animator.right_left);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        if(Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_TYPE).equals("2")){
            menuItem.setVisible(false);
        }else {
            menuItem.setVisible(true);
        }

        MenuItem myHome = menu.findItem(R.id.action_home);
        myHome.setVisible(true);
        myHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                return false;
            }
        });


        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ProductDetailsActivity.this, Activity_My_Cart.class);
                startActivity(intent);
                return false;
            }
        });


        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                Intent intent = new Intent(ProductDetailsActivity.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
               onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void getMyCartData() {
        Activity_My_Cart.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(Activity_My_Cart.GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(ProductDetailsActivity.this, Constants.REG_ID);
        Log.d("user_id", "getMyCartData: "+user_id);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyCart(user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("ResponseCart", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if(result)
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("cart_data");
                            JSONArray array=jsonObject1.getJSONArray("product");
                            Log.d("Badge", "onResponse: "+array.length());
                            mCartItemCount = array.length();
                            setupBadge();
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
    
}



