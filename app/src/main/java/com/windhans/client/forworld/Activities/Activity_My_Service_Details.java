package com.windhans.client.forworld.Activities;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Adapter.MultipleServiceImagesAdapter;
import com.windhans.client.forworld.Model.MyServiceModel;
import com.windhans.client.forworld.Model.Service_Images;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;


import java.util.ArrayList;

public class Activity_My_Service_Details extends AppCompatActivity implements MultipleServiceImagesAdapter.RecyclerViewItemInterface {
    TextView edt_ServiceName, edt_subcategory, edt_category, tv_description;
    RecyclerView recycler_view_images;
    ImageView imageView, imageView1;
    ArrayList<MyServiceModel> serviceDetailModelArrayList = new ArrayList<>();
    int position1;
    private int position, positionForImage = 0;
    String image_path = "";
    ArrayList<Service_Images> service_imagesArrayList = new ArrayList<>();
    MultipleServiceImagesAdapter multipleServiceImagesAdapter;
    LinearLayout ll_category,ll_subcategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my__service__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll_category=findViewById(R.id.ll_category);
        ll_subcategory=findViewById(R.id.ll_subcategory);
        edt_ServiceName = findViewById(R.id.edt_ServiceName);
        edt_category = findViewById(R.id.edt_category);
        edt_subcategory = findViewById(R.id.edt_subcategory);
        tv_description = findViewById(R.id.tv_description);
        recycler_view_images = findViewById(R.id.recycler_view_images);
        imageView1 = findViewById(R.id.imageView);

        serviceDetailModelArrayList = Activity_My_Service_Details.this.getIntent().getParcelableArrayListExtra(Constants.Service_LIst);
        Log.d("size", "onCreate: " + serviceDetailModelArrayList.size());
        position1 = this.getIntent().getIntExtra(Constants.Position, 0);


        if (serviceDetailModelArrayList.get(position1).getName().equals("") || serviceDetailModelArrayList.get(position1).getName().equals(null) || serviceDetailModelArrayList.get(position1).getName().equals("null")) {
            title.setText(this.getResources().getString(R.string.servicedetails));
            edt_ServiceName.setVisibility(View.GONE);
        } else {

            title.setText(serviceDetailModelArrayList.get(position1).getName());
            edt_ServiceName.setText(serviceDetailModelArrayList.get(position1).getName());
        }
        if (serviceDetailModelArrayList.get(position1).getSub_category().equals("") || serviceDetailModelArrayList.get(position1).getSub_category().equals(null) || serviceDetailModelArrayList.get(position1).getSub_category().equals("null")) {
            edt_subcategory.setVisibility(View.GONE);
            ll_subcategory.setVisibility(View.GONE);
        } else {
            edt_subcategory.setText(serviceDetailModelArrayList.get(position1).getSub_category());

        }
        if (serviceDetailModelArrayList.get(position1).getCategory_name().equals("") || serviceDetailModelArrayList.get(position1).getCategory_name().equals(null) || serviceDetailModelArrayList.get(position1).getCategory_name().equals("null")) {
            edt_category.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
        } else {
            edt_category.setText(serviceDetailModelArrayList.get(position1).getCategory_name());
        }
        if (serviceDetailModelArrayList.get(position1).getDescription().equals("") || serviceDetailModelArrayList.get(position1).getDescription().equals(null) || serviceDetailModelArrayList.get(position1).getDescription().equals("null")) {
            tv_description.setVisibility(View.GONE);
        } else {
            tv_description.setText(serviceDetailModelArrayList.get(position1).getDescription());
        }
        DisplayMetrics metricscard = getResources().getDisplayMetrics();
        int cardwidth = metricscard.widthPixels;
        int cardheight = (int) (metricscard.heightPixels);
        // imageView1.getLayoutParams().width = (int) (cardwidth / 1.5);
        imageView1.getLayoutParams().height = (int) (cardheight / 3);
        if (serviceDetailModelArrayList.get(position1).getBanner_image().equals("") || serviceDetailModelArrayList.get(position1).getBanner_image().equals(null) || serviceDetailModelArrayList.get(position1).getBanner_image().equals("null")) {

            Glide.with(Activity_My_Service_Details.this)
                    .load(R.drawable.no_image_available)
                    .into(imageView1);
        } else {
            Glide.with(Activity_My_Service_Details.this)
                    .load(MyConfig.JSON_ServicePath + serviceDetailModelArrayList.get(position1).getBanner_image())
                    .into(imageView1);
        }
        // ArrayList aList= new ArrayList(Arrays.asList(serviceDetailModelArrayList.get(position1).getMulti_image().split(",")));
       /* ArrayList aList= new ArrayList(Arrays.asList(serviceDetailModelArrayList.get(position1).getMulti_image().split(",")));
        for(int i=0;i<aList.size();i++)
        {
            Log.d("Multiple", "onCreate: "+aList.get(i));
        }*/
        service_imagesArrayList = serviceDetailModelArrayList.get(position1).getService_imagesArrayList();
        Log.d("sizeImages", "onCreate: " + service_imagesArrayList.size());
        recycler_view_images = findViewById(R.id.recycler_view_images);
        multipleServiceImagesAdapter = new MultipleServiceImagesAdapter(Activity_My_Service_Details.this, service_imagesArrayList, this);
        // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
        recycler_view_images.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view_images.setItemAnimator(new DefaultItemAnimator());
        recycler_view_images.setAdapter(multipleServiceImagesAdapter);
        multipleServiceImagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, String path) {

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


       /* if (item.getItemId() == R.id.reply_thread) {
            ReplayDialogue();
        }*/
        //overridePendingTransition(R.animator.left_right, R.animator.right_left);
        return super.onOptionsItemSelected(item);
    }
}
