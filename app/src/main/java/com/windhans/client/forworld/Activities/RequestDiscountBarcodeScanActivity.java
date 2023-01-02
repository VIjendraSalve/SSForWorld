package com.windhans.client.forworld.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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

public class RequestDiscountBarcodeScanActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    Button btn_submit;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Intent intent;
    private String delegate_id = "", delegate_name, user_id, event_id, card_type = "", vendorName="", serial = "", vendorID = "";
    private boolean resultValue;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bacodescan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.scan_barcode));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //bundle = getIntent().getExtras();

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btn_submit);


        Log.d("data", "getEventAboutData: " + "user id " + user_id + "event id " + event_id + "delegate id " + delegate_id);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode_value = txtBarcodeValue.getText().toString();
                Toast.makeText(RequestDiscountBarcodeScanActivity.this, ""+barcode_value, Toast.LENGTH_SHORT).show();

                    String user_id = Shared_Preferences.getPrefs(RequestDiscountBarcodeScanActivity.this, Constants.REG_ID);
                    Toast.makeText(RequestDiscountBarcodeScanActivity.this, "" + barcode_value, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RequestDiscountBarcodeScanActivity.this, VendorInformationRequestDiscountActivity.class);
                    intent.putExtra("vendorName", vendorName);
                    intent.putExtra("vendorId", vendorID);
                    startActivity(intent);

                //submitData(barcode_value,user_id);
            }
        });

    }

    private void submitData(String barcode_value, String user_id) {

        Log.d("DATATEST", "submitData: " + Shared_Preferences.getPrefs(RequestDiscountBarcodeScanActivity.this, Constants.LEAD_USER_ID));
        Log.d("DATATEST", "submitData: " + barcode_value);

        GetOrderAPI getOrderAPI = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        Call<ResponseBody> responseBodyCall = getOrderAPI.submitData(
                Shared_Preferences.getPrefs(RequestDiscountBarcodeScanActivity.this, Constants.LEAD_USER_ID),
                barcode_value);
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

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestDiscountBarcodeScanActivity.this);
                        alertDialogBuilder.setTitle("Wallet Amount");
                        alertDialogBuilder.setMessage("Wallet Balance For This User : " + jsonObject.getString("walletAmount"));
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        onBackPressed();
                                        arg0.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Toast.makeText(RequestDiscountBarcodeScanActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RequestDiscountBarcodeScanActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(RequestDiscountBarcodeScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(RequestDiscountBarcodeScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {


                            if (barcodes.valueAt(0).email != null) {

                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                btnAction.setText("ADD CONTENT TO THE MAIL");

                            } else  {
                                Log.d("testValues", "run: "+intentData);

                                    intentData = barcodes.valueAt(0).displayValue;
                                    String data = intentData;
                                    if(data.contains("WHT")) {
                                        String[] separated1 = data.split("-");
                                        separated1[0] = separated1[0].trim();
                                        separated1[1] = separated1[1].trim();
                                        separated1[2] = separated1[2].trim();
                                        separated1[3] = separated1[3].trim();

                                        card_type = String.valueOf(separated1[0]);
                                        serial = String.valueOf(separated1[1]);
                                        vendorName = String.valueOf(separated1[2]);
                                        vendorID = String.valueOf(separated1[3]);
                                        Log.d("card_number", "run: " + vendorID);
                                        Log.d("my_tag", "run: " + intentData);

                                        txtBarcodeValue.setText(vendorName);
                                    }else {
                                        txtBarcodeValue.setText("Invalid QR Code");
                                    }
                                
                                //Shared_Preferences.setPrefs(RequestDiscountBarcodeScanActivity.this, Constants.BARCODE, card_number);


                            }

                        }
                    });

                }


            }
        });
    }

    private void getEventAboutData() {
        GetOrderAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderAPI.class);
        final Call<ResponseBody> result = api.getOrderData(user_id, event_id, delegate_id);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();

                    Log.d("my_tag", "onResponse9: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("status");

                    if (res) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestDiscountBarcodeScanActivity.this);
                        alertDialogBuilder.setMessage(" " + delegate_name + " is " + jsonObject.getString("message"));
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        arg0.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //Toast.makeText(ScannedBarcodeActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestDiscountBarcodeScanActivity.this);
                        alertDialogBuilder.setMessage(" " + jsonObject.getString("message"));
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        arg0.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //Toast.makeText(ScannedBarcodeActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
            }
        });
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }

    public boolean onOptionsItemSelected(MenuItem item) {

      /*  if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }*/
        return super.onOptionsItemSelected(item);
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

    private interface GetOrderAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "")
        public Call<ResponseBody> getOrderData(
                @Field("user_id") String user_id,
                @Field("event_id") String event_id,
                @Field("deligates_id") String deligates_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getCardData")
        Call<ResponseBody> submitData(
                @Field("user_id") String user_id,
                @Field("card_no") String event_id
        );
    }

}
