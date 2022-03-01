package com.windhans.client.forworld.StartActivities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import static com.windhans.client.forworld.my_library.Constants.changeLang;
import static com.windhans.client.forworld.my_library.Constants.change_language;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private static int SPLASH_TIME_OUT = 3000;
    private TextView tv_app_name;
    private ImageView iv_app_logo;
    private  String link1="";
    private LinearLayout  noConnectionLayout;
    private Button btnRetry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        noConnectionLayout = findViewById(R.id.noConnectionLayout);
       /* tv_app_name = (TextView) findViewById(R.id.tv_app_name);
        iv_app_logo = (ImageView) findViewById(R.id.iv_app_logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        tv_app_name.startAnimation(animation);
        iv_app_logo.startAnimation(animation);*/

        String language = Shared_Preferences.getPrefs(this, "select");
        if (language == null) {
            //Log.d("lang-->","dsadaad");
        } else {
            //Log.d("lang-->",language);
            if (language.equals("0")) {
                changeLang("eng", this);
            }
            if (language.equals("2")) {
                changeLang("mr", this);
            }
            if (language.equals("1")) {
                changeLang("hi", this);
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
              checkConnection();


            }
        }, SPLASH_TIME_OUT);
    }

    private void checkConnection() {
        if(CheckNetwork.isInternetAvailable(SplashActivity.this))
        {
            noConnectionLayout.setVisibility(View.GONE);
            startNext();
        }
        else{

            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void startNext() {
        if(Shared_Preferences.getPrefs(SplashActivity.this, Constants.REG_ID)!=null)
        {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            //orderList.clear();
            checkConnection();
        }
    }
}
