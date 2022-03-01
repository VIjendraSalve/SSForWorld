package com.windhans.client.forworld.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.EditText_Poppins_Medium;
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

public class Activity_Forgot_Password extends AppCompatActivity {

    EditText_Poppins_Medium et_username;
    private Button btn_forgotpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__forgot__password);

        et_username=findViewById(R.id.et_username);
        btn_forgotpassword=findViewById(R.id.btn_forgotpassword);

        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordApi();
            }
        });

    }

    private void forgotPasswordApi() {
        Log.d("Vijedra1234", "forgotPasswordApi: "+ et_username.getText().toString());
        GetOrderAPi getOrderAPi=MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPi.class);
        Call<ResponseBody> responseBodyCall=getOrderAPi.forgotPassword(
                et_username.getText().toString()
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
                        Toast.makeText(Activity_Forgot_Password.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Forgot_Password.this, OTPVerification.class);
                        intent.putExtra(Constants.MOBILE_NUMBER, et_username.getText().toString());
                        intent.putExtra(Constants.Flag, "0"); //flag 0 == forgot password
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Activity_Forgot_Password.this, ""+message, Toast.LENGTH_SHORT).show();
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
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/sendOtpForgetpassword")
        Call<ResponseBody> forgotPassword(
                @Field("mobile") String mobile);


    }
}
