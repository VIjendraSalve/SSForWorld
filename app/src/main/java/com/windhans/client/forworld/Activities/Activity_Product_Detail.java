package com.windhans.client.forworld.Activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;

public class Activity_Product_Detail extends AppCompatActivity {

    private ImageView img_product;
    private TextView txt_product_name, txt_brand, txt_product_details, txt_price;
    String product_name,price,brand,details,image, isProduct;
    private LinearLayout ll_brand, ll_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__product__detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Product Details");

       /* imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getViews();
    }

    private void getViews() {
        img_product = findViewById(R.id.img_product);
        txt_product_name = findViewById(R.id.txt_product_name);
        txt_brand = findViewById(R.id.txt_brand);
        txt_price = findViewById(R.id.txt_price);
        txt_product_details = findViewById(R.id.txt_product_details);
        ll_brand = findViewById(R.id.ll_brand);
        ll_price = findViewById(R.id.ll_price);

        product_name=getIntent().getStringExtra("Product_Name");
        price=getIntent().getStringExtra("Price");
        brand=getIntent().getStringExtra("Brand_name");
        details=getIntent().getStringExtra("Description");
        image=getIntent().getStringExtra("image");
        isProduct=getIntent().getStringExtra("IS_PRODUCT_IMAGE");

        Log.d("ISPRODUCT", "getViews: "+isProduct);

        DisplayMetrics metricscard = Activity_Product_Detail.this.getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        img_product.getLayoutParams().width = (int) (cardwidth / 3);
        img_product.getLayoutParams().height = (int) (cardheight / 5);

        if(isProduct == null) {
            if (image != null && !image.isEmpty() && !image.equals("null")) {
                Log.d("image", "response: ");

                Glide.with(Activity_Product_Detail.this)
                        .load(MyConfig.JSON_BusinessImage + image)
                        .into(img_product);
            } else {
                Glide.with(Activity_Product_Detail.this)
                        .load(R.drawable.no_image_available)

                        .into(img_product);
            }
        }else {
            if(!isProduct.equals("0")) {
                if (image != null && !image.isEmpty() && !image.equals("null")) {
                    Log.d("image", "response: ");

                    Glide.with(Activity_Product_Detail.this)
                            .load(MyConfig.JSON_ServicePath + image)
                            .into(img_product);
                } else {
                    Glide.with(Activity_Product_Detail.this)
                            .load(R.drawable.no_image_available)

                            .into(img_product);
                }
            }else {
                if (image != null && !image.isEmpty() && !image.equals("null")) {
                    Log.d("image", "response: ");

                    Glide.with(Activity_Product_Detail.this)
                            .load(MyConfig.JSON_BusinessImage + image)
                            .into(img_product);
                } else {
                    Glide.with(Activity_Product_Detail.this)
                            .load(R.drawable.no_image_available)

                            .into(img_product);
                }
            }
        }


        txt_product_name.setText(product_name);
        if(brand != null && !brand.equals("null")) {
            ll_brand.setVisibility(View.VISIBLE);
            txt_brand.setText(brand);
        }else {

            ll_brand.setVisibility(View.GONE);
        }
        if(price != null && !price.equals("null")){
            ll_price.setVisibility(View.VISIBLE);
            txt_price.setText(Constants.rs+" "+price);
        }else {
            ll_price.setVisibility(View.GONE);
        }
        txt_product_details.setText(details);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(R.animator.left_right, R.animator.right_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
