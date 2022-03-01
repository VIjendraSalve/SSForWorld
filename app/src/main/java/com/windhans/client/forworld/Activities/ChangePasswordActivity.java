package com.windhans.client.forworld.Activities;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.PasswordEditText;
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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_oldPassword;
    PasswordEditText edt_newPassword,edt_confirmPassword;
    Button btn_submit;
    String user_id,oldPassword,newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.changepassword));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getViews();
    }

    private void getViews() {
        edt_oldPassword=findViewById(R.id.edt_oldPassword);
        edt_newPassword=findViewById(R.id.edt_newPassword);
        edt_confirmPassword=findViewById(R.id.edt_confirmPassword);
        btn_submit=findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    user_id= Shared_Preferences.getPrefs(ChangePasswordActivity.this, Constants.REG_ID);
                    oldPassword=edt_oldPassword.getText().toString();
                    newPassword=edt_newPassword.getText().toString();

                    changePassword(user_id,oldPassword,newPassword);
                }


            }
        });
    }

    private boolean isValid() {
        boolean result=true;
        if(!MyValidator.isValidPassword(edt_oldPassword))
        {
            result=false;
        }
        if(!MyValidator.isValidPassword(edt_newPassword))
        {
            result=false;
        }
        if (!MyValidator.isMatchPassword(edt_newPassword,edt_confirmPassword))
        {
            result=false;
        }
        return result;
    }

    private void changePassword(String user_id, String oldPassword, String newPassword) {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.changePassword(
                oldPassword,
                newPassword,
                user_id
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("Response", "onResponse: "+output);
                    JSONObject jsonObject=new JSONObject(output);
                    boolean status=jsonObject.getBoolean("result");
                    String message=jsonObject.getString("reason");
                    if(status)
                    {
                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public interface GetOrderApi
    {

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD +"/changePassword")
        Call<ResponseBody> changePassword(
                @Field("old_password") String oldPassword,
                @Field("new_password") String newPassword,
                @Field("user_id") String user_id
        );
    }
}
