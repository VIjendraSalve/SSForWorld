package com.windhans.client.forworld.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.vipul.hp_hp.library.Layout_to_Image;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.windhans.client.forworld.Activities.ProfileActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class Activity_share_business extends AppCompatActivity {
    Layout_to_Image layout_to_image;
    Bitmap bitmap;
    ImageView imageView;
    RelativeLayout relativeLayout;
    CardView cardview;
    MyTextView_Poppins_Regular tv_user_name, tv_email, tv_mobile, tv_address;
    private CircleImageView iv_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_business);
        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.digitalvistingCard));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = (RelativeLayout) findViewById(R.id.container);
        imageView = (ImageView) findViewById(R.id.imageView2);
        cardview = findViewById(R.id.cardview);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_address = findViewById(R.id.tv_address);
        iv_banner=(CircleImageView) findViewById(R.id.iv_banner);
        tv_user_name.setText(Shared_Preferences.getPrefs(Activity_share_business.this, Constants.REG_NAME));
        tv_email.setText(Shared_Preferences.getPrefs(Activity_share_business.this, Constants.REG_EMAIL));
        tv_mobile.setText(Shared_Preferences.getPrefs(Activity_share_business.this, Constants.REG_MOBILE));
        tv_address.setText(Shared_Preferences.getPrefs(Activity_share_business.this, Constants.VendorAddress));

        layout_to_image = new Layout_to_Image(Activity_share_business.this, cardview);
        bitmap = layout_to_image.convert_layout();
        imageView.setImageBitmap(bitmap);

        String imageName = Shared_Preferences.getPrefs(Activity_share_business.this,Constants.business_banner);
        if (imageName != null && !imageName.isEmpty() && !imageName.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(Activity_share_business.this)
                    .load(MyConfig.JSON_BusinessPath + imageName)
                    .into(iv_banner);
        } else {
            Glide.with(Activity_share_business.this)
                    .load(R.drawable.no_image_available)
                    .into(iv_banner);
        }

        //String imgBitmapPath = MediaStore.Images.Media.insertImage(Activity_share_business.this.getContentResolver(), bitmap, "title", null);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionREAD_EXTERNAL_STORAGE(Activity_share_business.this)) {
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(Activity_share_business.this.getContentResolver(), bitmap, "title", null);


                    if (imgBitmapPath != null) {
                        Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                        Intent shareIntent = new Intent();
                        shareIntent.setType("image/*");
                        shareIntent.setAction(Intent.ACTION_SEND);
                        //   shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Share images..."));
                    } else {
                        Toast.makeText(Activity_share_business.this, "Image is downloading Please wait...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



      /*  if (imgBitmapPath != null) {
            Uri imgBitmapUri = Uri.parse(imgBitmapPath);
            Intent shareIntent = new Intent();
            shareIntent.setType("image/*");
            shareIntent.setAction(Intent.ACTION_SEND);
         //   shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share images..."));
        } else {
            Toast.makeText(Activity_share_business.this, "Image is downloading Please wait...", Toast.LENGTH_SHORT).show();
        }*/

    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", Activity_share_business.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
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
