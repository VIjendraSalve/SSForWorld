package com.windhans.client.forworld.Activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import java.util.Objects;

public class EnquiryDetailsActivity1 extends AppCompatActivity {
    TextView txt_product_name,txt_brand,txt_description,txt_price,txt_purpose,txt_status;
    ImageView imageView_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_details1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.enquirydetails));

        getViews();
    }

    private void getViews() {
        txt_product_name=findViewById(R.id.txt_product_name);
        txt_brand=findViewById(R.id.txt_brand);
        txt_description=findViewById(R.id.txt_description);
        txt_price=findViewById(R.id.txt_price);
        txt_purpose=findViewById(R.id.txt_purpose);
        txt_status=findViewById(R.id.txt_status);

        imageView_product=findViewById(R.id.imageView_product);

        String product_name= Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this, Constants.PRODUCT_NAME);
        String brand=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.BRAND);
        String description=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.DESCRIPTION);
        String price=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.PRICE);
        String purpose=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.PURPOSE);
        String image=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.PRODUCT_IMAGE);
        String status=Shared_Preferences.getPrefs(EnquiryDetailsActivity1.this,Constants.STATUS);

        txt_product_name.setText(product_name);
        txt_brand.setText(brand);
        txt_description.setText(description);
        txt_price.setText(price);
        txt_purpose.setText(purpose);

        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(EnquiryDetailsActivity1.this)
                    .load(MyConfig.JSON_BusinessImage +image)

                    .into(imageView_product);
        } else {
            Glide.with(EnquiryDetailsActivity1.this)
                    .load(R.drawable.no_image_available)

                    .into(imageView_product);
        }
        if(status.equals("0"))
        {
           txt_status.setText(this.getResources().getString(R.string.pending));
            txt_status.setTextColor(EnquiryDetailsActivity1.this.getResources().getColor(R.color.blue));
        }
        else  if(status.equals("1"))
        {
            txt_status.setText(this.getResources().getString(R.string.complete));
            txt_status.setTextColor(EnquiryDetailsActivity1.this.getResources().getColor(R.color.green_a));
        }
        else
        if(status.equals("2"))
        {
            txt_status.setText(this.getResources().getString(R.string.progress));
            txt_status.setTextColor(EnquiryDetailsActivity1.this.getResources().getColor(R.color.primary_orange));
        }
        else   if(status.equals("3"))
        {
            txt_status.setText(this.getResources().getString(R.string.rejected));
            txt_status.setTextColor(EnquiryDetailsActivity1.this.getResources().getColor(R.color.red));
        }
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
