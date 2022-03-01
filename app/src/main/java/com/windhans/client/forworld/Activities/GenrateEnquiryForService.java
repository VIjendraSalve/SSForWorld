package com.windhans.client.forworld.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class GenrateEnquiryForService extends AppCompatActivity {

    TextInputEditText edt_Name, edt_mobile, edt_Address, edt_city;
    EditText edt_purpose;
    Button btn_save;
    String name, mobile, address, city, purpose;
    private ImageView imageView;
    private String serviceID = "", business_id = "";
    TextView textCartItemCount;
    private Integer mCartItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genrate_enquiry1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.generateenquiry));

        serviceID = getIntent().getStringExtra(Constants.ServiceIDForEnquiry);
        business_id = getIntent().getStringExtra(Constants.BusinessIDForEnquiry);

       
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllViews();
        getMyCartData();
    }

    private void getAllViews() {
        edt_Name = findViewById(R.id.edt_Name);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_Address = findViewById(R.id.edt_Address);
        edt_city = findViewById(R.id.edt_city);
        edt_purpose = findViewById(R.id.edt_purpose);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    name = edt_Name.getText().toString();
                    mobile = edt_mobile.getText().toString();
                    address = edt_Address.getText().toString();
                    city = edt_city.getText().toString();
                    purpose = edt_purpose.getText().toString();
                    genreateEnquiry(name, mobile, address, city, purpose);
                }

            }
        });
    }

    private boolean isValid() {
        boolean res = true;
        if (!MyValidator.isValidField(edt_Name)) {
            res = false;
        }
        if (!MyValidator.isValidMobile(edt_mobile)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_Address)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_city)) {
            res = false;
        }

        return res;
    }

    private void genreateEnquiry(String name, String mobile, String address, String city, String purpose) {

        Log.d("ids", "genreateEnquiry: " + serviceID + "," + business_id);
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.addEnquiry(
                Shared_Preferences.getPrefs(this, Constants.REG_ID),
                business_id,
                serviceID,
                mobile,
                purpose,
                address,
                city,
                name

        );
        Log.d("id", "genreateEnquiry: " + Shared_Preferences.getPrefs(this, Constants.REG_ID));
        Log.d("id", "genreateEnquiry: " + Shared_Preferences.getPrefs(this, Constants.BUSINESS_ID));

        Log.d("id", "genreateEnquiry: " + Shared_Preferences.getPrefs(this, Constants.PRODUCT_ID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("Response", "onResponse: "+output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    String msg = jsonObject.getString("reason");
                    if (res) {
                        Toast.makeText(GenrateEnquiryForService.this, msg, Toast.LENGTH_SHORT).show();
                        clearAllValues();
                        onBackPressed();
                    } else {
                        Toast.makeText(GenrateEnquiryForService.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void clearAllValues() {
        edt_Name.setText("");
        edt_Address.setText("");
        edt_city.setText("");
        edt_mobile.setText("");
        edt_purpose.setText("");

    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/generateServiceEnquiry")
        Call<ResponseBody> addEnquiry(
                @Field("user_id") String user_id,
                @Field("business_id") String business_id,
                @Field("service_id") String service_id,
                @Field("contact") String contact,
                @Field("purpose") String purpose,
                @Field("address") String address,
                @Field("city") String city,
                @Field("name") String name
        );
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

        if(Shared_Preferences.getPrefs(GenrateEnquiryForService.this, Constants.REG_TYPE).equals("2")){
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
                Intent intent = new Intent(GenrateEnquiryForService.this, Activity_My_Cart.class);
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
        String user_id= Shared_Preferences.getPrefs(GenrateEnquiryForService.this, Constants.REG_ID);
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
