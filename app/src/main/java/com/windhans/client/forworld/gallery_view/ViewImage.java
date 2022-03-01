package com.windhans.client.forworld.gallery_view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.windhans.client.forworld.R;


public class ViewImage extends AppCompatActivity {
	
	private TouchImageView image;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle=getIntent().getExtras();
		image = (TouchImageView) findViewById(R.id.img);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		Picasso.with(getApplicationContext()).load(bundle.getString("product_image_url")).into(image, new Callback(){
			@Override
			public void onSuccess() {
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				progressBar.setVisibility(View.GONE);
				image.setImageResource(R.mipmap.ic_launcher);
				//import android.graphics.Bitmap;
				//Bitmap bitmap = utils.decodeBitmapFromPath(img_list.get(position));
				//img.setImageBitmap(bitmap);
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
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
