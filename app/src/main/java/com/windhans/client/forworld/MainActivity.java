package com.windhans.client.forworld;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

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
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.windhans.client.forworld.Activities.ActivityAddServices;
import com.windhans.client.forworld.Activities.ActivityMyFavorite;
import com.windhans.client.forworld.Activities.ActivityMyProduct;
import com.windhans.client.forworld.Activities.ActivityMyService;
import com.windhans.client.forworld.Activities.ActivityOfferListing;
import com.windhans.client.forworld.Activities.ActivityOrderListing;
import com.windhans.client.forworld.Activities.Activity_Add_Offer;
import com.windhans.client.forworld.Activities.Activity_Advertising;
import com.windhans.client.forworld.Activities.Activity_Campaign;
import com.windhans.client.forworld.Activities.Activity_My_Balance;
import com.windhans.client.forworld.Activities.Activity_My_Cart;
import com.windhans.client.forworld.Activities.Activity_My_Rewards;
import com.windhans.client.forworld.Activities.Activity_Share;
import com.windhans.client.forworld.Activities.Activity_share_business;
import com.windhans.client.forworld.Activities.BillActivity;
import com.windhans.client.forworld.Activities.BusinessProfile;
import com.windhans.client.forworld.Activities.ChangePasswordActivity;
import com.windhans.client.forworld.Activities.EnquiryListActivity;
import com.windhans.client.forworld.Activities.ProfileActivity;

