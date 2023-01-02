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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.R;
import com.windhans.client.forworld.StartActivities.LoginActivity;
import com.windhans.client.forworld.customfonts.EditText_Poppins_Medium;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.PasswordEditText;

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

public class RegistrationActivity extends AppCompatActivity {

    EditText_Poppins_Medium edt_first_name,edt_last_name,edt_mobile_No,edt_email_id,edt_reference_no;
    EditText edt_password;
    Button btn_Regiter;
    String f_name,l_name,mobile,email,password,reference_no;
    ImageView imageView;
    TextView btn_Already_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.register));

      /*  imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllViews();

    }

    private void getAllViews() {
        edt_first_name=findViewById(R.id.edt_first_name);
        edt_last_name=findViewById(R.id.edt_last_name);
        edt_mobile_No=findViewById(R.id.edt_mobile_No);
        edt_email_id=findViewById(R.id.edt_email_id);
        edt_password=findViewById(R.id.edt_password);
        edt_reference_no=findViewById(R.id.edt_reference_no);
        btn_Regiter=findViewById(R.id.btn_Regiter);

        btn_Regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    f_name=edt_first_name.getText().toString();
                    l_name=edt_last_name.getText().toString();
                    mobile=edt_mobile_No.getText().toString();
                    email=edt_email_id.getText().toString();
                    password=edt_password.getText().toString();
                    reference_no=edt_reference_no.getText().toString();
                    userRegistration(f_name,l_name,mobile,email,password,reference_no);
                }

            }
        });

        btn_Already_User=findViewById(R.id.btn_Already_User);
        btn_Already_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValid() {
        boolean res = true;
        if (!MyValidator.isValidField(edt_first_name)) {
            res = false;
        }

        if (!MyValidator.isValidField(edt_last_name)) {
            res = false;
        }

        if (!MyValidator.isValidMobile(edt_mobile_No)) {
            res = false;
        }
        if (!(MyValidator.isValidEmail(edt_email_id))) {
            res = false;
        }
        if (!(MyValidator.isValidPassword(edt_password))) {
            res = false;
        }
        return res;
    }

    private void userRegistration(String f_name, String l_name, String mobile,
                                  String email, String password, String reference_no) {


        GetOrderAPI getOrderAPI = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        Call<ResponseBody> responseBodyCall=getOrderAPI.registerNewUser(f_name,
                l_name,
                mobile,
                email,
                password,
                reference_no
                );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output=response.body().string();
                    Log.d("Vijendra123", "onResponse: "+output);
                    JSONObject jsonObject=new JSONObject(output);
                    boolean res=jsonObject.getBoolean("result");
                    String message="";
                    message=jsonObject.getString("reason");
                    if (res)
                    {
                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegistrationActivity.this, OTPVerification.class);
                        intent.putExtra(Constants.MOBILE_NUMBER,edt_mobile_No.getText().toString());
                        intent.putExtra(Constants.Flag, "1"); //flag 1 == registration
                        startActivity(intent);


                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();

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
    public interface GetOrderAPI {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD +"/appRegister")
        Call<ResponseBody> registerNewUser(
                @Field("first_name") String fname,
                @Field("last_name") String lname,
                @Field("mobile_number") String mobile_number,
                @Field("email") String email,
                @Field("password") String password,
                @Field("reference_number") String reference_number

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
