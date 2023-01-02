package com.windhans.client.forworld.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
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

public class VendorInformationRequestDiscountActivity extends AppCompatActivity {

    private EditText edt_amount, edt_product;
    private Button btn_request_discount;
    private TextView tv_business_name;
    private String vendorId = "", vendorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_information_request_discount);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.vendorinformation));

        vendorId = getIntent().getStringExtra("vendorId");
        vendorName = getIntent().getStringExtra("vendorName");

        initViews();

    }

    private void initViews() {

        edt_amount = findViewById(R.id.edt_amount);
        edt_product = findViewById(R.id.edt_product);
        btn_request_discount = findViewById(R.id.btn_request_discount);
        tv_business_name = findViewById(R.id.tv_business_name);

        tv_business_name.setText(""+vendorName);

        btn_request_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_amount.getText().toString().isEmpty()){
                    requestDiscount();
                }
            }
        });

    }

    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/send_qr_request")
        public Call<ResponseBody> sendQrRequest(
                @Field("user_id") String user_id,
                @Field("vendor_id") String vendor_id,
                @Field("total_amount") String total_amount,
                @Field("product_title") String product_title
        );
    }


    private void requestDiscount() {


        GetOrderAPI getOrderAPI = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        Call<ResponseBody> responseBodyCall = getOrderAPI.sendQrRequest(
                Shared_Preferences.getPrefs(VendorInformationRequestDiscountActivity.this, Constants.REG_ID),
                vendorId,
                edt_amount.getText().toString().trim(),
                edt_product.getText().toString().trim()
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    boolean result = jsonObject.getBoolean("result");
                    String message = jsonObject.getString("reason");
                    if (result) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VendorInformationRequestDiscountActivity.this);
                        alertDialogBuilder.setTitle("Thank you");
                        alertDialogBuilder.setMessage("Vendor will approved your discount request");
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                        Intent intent = new Intent(VendorInformationRequestDiscountActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Toast.makeText(VendorInformationRequestDiscountActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VendorInformationRequestDiscountActivity.this, message, Toast.LENGTH_SHORT).show();
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
}