package com.windhans.client.forworld.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.vipul.hp_hp.library.Layout_to_Image;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.StartActivities.LoginActivity;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;
import com.windhans.client.forworld.my_library.UtilityRuntimePermission;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class ProfileActivity extends UtilityRuntimePermission implements Camera.AsyncResponse {

    TextView   txt_city,txt_state,txt_country,txt_zoneName,txt_city1,txt_state1,txt_zone,
            txt_member,txt_spec,txt_address1,txt_address,txt_Address;

    MyTextView_Poppins_Bold txt_id, txt_contact_number, txt_email_id, tv_refrence_number;

    Button tv_edit_update_password,tv_logout, tv_edit_profile;
    Context context;
    CircleImageView img_profile;
    FloatingActionButton fab;
    Layout_to_Image layout_to_image;
    Bitmap bitmap;
   // CircleImageView imageView;
    RelativeLayout relativeLayout;
    CardView cardview;
    TextView txt_ref;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    Button ll_upload_image1;
    private String profile_image_name = "", profile_image_path = "";
    private ProgressDialog progressDialog;
    private Camera camera;
    CardView card_reference;
    private  CircleImageView imageView;
    private ImageView ll_upload_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.myprofile));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=ProfileActivity.this;
        getView();

        camera = new Camera(this);
        String user_type = (Shared_Preferences.getPrefs(ProfileActivity.this, Constants.REG_TYPE));
        if (user_type.equals("1")) {
           // relativeLayout.setVisibility(View.GONE);
            tv_refrence_number.setVisibility(View.VISIBLE);
//            card_reference.setVisibility(View.VISIBLE);
        } else {
           // relativeLayout.setVisibility(View.VISIBLE);
            tv_refrence_number.setVisibility(View.VISIBLE);
      //      card_reference.setVisibility(View.GONE);
        }

        ll_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProfileActivity.super.requestAppPermissions(ProfileActivity.this))
                    camera.selectImage(img_profile, 0);
            }
        });
        tv_edit_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                alertDialogBuilder.setMessage("Are you sure, You want to Logout");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent logout = new Intent(ProfileActivity.this, LoginActivity.class);
                                Shared_Preferences.clearPref1(getApplicationContext(), Constants.REG_ID);
                                startActivity(logout);
                                finish();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private void uploadData() {
        if (profile_image_path == null) {
            Toast.makeText(ProfileActivity.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(true);
            progressDialog.show();
            uploadFile(profile_image_path, profile_image_name);
        }
    }

    private void uploadFile(String profile_image_path, String profile_image_name) {

        FileUploadService service = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
        MultipartBody.Part body = null;

        Log.d("ImageTag", "uploadFile: "+profile_image_path);
        Log.d("ImageTag", "uploadFile: "+profile_image_name);

        if (!profile_image_path.equalsIgnoreCase("")) {

            File file = new File(profile_image_path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("profile_image", profile_image_name, requestFile);
        }

        RequestBody reg_id = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_ID));
        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);
        call = service.upload(reg_id,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.v("Upload", "success");
                String output = "";
                try {
                    output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    Log.d("Result_New", output);

                    String reason = jsonObject.getString("reason");
                    Log.d("message", reason);
                    if (res) {
                        // progressDialog.dismiss();
                        Shared_Preferences.setPrefs(ProfileActivity.this, Constants.REG_PROFILE_IMAGE, jsonObject.getString("profile_image"));
                        String imageName = Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_PROFILE_IMAGE);
                        if (imageName != null && !imageName.isEmpty() && !imageName.equals("null")) {
                            Log.d("image", "response: ");

                            Glide.with(ProfileActivity.this)
                                    .load(MyConfig.JSON_ProfileImage + imageName)
                                    .into(imageView);
                        } else {
                            Glide.with(ProfileActivity.this)
                                    .load(R.drawable.no_image_available)
                                    .into(imageView);
                        }
                        Toast.makeText(ProfileActivity.this, reason, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ProfileActivity.this, "" + reason, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Upload_error:", "");
                progressDialog.dismiss();
            }
        });
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
                    showDialog("External storage", ProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
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
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void setDataToView() {
        if(!Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_LNAME).equals("null")) {
            txt_id.setText(Shared_Preferences.getPrefs(ProfileActivity.this, Constants.REG_NAME) + " " + Shared_Preferences.getPrefs(ProfileActivity.this, Constants.REG_LNAME));
        }else {
            txt_id.setText(Shared_Preferences.getPrefs(ProfileActivity.this, Constants.REG_NAME));
        }
       // txt_id.setText(Shared_Preferences.getPrefs(ProfileActivity.this, Constants.REG_NAME));
        txt_contact_number.setText(Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_MOBILE));
        txt_email_id.setText(Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_EMAIL));
        tv_refrence_number.setText(Shared_Preferences.getPrefs(ProfileActivity.this,Constants.SELF_REFERENCE_NUMBER));

        String imageName = Shared_Preferences.getPrefs(ProfileActivity.this,Constants.REG_PROFILE_IMAGE);
        if (imageName != null && !imageName.isEmpty() && !imageName.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(ProfileActivity.this)
                    .load(MyConfig.JSON_ProfileImage + imageName)
                    .into(imageView);
        } else {
            Glide.with(ProfileActivity.this)
                    .load(R.drawable.no_image_available)
                    .into(imageView);
        }

    }


    private void getView() {
        txt_id=findViewById(R.id.txt_id);
        tv_refrence_number=findViewById(R.id.tv_refrence_number);
        txt_contact_number=findViewById(R.id.txt_contact_number);

        txt_city1=findViewById(R.id.txt_city1);
        txt_state1=findViewById(R.id.txt_state1);

        img_profile=findViewById(R.id.imageView);

        txt_city=findViewById(R.id.txt_city);
        txt_state=findViewById(R.id.txt_state);
        txt_country=findViewById(R.id.txt_country);

        txt_zone=findViewById(R.id.txt_zone);
        txt_member=findViewById(R.id.txt_member);
        txt_spec=findViewById(R.id.txt_spec);
        txt_email_id=findViewById(R.id.txt_email_id);
        txt_address=findViewById(R.id.txt_address);
        txt_Address=findViewById(R.id.txt_Address);
        txt_address1=findViewById(R.id.txt_address1);
        tv_edit_update_password=findViewById(R.id.tv_edit_update_password);
        txt_ref=findViewById(R.id.txt_ref);
        tv_logout=findViewById(R.id.tv_logout);
        cardview=findViewById(R.id.cardview);
        //card_reference=findViewById(R.id.card_reference);
        //relativeLayout=(RelativeLayout)findViewById(R.id.container);
       // imageView= findViewById(R.id.imageView);
        imageView=(CircleImageView) findViewById(R.id.imageView);
        ll_upload_image=findViewById(R.id.ll_upload_image);
        setDataToView();

        //fab=findViewById(R.id.fab);
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

    @Override
    public void processFinish(String result, int img_no) {
        String[] parts = result.split("/");
        String imagename = parts[parts.length - 1];
        Log.d("Image_path", result + " " + img_no);
        profile_image_name = imagename;
        profile_image_path = result;
        uploadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);


    }

    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/profileUpdate")
        Call<ResponseBody> upload(
                @Part("user_id") @Nullable RequestBody userId,
                @Part @Nullable MultipartBody.Part file);
    }

}
