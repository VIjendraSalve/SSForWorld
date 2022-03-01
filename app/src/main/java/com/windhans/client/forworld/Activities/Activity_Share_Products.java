package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.windhans.client.forworld.Adapter.MyProductShareAdapter;
import com.windhans.client.forworld.Model.VendorProductData;
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

public class Activity_Share_Products extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    List<VendorProductData> vendorProductDataList;
    MyProductShareAdapter myProductAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private Button btnRetry;
    private String query = "";
    private Handler mHandler;
    private ImageView imageView;
    private String search;
    private String s;
    private SearchView mSearchView;
    private BroadcastReceiver mMessageReceiver;
    List<String> stringArrayListExtra=new ArrayList<>();
    Button btn_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__share__products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.myproduct));

        noRecordLayout = findViewById(R.id.noRecordLayout);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = findViewById(R.id.progress_view);
        progressBar_endless = findViewById(R.id.progressBar_endless);
        btn_share = findViewById(R.id.btn_share);
        check_connection();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("share-message"));




    }

    /*@SuppressLint("RestrictedApi")
    private Bitmap getBitmapFromUrl(String url1) {

        final Bitmap[] bitmap = {null};
        Glide.with(Activity_Share_Products.this)
                .load(url1)
                .asBitmap()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bimtapImage=resource;
                        bitmap[0] =bimtapImage;
                        //Convert this bimtapImage to byte array
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
        return bitmap[0];
    }*/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            check_connection();
        }
    }

    @Override
    public void onRefresh() {
        if (vendorProductDataList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                vendorProductDataList.clear();
                myProductAdapter.notifyDataSetChanged();
                page_count = 1;
                remainingCount = 0;

                check_connection();
                progressBar_endless.setVisibility(GONE);
                progressView.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            remainingCount = 0;
            page_count = 1;
            check_connection();
            progressBar_endless.setVisibility(GONE);
            progressView.setVisibility(GONE);
            noRecordLayout.setVisibility(GONE);


        }
    }

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {
            noConnectionLayout.setVisibility(GONE);
            noRecordLayout.setVisibility(GONE);
            init();
        } else {

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


        recyclerView = findViewById(R.id.recycler_view);


        vendorProductDataList = new ArrayList<>();
        myProductAdapter = new MyProductShareAdapter(Activity_Share_Products.this, vendorProductDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myProductAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && vendorProductDataList.size() > 4) {
                        if (remainingCount > 0) {
                            Log.d("data", "onScrollStateChanged: " + remainingCount);
                            Log.d("data", "onScrollStateChanged: " + page_count);
                            Log.d("data", "onScrollStateChanged: " + lastVisibleItemPosition);
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getMyProductData();
                        }
                    }
                }
            }
        });

        vendorProductDataList.clear();
        myProductAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getMyProductData();

       /* btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Get extra data included in the Intent
                        stringArrayListExtra = intent.getStringArrayListExtra("share_product");
                        Log.d("string_size", "onCreate: "+stringArrayListExtra);
                    }
                };

                String   IMAGE_URL=MyConfig.JSON_BASE_URL + MyConfig.SSWORLD + "/assets/uploads/product/" +stringArrayListExtra.get(2);
                Log.d("image", "onMenuItemClick: "+IMAGE_URL);
                Bitmap bmp = getBitmapFromUrl(IMAGE_URL);

                String imgBitmapPath = MediaStore.Images.Media.insertImage(Activity_Share_Products.this.getContentResolver(), bmp, "title", null);
                //    String url = "https://www.gstatic.com/webp/gallery3/1.sm.png";

                if (imgBitmapPath != null) {
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                    String text = "ForWorld" + "\n\n" + "Proudct : " + stringArrayListExtra.get(0) + "\n\n" + stringArrayListExtra.get(1);
                    //Uri pictureUri = Uri.parse(MyConfig.JSON_PRODUCTS_URL+products.getProduct_banner_image());
                    Intent shareIntent = new Intent();
                    shareIntent.setType("image/*");
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share images..."));
                } else {
                    Toast.makeText(Activity_Share_Products.this, "Image is downloading Please wait...", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    private void getMyProductData() {

        String user_id = Shared_Preferences.getPrefs(this, Constants.REG_ID);

        ActivityMyProduct.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(ActivityMyProduct.GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyData(
                user_id,
                page_count,
                query
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("Data", "onResponse: " + output);
                    Log.d("Data", "onResponse: " + user_id);
                    Log.d("Data", "onResponse: " + page_count);
                    Log.d("Data", "onResponse: " + query);
                    JSONObject jsonObject = new JSONObject(output);

                    boolean result = jsonObject.getBoolean("result");

                    if (result) {
                        remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        Log.d("data", "Remaining count onResponse: " + remainingCount);
                        JSONArray jsonArray = jsonObject.getJSONArray("productData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            vendorProductDataList.add(new VendorProductData(object));
                            Shared_Preferences.setPrefs(Activity_Share_Products.this, Constants.SUB_ID, object.getString("subcategory_id"));
                            Log.d("sub", "onResponse: " + object.getString("subcategory_id"));

                        }


                    }
                    progressDialog.dismiss();
                    progressView.setVisibility(GONE);
                    //recyclerView.setAdapter(myProductAdapter);
                    myProductAdapter.notifyDataSetChanged();
                    if (vendorProductDataList != null && !vendorProductDataList.isEmpty() && !vendorProductDataList.equals("null")) {

                        noRecordLayout.setVisibility(GONE);

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

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/allProductsByVendor")
        Call<ResponseBody> getMyData(
                @Field("user_id") String user_id,
                @Field("page_no") int page_no,
                @Field("search") String search
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
        return super.onOptionsItemSelected(item);
    }
}
