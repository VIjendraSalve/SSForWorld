package com.windhans.client.forworld.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Adapter.MultipleImagesProductAdapter;
import com.windhans.client.forworld.Model.VendorProductData;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityMyProductDetails1 extends AppCompatActivity implements MultipleImagesProductAdapter.RecyclerViewItemInterface{

    TextView tv_product_name,textView_brand,textView_price,textView_description,textView_originalCost;
    ImageView image_view;
    private Bundle bundle;
    List<VendorProductData> vendorProductDataList;
    int position1;
    ArrayList aList;
    RecyclerView recycler_view;
    MultipleImagesProductAdapter adapterMutiImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_details1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


       /* imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.productdetails));

        position1 = Integer.parseInt(getIntent().getStringExtra("Position"));
        vendorProductDataList = getIntent().getParcelableArrayListExtra("productList");
        String product_id=vendorProductDataList.get(position1).getId();
        Log.d("MultipleImages_list", "onCreate: " +   vendorProductDataList.get(position1).getMultiple_images());

        aList = new ArrayList(Arrays.asList(vendorProductDataList.get(position1).getMultiple_images().split(",")));
        Log.d("list", "ArrayListSize: "+aList.size());
        aList.add(0,MyConfig.JSON_BusinessImage+Shared_Preferences.getPrefs(ActivityMyProductDetails1.this, Constants.PRODUCT_IMAGE) );
        Log.d("list", "ArrayListSize: "+aList.size());

        recycler_view=findViewById(R.id.recycler_view_product_image);


        if(vendorProductDataList.get(position1).getMultiple_images().isEmpty())
        {
            recycler_view.setVisibility(View.GONE);
        }
        else {

            adapterMutiImages = new MultipleImagesProductAdapter(ActivityMyProductDetails1.this, aList, this);
            //   RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            //  GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
            //  recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(adapterMutiImages);
            adapterMutiImages.notifyDataSetChanged();
        }

        image_view=findViewById(R.id.image_view);
        textView_originalCost=findViewById(R.id.textView_originalCost);
        tv_product_name=findViewById(R.id.tv_product_name);
        textView_brand=findViewById(R.id.textView_brand);
        textView_price=findViewById(R.id.textView_price);
        textView_description=findViewById(R.id.textView_description);
        Uri data = getIntent().getData();
        if (data != null) {
            String scheme = data.getScheme(); // "http"
            String host = data.getHost(); // "twitter.com"
            List<String> params = data.getPathSegments();
            String first = params.get(0); // "status"
            //String second = params.get(1); // "1234"
            Log.d("my_tag", "onCreate: " + data + " " + data.toString().replace("http://www.ssforworld.info/", ""));
            bundle.putString("product_id", params.get(0));
            //bundle.putString("is_combo", params.get(1));
            bundle.putString("price", "2");
            bundle.putString("subgategory_id", params.get(3));
            bundle.putString("business_id", params.get(4));
          //  bundle.putInt("share", 1);
            //products.set
        }
        setData();
        DisplayMetrics metricscard = getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
     //   image_view.getLayoutParams().width = (int) (cardwidth / 1.5);
     //   image_view.getLayoutParams().height = (int) (cardheight / 5);
        image_view.getLayoutParams().height = (int) (cardheight / 2);
        String image= Shared_Preferences.getPrefs(ActivityMyProductDetails1.this, Constants.PRODUCT_IMAGE);
        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(ActivityMyProductDetails1.this)
                    .load(MyConfig.JSON_BusinessImage + image)

                    .into(image_view);
        } else {
            Glide.with(ActivityMyProductDetails1.this)
                    .load(R.drawable.no_image_available)

                    .into(image_view);
        }
    }

    private void setData() {
        tv_product_name.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails1.this,Constants.PRODUCT_NAME));
        textView_brand.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails1.this,Constants.BRAND));
        textView_price.setText(Constants.rs+vendorProductDataList.get(position1).getSelling_price());
        textView_description.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails1.this,Constants.DESCRIPTION));
        textView_originalCost.setText(Constants.rs+vendorProductDataList.get(position1).getPrice());
        textView_originalCost.setPaintFlags(textView_originalCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
        getMenuInflater().inflate(R.menu.sacn_button, menu);
        MenuItem myActionScan=menu.findItem(R.id.scan_button);

        myActionScan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(ActivityMyProductDetails1.this,BarcodeScanActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;

    };

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

    @Override
    public void onItemClick(String path) {

    }
}
