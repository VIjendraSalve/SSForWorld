package com.windhans.client.forworld.StartActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.windhans.client.forworld.Activities.Activity_Forgot_Password;
import com.windhans.client.forworld.Activities.OTPVerification;
import com.windhans.client.forworld.Activities.RegistrationActivity;
import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.GPSTracker;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.ConnectionCallbacks {

    // private EditText et_username;
    //  PasswordEditText et_password;
    TextInputEditText et_password, et_username;
    private MyTextView_Poppins_Regular new_user;
    private ProgressDialog progressDialog;
    private Button btn_login;
    private int send_otp_flag = 1;
    MyTextView_Poppins_Regular tv_forgotPass, tv_registration;
    int flag;
    LinearLayout no_internet_connection;
    Button btn_retry;
    private String notification_token = "ssfsfd";
    protected GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_LOCATION = 199;
    private GPSTracker gpsTracker;
    private boolean statusOfGPS;
    private Geocoder geocoder;

    //  TextView tv_forgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gpsTracker = new GPSTracker(this);
        init();
        Log.d("flag", "onCreate: " + flag);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("Notification_Token1", "onComplete: " + token);
                        Shared_Preferences.setPrefs(LoginActivity.this, Constants.NOTIFICATION_TOKEN, token);

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
       /* if(flag==0)
        {
            wel.setText("Welcome");
            Log.d("flag", "onCreate: "+flag);

        }
        if(flag==1) {
            wel.setText("Welcome"+","+"Back");
        }*/

    }

    private void init() {

        btn_login = (Button) findViewById(R.id.btn_sign_in);

        et_username = (TextInputEditText) findViewById(R.id.et_username);
        et_password = (TextInputEditText) findViewById(R.id.et_password);
        //    tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        tv_registration = findViewById(R.id.tv_registration);
        tv_forgotPass = findViewById(R.id.tv_forgotPass);

        tv_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Activity_Forgot_Password.class);
                startActivity(intent);
            }
        });
        setListener();
    }

    private boolean validateFields() {
        boolean result = true;
        if (!MyValidator.isValidMobile(et_username)) {
            result = false;
        }
        if (!MyValidator.isValidField(et_password)) {
            result = false;
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // startActivity(new Intent(this,MainActivity.class));
    }


    private void loginUser() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);
        progressDialog.show();

        Log.d("TokenVijendra", "loginUser: "+Shared_Preferences.getPrefs(LoginActivity.this, Constants.NOTIFICATION_TOKEN));

        LoginAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(LoginAPI.class);
        Call<ResponseBody> result = api.checkUser(
                et_username.getText().toString(),
                et_password.getText().toString(),
                Shared_Preferences.getPrefs(LoginActivity.this, Constants.NOTIFICATION_TOKEN));

        Log.d("Vijendra", "JSON_BASE_URL: "+MyConfig.JSON_BASE_URL);
        Log.d("Vijendra", "JSON_SUBBASE_URL: "+MyConfig.SSWORLD + "/appLogin");
        Log.d("Vijendra", "result: "+result.toString());

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Response", "onResponse: "+call.toString());

                try {
                    String output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    String reason = jsonObject.getString("reason");

                    if (res) {

                        if (!reason.equals("Not Verified")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("userDetails");
                            if (jsonObject1.getString("is_verified").equals("1")) {
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_ID, jsonObject1.getString("id"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_TYPE, jsonObject1.getString("type"));
                               // Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_PROFILE_IMAGE, jsonObject1.getString("profile_image"));
                                Log.d("login", "onResponse: " + jsonObject1.getString("id"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_NAME, jsonObject1.getString("first_name"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_LNAME, jsonObject1.getString("last_name"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_EMAIL, jsonObject1.getString("email"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REG_MOBILE, jsonObject1.getString("mobile"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.REFERENCE_NUMBER, jsonObject1.getString("reference_number"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.SELF_REFERENCE_NUMBER, jsonObject1.getString("self_reference_number"));
                                Shared_Preferences.setPrefs(LoginActivity.this, Constants.IS_VERIFIED, jsonObject1.getString("is_verified"));

                                if(jsonObject1.getString("is_prime").equals("1")){
                                    if(jsonObject1.getString("is_prime_verified").equals("1")){
                                        Shared_Preferences.setPrefs(LoginActivity.this,
                                                Constants.IS_PRIME, "1");
                                        Shared_Preferences.setPrefs(LoginActivity.this, Constants.PRIME_CARD_NUMBER, jsonObject1.getString("prime_user_card_number"));
                                        Shared_Preferences.setPrefs(LoginActivity.this, Constants.PRIME_START_DATE, jsonObject1.getString("prime_start_date"));
                                        Shared_Preferences.setPrefs(LoginActivity.this, Constants.PRIME_END_DATE, jsonObject1.getString("prime_end_date"));

                                    }else{
                                        Shared_Preferences.setPrefs(LoginActivity.this,
                                                Constants.IS_PRIME, "0");
                                    }
                                }else{
                                    Shared_Preferences.setPrefs(LoginActivity.this,
                                            Constants.IS_PRIME, "0");
                                }


                                if (jsonObject1.has("category_type")) {

                                    Shared_Preferences.setPrefs(LoginActivity.this, Constants.category_type, jsonObject1.getString("category_type"));
                                    Shared_Preferences.setPrefs(LoginActivity.this, Constants.subcategoryID, jsonObject1.getString("category_id"));
                                    Shared_Preferences.setPrefs(LoginActivity.this, Constants.parent_categoryID, jsonObject1.getString("parent_category"));
                                    Shared_Preferences.setPrefs(LoginActivity.this, Constants.VendorAddress, jsonObject1.getString("address"));
                                    Shared_Preferences.setPrefs(LoginActivity.this, Constants.business_banner, jsonObject1.getString("business_banner"));

                                }

                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {

                            Intent intent = new Intent(LoginActivity.this, OTPVerification.class);
                            intent.putExtra(Constants.MOBILE_NUMBER, et_username.getText().toString());
                            intent.putExtra(Constants.Flag, "1");
                            startActivity(intent);


                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid Mobile Number And Password", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d("Retrofit Error:",t.getMessage());
                //progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
        check_connection();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public interface LoginAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/appLogin")
        Call<ResponseBody> checkUser(
                @Field("mobile") String mobile,
                @Field("password") String otp_number,
                @Field("notification_token") String notification
        );


    }

    private void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    //startActivity(new Intent(this, MainActivity.class));
                    //flag=1;
                    Log.d("flag", "onClick: " + flag);

                    Dexter.withActivity(LoginActivity.this)
                            .withPermissions(
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {

                                        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                                                .addApi(LocationServices.API)
                                                .addConnectionCallbacks(LoginActivity.this)
                                                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                                                    @Override
                                                    public void onConnectionFailed(ConnectionResult connectionResult) {

                                                        //Timber.v("Location error " + connectionResult.getErrorCode());
                                                    }
                                                }).build();
                                        mGoogleApiClient.connect();

                                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                            check_connection();
                                        }else{
                                            enableLoc();
                                        }




                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        // show alert dialog navigating to Settings
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).
                            withErrorListener(new PermissionRequestErrorListener() {
                                @Override
                                public void onError(DexterError error) {
                                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .onSameThread()
                            .check();

                }
            }
        });

    }

    private void enableLoc() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        statusOfGPS = true;
                        geocoder = new Geocoder(LoginActivity.this, Locale.getDefault());
                        Log.d("longitude", "onCreate: "+gpsTracker.getLongitude());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                            String cityName = addresses.get(0).getLocality();
                            String stateName = addresses.get(0).getAddressLine(1);
                            String countryName = addresses.get(0).getAddressLine(2);
                            Shared_Preferences.setPrefs(LoginActivity.this, Constants.DISTRICT_NAME_Location, cityName);
                            Log.d("CityName", "onCreate: " + cityName);


                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(LoginActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    private void check_connection() {
        if (CheckNetwork.isInternetAvailable(LoginActivity.this)) {
            //no_internet_connection.setVisibility(View.GONE);
            loginUser();
        } else {
            Toast.makeText(LoginActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            //no_internet_connection.setVisibility(View.VISIBLE);
        }
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}
