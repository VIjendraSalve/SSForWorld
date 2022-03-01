package com.windhans.client.forworld.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;
import com.windhans.client.forworld.my_library.UtilityRuntimePermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class Activity_Add_Offer extends UtilityRuntimePermission implements View.OnClickListener, Camera.AsyncResponse, PopupMenu.OnMenuItemClickListener {

    CircleImageView circleImageView;
    Button ll_upload_image, btn_offer;
    EditText edt_offerName, edt_OfferDescription, edt_OfferCode, edt_OfferLimit, edt_terms;
    EditText edt_validity, edt_price, offerType;
    private Camera camera;
    private static final int IMG_REQUEST = 1;
    private String profile_image_name = "", profile_image_path = "";
    private ProgressDialog progressDialog;
    final Calendar myCalendar = Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String offerTypeString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__offer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.offerCreation));
        camera = new Camera(this);
        getViews();

       /* DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };*/
        edt_validity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Add_Offer.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //edt_validity.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                edt_validity.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
     /*   edt_validity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Activity_Add_Offer.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/
        offerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Activity_Add_Offer.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.my_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(Activity_Add_Offer.this);

                popupMenu.show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_validity.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void processFinish(String result, int img_no) {
        String[] parts = result.split("/");
        String imagename = parts[parts.length - 1];
        Log.d("Image_path", result + " " + img_no);
        profile_image_name = imagename;
        profile_image_path = result;
    }

    private void getViews() {
        circleImageView = findViewById(R.id.image);

        edt_offerName = findViewById(R.id.edt_offerName);
        edt_OfferDescription = findViewById(R.id.edt_OfferDescription);
        edt_validity = findViewById(R.id.edt_validity);
        edt_price = findViewById(R.id.edt_price);
        edt_terms = findViewById(R.id.edt_terms);
        offerType = findViewById(R.id.offerType);
        edt_OfferLimit = findViewById(R.id.edt_OfferLimit);
        edt_OfferCode = findViewById(R.id.edt_OfferCode);
        ll_upload_image = findViewById(R.id.ll_upload_image);
        ll_upload_image.setOnClickListener(this);

        btn_offer = findViewById(R.id.btn_offer);

        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validates()) {
                    submitOffer();
                }
            }
        });

    }

    private void submitOffer() {
        if (profile_image_path == null) {
            Toast.makeText(Activity_Add_Offer.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(Activity_Add_Offer.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            //progressDialog.setCancelable(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            if(offerType.getText().toString().equals("Percent wise")){
                offerTypeString = "1";
            }else if(offerType.getText().toString().equals("Flat Off")){
                offerTypeString = "2";
            }else if(offerType.getText().toString().equals("Upto Off")){
                offerTypeString = "3";
            }

            uploadFile(profile_image_path, profile_image_name, offerTypeString);
        }
    }

    private void uploadFile(String profile_image_path, String profile_image_name, String offerTypeString) {
        FileUploadService service = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {

            File file = new File(profile_image_path);
            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("offer_image", profile_image_name, requestFile);
        }
        RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(Activity_Add_Offer.this, Constants.REG_ID));
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), edt_offerName.getText().toString());
        RequestBody validity = RequestBody.create(MediaType.parse("text/plain"), edt_validity.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_OfferDescription.getText().toString());
        RequestBody offer_type = RequestBody.create(MediaType.parse("text/plain"), offerTypeString);
        RequestBody offer_price = RequestBody.create(MediaType.parse("text/plain"), edt_price.getText().toString());
        RequestBody terms = RequestBody.create(MediaType.parse("text/plain"), edt_terms.getText().toString());
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody offer_code = RequestBody.create(MediaType.parse("text/plain"), edt_OfferCode.getText().toString());
        RequestBody offer_limit = RequestBody.create(MediaType.parse("text/plain"), "0");


        Call<ResponseBody> responseBodyCall;
        responseBodyCall = service.upload(userID, action, name, validity, description, offer_type, offer_price, terms, status, offer_code, offer_limit, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject object = new JSONObject(output);
                    boolean result = object.getBoolean("result");
                    String message = object.getString("reason");
                    if (result) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Add_Offer.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Add_Offer.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Add_Offer.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Add_Offer.this, MainActivity.class);
                        startActivity(intent);
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

    private boolean validates() {
        boolean res = true;
        if (!MyValidator.isValidField(edt_offerName)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_OfferDescription)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_validity)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_price)) {
            res = false;
        }
        if (!MyValidator.isValidField(offerType)) {
            res = false;
        }
       /* if (!MyValidator.isValidField(edt_OfferLimit)) {
            res = false;
        }*/
        if (!MyValidator.isValidField(edt_OfferCode)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_terms)) {
            res = false;
        }
        return res;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_upload_image) {
            if (Activity_Add_Offer.super.requestAppPermissions(this))
                camera.selectImage(circleImageView, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.slot1:
                String first = item.getTitle().toString();
                offerType.setText(first);
                return true;
            case R.id.slot2:
                String second = item.getTitle().toString();
                offerType.setText(second);
                return true;
            case R.id.slot3:
                String third = item.getTitle().toString();
                offerType.setText(third);
                return true;
            default:
                return true;
        }

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

    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/createUpdateOfferMaster")
        Call<ResponseBody> upload(


                @Part("user_id") @Nullable RequestBody userID,
                @Part("action") @Nullable RequestBody action,
                @Part("name") @Nullable RequestBody name,
                @Part("validity") @Nullable RequestBody validity,
                @Part("description") @Nullable RequestBody description,
                @Part("offer_type") @Nullable RequestBody offerType,
                @Part("offer_price") @Nullable RequestBody offerPrice,
                @Part("terms") @Nullable RequestBody terms,
                @Part("status") @Nullable RequestBody status,
                @Part("offer_code") @Nullable RequestBody offerCode,
                @Part("offer_limit") @Nullable RequestBody offerLimit,
                @Part @Nullable MultipartBody.Part file);
    }
}
