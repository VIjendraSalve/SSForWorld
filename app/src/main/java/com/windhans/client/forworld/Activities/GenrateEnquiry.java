package com.windhans.client.forworld.Activities;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.EditText_Poppins_Medium;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;

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

public class GenrateEnquiry extends AppCompatActivity {

    EditText_Poppins_Medium edt_Name,edt_mobile,edt_Address,edt_city,edt_purpose;

    Button btn_save;
    String name,mobile,address,city,purpose;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genrate_enquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        //title.setText("Generate Enquiry");
        title.setText(this.getResources().getString(R.string.generateenquiry));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllViews();
    }

    private void getAllViews() {
        edt_Name=findViewById(R.id.edt_Name);
        edt_mobile=findViewById(R.id.edt_mobile);
        edt_Address=findViewById(R.id.edt_Address);
        edt_city=findViewById(R.id.edt_city);
        edt_purpose=findViewById(R.id.edt_purpose);
        btn_save=findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    name=edt_Name.getText().toString();
                    mobile=edt_mobile.getText().toString();
                    address=edt_Address.getText().toString();
                    city=edt_city.getText().toString();
                    purpose=edt_purpose.getText().toString();
                    genreateEnquiry(name,mobile,address,city,purpose);
                }

            }
        });
    }

    private boolean isValid() {
        boolean res=true;
        if(!MyValidator.isValidField(edt_Name))
        {
           res=false;
        }
        if(!MyValidator.isValidMobile(edt_mobile))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_Address))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_city))
        {
            res=false;
        }

        return res;
    }

    private void genreateEnquiry(String name, String mobile, String address, String city, String purpose) {

        String product_ids = Shared_Preferences.getPrefs(this,Constants.PRODUCT_ID_STRING);
        product_ids = product_ids.substring(0, (product_ids.length()-1));

        Log.d("id", "genreateEnquiry: "+Shared_Preferences.getPrefs(this, Constants.REG_ID));
        Log.d("id", "genreateEnquiry: "+Shared_Preferences.getPrefs(this,Constants.BUSINESS_ID1));
        Log.d("id", "genreateEnquiry: "+product_ids);
        Log.d("id", "genreateEnquiry: "+mobile);
        Log.d("id", "genreateEnquiry: "+purpose);
        Log.d("id", "genreateEnquiry: "+address);
        Log.d("id", "genreateEnquiry: "+city);
        Log.d("id", "genreateEnquiry: "+name);

        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.addEnquiry(
                Shared_Preferences.getPrefs(this, Constants.REG_ID),
                Shared_Preferences.getPrefs(this,Constants.BUSINESS_ID1),
                product_ids,
                mobile,
                purpose,
                address,
                city,
                name

        );
        Log.d("id", "genreateEnquiry: "+  Shared_Preferences.getPrefs(this, Constants.REG_ID));
        Log.d("id", "genreateEnquiry: "+  Shared_Preferences.getPrefs(this,Constants.BUSINESS_ID1));

        Log.d("id", "genreateEnquiry: "+ Shared_Preferences.getPrefs(this,Constants.PRODUCT_ID_STRING));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();

                    JSONObject jsonObject=new JSONObject(output);
                    boolean res=jsonObject.getBoolean("result");
                    String msg=jsonObject.getString("reason");
                    if(res)
                    {
                        Toast.makeText(GenrateEnquiry.this, msg, Toast.LENGTH_SHORT).show();
                        clearAllValues();
                        Intent intent = new Intent(GenrateEnquiry.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(GenrateEnquiry.this, msg, Toast.LENGTH_SHORT).show();
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

    public  interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/generateEnquiry")
        Call<ResponseBody> addEnquiry(
                @Field("user_id") String user_id,
                @Field("business_id") String business_id,
                @Field("product_id") String product_id,
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
