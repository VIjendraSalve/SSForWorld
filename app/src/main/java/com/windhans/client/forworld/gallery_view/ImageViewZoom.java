package com.windhans.client.forworld.gallery_view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.MyConfig;

import java.util.ArrayList;
import java.util.List;





//import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;

//import com.windhans.client.courier.my_library.ImageLoadingUtils;

public class ImageViewZoom extends AppCompatActivity implements View.OnClickListener {

   // private ADDPOST addpost;
    private List<String> img_list = new ArrayList<String>();
    private ExtendedViewPager mViewPager;
    private ImageView backward,forward;
    //private OkHttpClient mOkHttpClient;
    private ProgressBar progressBar;
    //private ImageLoadingUtils utils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.images);

        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            img_list=bundle.getStringArrayList("images");
        }else {

        }

        Log.d("ImageSize", "onCreate: "+img_list.size());
        backward = (ImageView) findViewById(R.id.backward);
        forward = (ImageView) findViewById(R.id.forward);
        backward.setOnClickListener(this);
        forward.setOnClickListener(this);
        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter());
        mViewPager.setCurrentItem(bundle.getInt("position"));
        //utils=new ImageLoadingUtils(getApplicationContext());
    }



    @Override
    public void onClick(View v)
    {
        if (v.getId()== R.id.backward)
        {mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);}
        if (v.getId()== R.id.forward)
        {mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);}
        /*if (mViewPager.getCurrentItem() == 0) {backward.setVisibility(View.GONE);}
        if (mViewPager.getCurrentItem() == img_list.size()) {forward.setVisibility(View.GONE);}*/
    }



    public class TouchImageAdapter extends PagerAdapter {

       /* private String[] images = { addpost.getRoom_image1(),addpost.getRoom_image2(), addpost.getRoom_image3(), addpost.getRoom_image4(),
                addpost.getRoom_image5()};*/
        //private int[] images = { R.drawable.background, R.drawable.background, R.drawable.background, R.drawable.background, R.drawable.background };

        @Override
        public int getCount() {
            return img_list.size();}

        @Override
        public View instantiateItem(ViewGroup container,final int position) {
            final TouchImageView img = new TouchImageView(container.getContext());
            //img.setImageResource(images[position]);
           /* for (String s:images)
            {
                if (s.isEmpty()){}else{
                    img_list.add(s);
               }
            }*/
            Log.d("my_tag", "processFinish: "+img_list.get(position));
            //import com.bumptech.glide.Glide;
            /*Glide.with(getApplicationContext()).load(img_list.get(position))
                    .thumbnail(0.5f)
                    .crossFade()
                    .into(img);*/
            //import com.squareup.picasso.Picasso;
            /*Picasso.with(ImageViewZoom.this)
                    .load(img_list.get(position))
                    //.placeholder(R.drawable.background)
                    .error(R.mipmap.ic_launcher).into(img);*/

           /* Glide.with(activity).load(MyConfig.PRODUCT_IMG_URL+imgList.get(position))
                    .thumbnail(0.5f)
                    .crossFade()
                    .into(holder.iv_tag_img);*/

            /*Glide.with(ImageViewZoom.this).load(MyConfig.PRODUCT_IMG_URL+img_list.get(position)).into(img);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            mOkHttpClient = new OkHttpClient();

            final ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    int progress = (int) ((100 * bytesRead) / contentLength);

                    // Enable if you want to see the progress with logcat
                    // Log.v(LOG_TAG, "Progress: " + progress + "%");
                    progressBar.setProgress(progress);
                    if (done) {
                        Log.i("GifActivity", "Done loading");
                    }
                }
            };
            mOkHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });



            Glide.get(ImageViewZoom.this)
                    .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));
            Glide.with(ImageViewZoom.this)
                    .load(MyConfig.PRODUCT_IMG_URL+img_list.get(position))
                    // Disabling cache to see download progress with every app load
                    // You may want to enable caching again in production
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);*/

            Log.d("ImagePath1", "instantiateItem: "+MyConfig.JSON_BusinessImage+img_list.get(position));
           /* Glide.with(ImageViewZoom.this)
                    .load(MyConfig.JSON_BusinessImage+img_list.get(position))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>(500,500) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            //bm=resource;
                            img.setImageBitmap(resource); // Possibly runOnUiThread()
                        }
                    });*/
            Glide.with(ImageViewZoom.this)
                    .load(MyConfig.JSON_BusinessImage+img_list.get(position))
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            Bitmap mBitmap = ((BitmapDrawable) resource).getBitmap();
                            img.setImageBitmap(mBitmap);
                        }
                    });

            /*Picasso.with(ImageViewZoom.this).load(MyConfig.PRODUCT_IMG_URL+img_list.get(position)).into(img, new Callback(){
                @Override
                public void onSuccess() {}

                @Override
                public void onError() {
                    //import android.graphics.Bitmap;
                    //Bitmap bitmap = utils.decodeBitmapFromPath(img_list.get(position));
                    //img.setImageBitmap(bitmap);
                }
            });*/




                    //new LoadImage(img).execute(MyConfig.IMG_URL+s);
                   // Log.d("img-->",MyConfig.IMG_URL+images[position]);







            /*((ViewPager) container).addView(imageView, 0);
            return imageView;*/

            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return img;
        }





        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