import com.windhans.client.forworld.Fragment.HomeFragment;
import com.windhans.client.forworld.Fragment.HomeFragment1;
import com.windhans.client.forworld.StartActivities.LoginActivity;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.GPSTracker;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public Menu menu;
    private TextView tv;
    private Handler handler = new Handler();
    ImageView imageView;
    TextView txt_name, textView;
    protected GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_LOCATION = 199;
    int flag = 1;
    CircleImageView img_profile;
    TextView textCartItemCount;
    private Integer mCartItemCount = 2;
    String user_id,version;
    TextView nav_version;
    private GPSTracker gpsTracker;
    private boolean statusOfGPS;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsTracker = new GPSTracker(MainActivity.this);
        String name = Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_NAME);
        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        user_id = Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_TYPE);

        if (user_id.equals("2")) {
            title.setText("ForWorld");
        } else {
            getMyCartData();
            title.setText("ForWorld" + "/" + "Hi," + " " + name);
        }







        Dexter.withActivity(MainActivity.this)
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
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();

                            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                    .addApi(LocationServices.API)
                                    .addConnectionCallbacks(MainActivity.this)
                                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                                        @Override
                                        public void onConnectionFailed(ConnectionResult connectionResult) {

                                            //Timber.v("Location error " + connectionResult.getErrorCode());
                                        }
                                    }).build();
                            mGoogleApiClient.connect();
                            enableLoc();



                            getSupportActionBar().setDisplayShowTitleEnabled(false);

                            navigationView = (NavigationView) findViewById(R.id.nav_view);
                            navigationView.setNavigationItemSelectedListener(MainActivity.this);
                            navigationView.setItemIconTintList(null);


                            navigationView = (NavigationView) findViewById(R.id.nav_view);
                            View headerView = navigationView.getHeaderView(0);


                            View header = navigationView.getHeaderView(0);
                            menu = navigationView.getMenu();


                            nav_version = (TextView) findViewById(R.id.nav_version);

                            String versionName = null;
                            try {
                                versionName = MainActivity.this.getPackageManager()
                                        .getPackageInfo(MainActivity.this.getPackageName(), 0).versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            version = versionName;
                            Log.d("abc", "onCreate: " + version);
                            nav_version.setText("version " + version);


                            String user_type = (Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_TYPE));
                            if (user_type.equals("1")) {
                                hideItem();
                                menu.setGroupVisible(R.id.member_grp, false);
                                menu.setGroupVisible(R.id.member_grp1, true);
                            } else {
                                menu.setGroupVisible(R.id.member_grp, true);
                                menu.setGroupVisible(R.id.member_grp1, false);
                                hideItem();
                            }


                            Runnable runnable = new Runnable() {
                                public void run() {

                                    tv = (TextView) findViewById(R.id.tv_marquee);
                                    tv.setSelected(true);
                                    tv.setText(Shared_Preferences.getPrefs(MainActivity.this, "text"));
                                    handler.sendEmptyMessage(0);
                                }
                            };

                            Thread mythread = new Thread(runnable);
                            mythread.start();


                            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            toggle = new ActionBarDrawerToggle(
                                    MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                            drawer.addDrawerListener(toggle);
                            toggle.syncState();

                            String reg_id = Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_TYPE);
                            Log.d("reg", "onCreate: "+reg_id);
                            if (reg_id.equals("2")) {//user-1,vendor-2
                                HomeFragment1 myEventHomeFragment = new HomeFragment1();
                                // Because fragment two has been popup from the back stack, so it must be null.
                                if (myEventHomeFragment == null) {
                                    myEventHomeFragment = new HomeFragment1();
                                }


                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                // Create FragmentArchived instance.
                                //MyEventHomeFragment myEventHomeFragment = new MyEventHomeFragment();
                                getSupportActionBar().setTitle("Home");
                                // Add fragment one with tag name.
                                fragmentTransaction.add(R.id.frameLayout, myEventHomeFragment, "F");
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
                            } else {
                                HomeFragment myEventHomeFragment = new HomeFragment();
                                // Because fragment two has been popup from the back stack, so it must be null.
                                if (myEventHomeFragment == null) {
                                    myEventHomeFragment = new HomeFragment();
                                }


                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                // Create FragmentArchived instance.
                                //MyEventHomeFragment myEventHomeFragment = new MyEventHomeFragment();
                                getSupportActionBar().setTitle("Home");
                                // Add fragment one with tag name.
                                fragmentTransaction.add(R.id.frameLayout, myEventHomeFragment, "F");
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
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



    /*    ImageView iv_notification = (ImageView) toolbar.findViewById(R.id.search);
        iv_notification.setVisibility(View.VISIBLE);
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
*/



    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
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

                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        Log.d("longitude", "onCreate: "+gpsTracker.getLongitude());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(addresses != null) {
                            if (addresses.size() > 0) {
                                String cityName = addresses.get(0).getLocality();
                                String stateName = addresses.get(0).getAddressLine(1);
                                String countryName = addresses.get(0).getAddressLine(2);
                                Shared_Preferences.setPrefs(MainActivity.this, Constants.DISTRICT_NAME_Location, cityName);
                                Log.d("CityName", "onCreate: " + cityName);

                            }
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    private void hideViws() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (flag == 0) {
            nav_Menu.setGroupVisible(R.id.member_grp, true);
            flag = 1;

        } else if (flag == 1) {
            nav_Menu.setGroupVisible(R.id.member_grp, false);
            flag = 0;
        }

    }

    private void hideItem() {
      /*  navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();*/
        menu.findItem(R.id.nav_login).setVisible(false);

        // 1 product 2 Service
        if(Shared_Preferences.getPrefs(MainActivity.this, Constants.category_type) != null) {
            if (Shared_Preferences.getPrefs(MainActivity.this, Constants.category_type).equals("1")) {

                menu.findItem(R.id.nav_myProduct).setVisible(true);
                menu.findItem(R.id.nav_myService).setVisible(false);



            } else {
                menu.findItem(R.id.nav_myProduct).setVisible(false);
                menu.findItem(R.id.nav_myService).setVisible(true);

                menu.findItem(R.id.nav_offer_creation).setVisible(false);
                menu.findItem(R.id.nav_offer_listing).setVisible(false);
            }
        }


    }

    private void hideItem1() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_logout).setVisible(false);


    }

    private void showItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_login).setVisible(true);
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.search_menu, menu);
        // MenuItem myActionMenuItem = menu.findItem(R.id.search);
         return super.onCreateOptionsMenu(menu);
     }
 */
    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        callFragment(id);
        return true;
    }

    private void callFragment(int id) {

        Fragment fragment_nav = null;



       // MenuItem nav_version = menu.findItem(R.id.nav_buildVersion);
        if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_language) {

            View v = null;
            change_language(v);
        }
        else if (id == R.id.nav_feedback) {
            Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
        /*    Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_card_share) {
            Intent intent = new Intent(MainActivity.this, Activity_share_business.class);
            startActivity(intent);
        } else if (id == R.id.nav_offer_creation) {
            Intent intent = new Intent(MainActivity.this, Activity_Add_Offer.class);
            startActivity(intent);

        } else if (id == R.id.nav_offer_listing) {
            Intent intent = new Intent(MainActivity.this, ActivityOfferListing.class);
            startActivity(intent);
        } else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myProduct) {
            Intent intent = new Intent(MainActivity.this, ActivityMyProduct.class);
            startActivity(intent);
        }else if (id == R.id.nav_myService) {
            Intent intent = new Intent(MainActivity.this, ActivityMyService.class);
            startActivity(intent);
        } else if (id == R.id.nav_myOrders) {
            Intent intent = new Intent(MainActivity.this, ActivityOrderListing.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_campaign) {
            Intent intent = new Intent(MainActivity.this, Activity_Campaign.class);
            startActivity(intent);

        }*/ /*else if (id == R.id.nav_MyBalance) {
            Intent intent = new Intent(MainActivity.this, Activity_My_Balance.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_reward) {
            Intent intent = new Intent(MainActivity.this, Activity_My_Rewards.class);
            startActivity(intent);
        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(MainActivity.this, ActivityMyFavorite.class);
            startActivity(intent);
        } else if (id == R.id.nav_enquiry) {
            Intent intent = new Intent(MainActivity.this, EnquiryListActivity.class);
            startActivity(intent);
        }

        else if(id==R.id.nav_referrals)
        {
            Intent intent=new Intent(MainActivity.this, Activity_My_Balance.class);
            startActivity(intent);
        }
       /* else if(id==R.id.nav_customer_enquiry)
        {
            Intent intent=new Intent(MainActivity.this, EnquiryListActivity.class);
            startActivity(intent);
        }*/
       /* else if (id == R.id.nav_Add) {
            Intent intent = new Intent(MainActivity.this, Activity_Advertising.class);
            startActivity(intent);
        }*/ /*else if (id == R.id.Setting) {
        //    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }*/
     /*   else if(id==R.id.nav_demo)
        {
            Intent intent = new Intent(MainActivity.this, Demo.class);
            startActivity(intent);
        }*/
        else if (id == R.id.nav_business_profile) {
            Intent intent = new Intent(MainActivity.this, BusinessProfile.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(MainActivity.this, Activity_My_Cart.class);
            startActivity(intent);
        }
     /*   else if(id==R.id.nav_details)
        {
            Intent intent=new Intent(MainActivity.this, ActivityDetails.class);
            startActivity(intent);
        }*/
        else if (id == R.id.nav_Share) {
            Intent intent = new Intent(MainActivity.this, Activity_Share.class);
            startActivity(intent);
        }
       /* else if (id == R.id.nav_bill) {
            Intent intent = new Intent(MainActivity.this, BillActivity.class);
            startActivity(intent);
        }*/
     /*   else if(id==R.id.nav_Add)
        {
            Intent intent=new Intent(MainActivity.this, ActivityAdd.class);
            startActivity(intent);
        }*/
       /* else if (id == R.id.nav_buildVersion) {
            String versionName = BuildConfig.VERSION_NAME;

            int versionCode = BuildConfig.VERSION_CODE;
            Log.d("version", "callFragment: " + versionName);
            nav_version.setTitle("Version:" + versionName);

        }*/ else if (id == R.id.nav_logout) {



            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(this.getResources().getString(R.string.logout_message));
            alertDialogBuilder.setPositiveButton(this.getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                            Shared_Preferences.clearPref(getApplicationContext());
                            startActivity(logout);
                            finish();

                        }
                    });

            alertDialogBuilder.setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void change_language(View view) {
        Constants.change_language(this);
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                System.exit(0);
            }
            return;
        }

        if (Shared_Preferences.getPrefs(this, "Event_id") == null) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.please_press_back_again_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) myActionMenuItem.getIcon();
        setBadgeCount(this, icon, "9");
        myActionMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_cart) {
                    Intent intent = new Intent(MainActivity.this, Activity_My_Cart.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        if(Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_TYPE).equals("2")){
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
                Intent intent = new Intent(MainActivity.this, Activity_My_Cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.search_home: {
               /* Intent intent = new Intent(MainActivity.this, Activity_My_Cart.class);
                startActivity(intent);*/
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
        String user_id= Shared_Preferences.getPrefs(MainActivity.this, Constants.REG_ID);
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

    private void setBadgeCount(MainActivity mainActivity, LayerDrawable icon, String s) {
        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(MainActivity.this);
        }

        badge.setCount("1");
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!user_id.equals("2")) {
            getMyCartData();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
