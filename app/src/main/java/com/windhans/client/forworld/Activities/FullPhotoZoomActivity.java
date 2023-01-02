package com.windhans.client.forworld.Activities;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otaliastudios.zoom.ZoomImageView;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;

public class FullPhotoZoomActivity extends AppCompatActivity {

    private ImageView iv_photo;
    private RelativeLayout ivMainImage;
    private String imagePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo_zoom);
        imagePath = getIntent().getStringExtra(Constants.Image);
        toolbar();
        init();
    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.photo));

      /*  imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        iv_photo = findViewById(R.id.iv_photo);
        ivMainImage = findViewById(R.id.ivMainImage);

       /* DisplayMetrics metricscard = getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = metricscard.heightPixels;
        iv_photo.getLayoutParams().height = (int) (cardheight / 5.5);
        iv_photo.getLayoutParams().width = (int) (cardwidth / 2.5);
        ivMainImage.getLayoutParams().width = (int) (cardwidth / 1);*/

        Log.d("Image", "init: "+imagePath);
        Glide.with(FullPhotoZoomActivity.this)
                .load(imagePath)
                .apply(new RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.no_image_available))
                .into(iv_photo);
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
}