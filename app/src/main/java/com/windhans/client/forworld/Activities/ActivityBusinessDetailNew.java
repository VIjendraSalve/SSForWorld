package com.windhans.client.forworld.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.windhans.client.forworld.Model.BuisnessModel;
import com.windhans.client.forworld.Model.VendorByProduct;
import com.windhans.client.forworld.R;
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

public class ActivityBusinessDetailNew extends AppCompatActivity {

    ImageView image_business_banner;
    TextView txt_business_name, txt_business_details1, txt_contact, txt_email, txt_address, txt_city, txt_district, txt_state, txt_pincode;
    List<BuisnessModel> buisnessModelList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LinearLayout ll_call_number, ll_email;
    private ArrayList<BuisnessModel> vendorByProductArrayList = new ArrayList<>();
    private Integer position;
    private Button btn_view_all_product;
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vendorByProductArrayList = this.getIntent().getParcelableArrayListExtra(Constants.VendorDetailList);
        position = this.getIntent().getIntExtra(Constants.PositionOfVendor,0);

        Log.d("Vendor", "size: "+vendorByProductArrayList.size());
        Log.d("Vendor", "position: "+position);
        Log.d("Vendor", "vendorID: "+vendorByProductArrayList.get(position).getUser_id());

        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Business Details");
        progressDialog = new ProgressDialog(ActivityBusinessDetailNew.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);


        getView();
        getMyCartData();
    }

    private void getView() {
        image_business_banner = findViewById(R.id.image_business_banner);
        txt_business_name = findViewById(R.id.txt_business_name);
        txt_business_details1 = findViewById(R.id.txt_business_details1);
        txt_contact = findViewById(R.id.txt_contact);
        txt_email = findViewById(R.id.txt_email);
        txt_address = findViewById(R.id.txt_address);
        txt_city = findViewById(R.id.txt_city);
        txt_district = findViewById(R.id.txt_district);
        txt_state = findViewById(R.id.txt_state);
        ll_call_number = findViewById(R.id.ll_call_number);
        ll_email = findViewById(R.id.ll_email);
        btn_view_all_product = findViewById(R.id.btn_view_all_product);



        //txt_pincode = findViewById(R.id.txt_pincode);


        Log.d("VijSal", " "+Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_NAME));
        Log.d("VijSal", " "+Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_CONTACT));
        Log.d("VijSal", " "+Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_CITY));
        Log.d("VijSal", " "+Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_EMAIL));

        String image1 = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_IMAGE);
        String image = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUISNESS_BANNER);
        String business_name = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_NAME);
        String details = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_DEC);
        String address = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_ADDRESS);
        String contact_no = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_CONTACT);
        String city = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_CITY);
        String district = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_DISTRICT);
        String state = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_STATE);
        String pincode = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_PINCODE);
        String email = Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_EMAIL);
        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(ActivityBusinessDetailNew.this)
                    .load(MyConfig.JSON_BusinessPath + image)

                    .into(image_business_banner);
        } else {
            Glide.with(ActivityBusinessDetailNew.this)
                    .load(R.drawable.no_image_available)

                    .into(image_business_banner);
        }



        /*txt_business_name.setText(business_name);
        txt_business_details1.setText(details);
        txt_contact.setText(contact_no);
        txt_contact.setPaintFlags(txt_contact.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
        txt_email.setText(email);
        txt_email.setPaintFlags(txt_email.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
        txt_address.setText(address + " \n "+ city + "  ( " + pincode+" )");
        //txt_city.setText(city + "  ( " + pincode+" )");
        txt_district.setText(district);
        txt_state.setText(state);*/

        txt_business_name.setText(vendorByProductArrayList.get(position).getBusiness_name());
        txt_business_details1.setText(vendorByProductArrayList.get(position).getDescription());
        txt_contact.setText(vendorByProductArrayList.get(position).getContact_mobile());
        txt_contact.setPaintFlags(txt_contact.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
        txt_email.setText(vendorByProductArrayList.get(position).getEmail());
        txt_email.setPaintFlags(txt_email.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
        txt_address.setText(vendorByProductArrayList.get(position).getAddress() + " \n"+ vendorByProductArrayList.get(position).getCity() + "  (" + vendorByProductArrayList.get(position).getPincode()+")");

        btn_view_all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityBusinessDetailNew.this, ActivityVendorProductList.class);
                intent.putExtra(Constants.VendorID, vendorByProductArrayList.get(position).getUser_id());
                Shared_Preferences.setPrefs(ActivityBusinessDetailNew.this, Constants.BUSINESS_IDFORCART, vendorByProductArrayList.get(position).getUser_id());
                startActivity(intent);
            }
        });

        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",vendorByProductArrayList.get(position).getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry");
                emailIntent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        ll_call_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(ActivityBusinessDetailNew.this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + vendorByProductArrayList.get(position).getContact_mobile()));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    Activity#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for Activity#requestPermissions for more details.
                                        return;
                                    }
                                }
                                startActivity(callIntent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {
                                    // navigate user to app settings
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

//        txt_pincode.setText(pincode);

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBusinessDetailNew.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        if(Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.REG_TYPE).equals("2")){
            menuItem.setVisible(false);
        }else {
            menuItem.setVisible(true);
        }

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                Intent intent = new Intent(ActivityBusinessDetailNew.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void getMyCartData() {
        Activity_My_Cart.GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(Activity_My_Cart.GetOrderApi.class);
        String user_id= Shared_Preferences.getPrefs(ActivityBusinessDetailNew.this, Constants.REG_ID);
        Log.d("user_id", "getMyCartData: "+user_id);
        Call<ResponseBody> responseBodyCall = getOrderApi.getMyCart(user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("ResponseCart", "onResponse: "+output);
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if(result)
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("cart_data");
                            JSONArray array=jsonObject1.getJSONArray("product");
                            Log.d("Badge", "onResponse: "+array.length());
                            mCartItemCount = array.length();
                            setupBadge();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
