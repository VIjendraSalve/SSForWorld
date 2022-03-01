package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.StartActivities.LoginActivity;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;

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

public class OTPVerification extends AppCompatActivity implements View.OnClickListener {
    private Button bt_submit_otp;
    private TextView tv_mobile_number;
    private EditText et_otp, edt_newPassword;
    private TextView tv_otp_resend, tv_resend_otp_done;
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler();
    private String mobile_number, flag="";
    private TextInputLayout txt_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        mobile_number = intent.getExtras().getString(Constants.MOBILE_NUMBER);
        flag = intent.getExtras().getString(Constants.Flag);
        //flag =1 registration 0 = forgot password
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.verrifying_otp_message_dialog));

        et_otp = (EditText) findViewById(R.id.et_otp);
        edt_newPassword = (EditText) findViewById(R.id.edt_newPassword);
        txt_input = (TextInputLayout) findViewById(R.id.txt_input);
        tv_otp_resend = (TextView) findViewById(R.id.tv_resend_otp);
        tv_resend_otp_done = (TextView) findViewById(R.id.tv_resend_otp_done);
        tv_mobile_number = (TextView) findViewById(R.id.tv_mobile_number);
        tv_mobile_number.setText(mobile_number);
        bt_submit_otp = (Button) findViewById(R.id.bt_submit_otp);
        bt_submit_otp.setOnClickListener(this);
        tv_otp_resend.setOnClickListener(this);

        if(flag.equals("0")){
            edt_newPassword.setVisibility(View.VISIBLE);
            txt_input.setVisibility(View.VISIBLE);
        }else {
            edt_newPassword.setVisibility(View.GONE);
            txt_input.setVisibility(View.GONE);
        }

        setCountDown();
    }

    private void setCountDown() {
        tv_otp_resend.setEnabled(false);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                if (seconds < 10) {
                    tv_otp_resend.setText(minutes + ":0" + seconds);
                } else {
                    tv_otp_resend.setText(minutes + ":" + seconds);
                }
            }

            public void onFinish() {
                tv_otp_resend.setText("Resend OTP");
                tv_otp_resend.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_resend_otp) {
             if(flag.equals("0")){
                 resendOTP();
             }else {
                 resendOTPRegistration();
             }
        }
        if (v.getId() == R.id.bt_submit_otp) {
            if(flag.equals("1")) {
                checkOTP(); // flag 1 == registration
            }else {
                setNewPassword();
            }
        }
    }

    private void checkOTP() {
        progressDialog.show();

        CheckOTPAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CheckOTPAPI.class);
        Log.d("otp", "checkOTP: " + et_otp.getText().toString());
        Log.d("mobile_no", "mobile: " + mobile_number);
        String notification="dfsdfsd";
        Call<ResponseBody> result = api.checkOTP(
                mobile_number,
                et_otp.getText().toString(),
                notification
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();

                    output=response.body().string();
                    Log.d("log123", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);

                    Boolean res = jsonObject.getBoolean("result");
                    String reason=jsonObject.getString("reason");
                    if (res) {
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerification.this, reason, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OTPVerification.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerification.this, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setNewPassword() {
        progressDialog.show();

        CheckOTPAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CheckOTPAPI.class);
        Log.d("otp", "checkOTP: " + et_otp.getText().toString());
        Log.d("mobile_no", "mobile: " + mobile_number);
        String notification="dfsdfsd";
        Call<ResponseBody> result = api.OTPVerificationForgetpassword(
                mobile_number,
                et_otp.getText().toString(),
                edt_newPassword.getText().toString(),
                notification
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();

                    output=response.body().string();
                    Log.d("log123", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);

                    Boolean res = jsonObject.getBoolean("result");
                    String reason=jsonObject.getString("reason");
                    if (res) {
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerification.this, reason, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OTPVerification.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerification.this, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private interface CheckOTPAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/userOTPVerification")
        public Call<ResponseBody> checkOTP(
                @Field("reg_mobile") String MobileNo,
                @Field("otp_number") String OTP,
                @Field("reg_notification_token") String reg_notification_token);

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/OTPVerificationForgetpassword")
        public Call<ResponseBody> OTPVerificationForgetpassword(
                @Field("mobile") String MobileNo,
                @Field("otp_number") String otp_number,
                @Field("new_password") String new_password,
                @Field("reg_notification_token") String reg_notification_token
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/sendOtpForgetpassword")
        Call<ResponseBody> resendOTPForget(
                @Field("mobile") String mobile);

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/resendOtp")
        Call<ResponseBody> resendOtp(
                @Field("mobile") String mobile);

    }

    private void resendOTP() {
        Log.d("MobileToResend", "resendOTP: "+mobile_number);
        CheckOTPAPI getOrderAPi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CheckOTPAPI.class);
        Call<ResponseBody> responseBodyCall=getOrderAPi.resendOTPForget(
                mobile_number
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("response", "onResponse: "+output);
                    JSONObject object=new JSONObject(output);
                    boolean res=object.getBoolean("status");
                    String message=object.getString("message");
                    if(res)
                    {
                        Toast.makeText(OTPVerification.this, ""+message, Toast.LENGTH_SHORT).show();
                        setCountDown();
                    }
                    else {
                        Toast.makeText(OTPVerification.this, ""+message, Toast.LENGTH_SHORT).show();
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

    private void resendOTPRegistration() {
        Log.d("MobileToResend", "resendOTP: "+mobile_number);
        CheckOTPAPI getOrderAPi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CheckOTPAPI.class);
        Call<ResponseBody> responseBodyCall=getOrderAPi.resendOtp(
                mobile_number
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("response333", "onResponse: "+output);
                    JSONObject object=new JSONObject(output);
                    boolean res=object.getBoolean("status");
                    String message=object.getString("message");
                    if(res)
                    {
                        Toast.makeText(OTPVerification.this, ""+message, Toast.LENGTH_SHORT).show();
                        setCountDown();
                    }
                    else {
                        Toast.makeText(OTPVerification.this, ""+message, Toast.LENGTH_SHORT).show();
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

    public interface GetOrderAPi
    {



    }
}
