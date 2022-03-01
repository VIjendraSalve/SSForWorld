package com.windhans.client.forworld.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;
import com.windhans.client.forworld.my_library.UtilityRuntimePermission;

import org.json.JSONObject;

import java.io.File;
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

public class UpdateCapanignActivity extends UtilityRuntimePermission implements View.OnClickListener,Camera.AsyncResponse {
    EditText edt_description,edt_title;
    CircleImageView civ_event_image_upload;
    EditText edt_fromDate,edt_todate;
    RadioGroup radioGroup1,radioGroup2;
    Button btn_submit;
    RadioButton radioButton_yes,radioButton_no,radioButton2,radioButton1,radioButton,radioButtonNew;
    private String profile_image_name = "", profile_image_path = "";
    private Camera camera;
    private ProgressDialog progressDialog;
    Button ll_upload_image;
    final Calendar myCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_capanign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Update Campaign");
        camera = new Camera(this);
        getViews();
        setValues();
    }

    private void setValues() {
        edt_title.setText(Shared_Preferences.getPrefs(UpdateCapanignActivity.this, Constants.CAMPAIGN_TITLE));
        edt_description.setText(Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.CAMPAIGN_DECRIPTION));
        String from_date=Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.CAMPAIGN_FROM_DATE);
        String to_date=Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.CAMPAIGN_TO_DATE);
        Log.d("fromDate", "setValues: "+from_date+","+to_date);
        edt_fromDate.setText(from_date);
        edt_todate.setText(to_date);
        String image=Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.CAMPAIGN_BANNER);
        if(!image.equals("null")&&!image.isEmpty())
        {
            Glide
                    .with(UpdateCapanignActivity.this)
                    .load(MyConfig.JSON_BusinessPath+image)

                    .into(civ_event_image_upload);
        }
        else {
            Glide
                    .with(UpdateCapanignActivity.this)
                    .load(R.drawable.no_image)

                    .into(civ_event_image_upload);
        }
        String  isEnableEnquiry=Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.IS_ENABLE_ENQUIRY);
        if(!isEnableEnquiry.equals("null")&&!isEnableEnquiry.isEmpty())
        {
            if(isEnableEnquiry.equals("Yes"))
            {
                radioButton1.setChecked(true);
            }
            else {
                radioButton2.setChecked(true);
            }
        }

        String isLocationBasedEnquiry=Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.IS_LOCATION_BASED_ENQUIRY);
        Log.d("isLoction", "setValues: "+isLocationBasedEnquiry);
        if(!isLocationBasedEnquiry.equals("null")&&!isLocationBasedEnquiry.isEmpty())
        {
            if(isLocationBasedEnquiry.equals("Yes"))
            {
                radioButton_yes.setChecked(true);
            }
            else {
                radioButton_no.setChecked(true);
            }
        }
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
        edt_description=findViewById(R.id.edt_description);
        edt_title=findViewById(R.id.edt_title);
        civ_event_image_upload=findViewById(R.id.civ_event_image_upload);
        edt_fromDate=findViewById(R.id.edt_fromDate);
        edt_todate=findViewById(R.id.edt_todate);

        radioGroup1=findViewById(R.id.radioGroup1);
        radioGroup2=findViewById(R.id.radioGroup2);

        radioButton_no=findViewById(R.id.radioButton_no);
        radioButton_yes=findViewById(R.id.radioButton_yes);

        radioButton1=findViewById(R.id.radioButton1);
        radioButton2=findViewById(R.id.radioButton2);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        edt_fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateCapanignActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edt_todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateCapanignActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid())
                {
                    if (profile_image_path == null) {
                        Toast.makeText(UpdateCapanignActivity.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(UpdateCapanignActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        //progressDialog.setCancelable(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        uploadFile(profile_image_path, profile_image_name);
                    }
                } else {
                    Toast.makeText(UpdateCapanignActivity.this, "Please Fill Complete Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ll_upload_image=findViewById(R.id.ll_upload_image);
        ll_upload_image.setOnClickListener(this);
    }

    private void uploadFile(String profile_image_path, String profile_image_name) {
       FileUploadService service = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {

            File file = new File(profile_image_path);


            // String mimeType= URLConnection.guessContentTypeFromName(file.getName());

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name

            body = MultipartBody.Part.createFormData("ad_banner", profile_image_name, requestFile);
        }
        RequestBody campaign_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.CAMPAIGN_ID));
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), "update");
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.REG_ID));
        RequestBody business_id = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(UpdateCapanignActivity.this,Constants.BUSINESS_ID));
        RequestBody ads_unit_id = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody from_date = RequestBody.create(MediaType.parse("text/plain"), edt_fromDate.getText().toString().trim());
        RequestBody to_date = RequestBody.create(MediaType.parse("text/plain"), edt_todate.getText().toString().trim());
        Log.d("id", "uploadFile: "+campaign_id+","+business_id);
        int selectedId = radioGroup1.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        RequestBody is_enable_enquiry = RequestBody.create(MediaType.parse("text/plain"), radioButton.getText().toString().trim());
        Log.d("selectedID", "uploadFile: "+is_enable_enquiry);

        int selectedId1 = radioGroup2.getCheckedRadioButtonId();
        radioButtonNew = (RadioButton) findViewById(selectedId1);
        RequestBody is_locationbased_enquiry = RequestBody.create(MediaType.parse("text/plain"), radioButtonNew.getText().toString().trim());
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), edt_title.getText().toString().trim());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_description.getText().toString().trim());
        RequestBody search = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody page_count = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);
        call = service.upload(campaign_id, action, user_id, business_id, ads_unit_id,from_date, to_date,is_enable_enquiry,is_locationbased_enquiry,title,description,search,page_count,body);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.v("Upload", "success");
                String output = "";
                try {
                    output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("status"));
                    Log.d("Result_New", output);

                    String reason = jsonObject.getString("message");
                    Log.d("message", reason);
                    /*if (jsonObject.getBoolean(Constants.IS_SESSION_EXPIRED)) {
                        Constants.setIsSessionExpired(ActivityUpdateProfile.this);
                    } else {*/
                    if (res) {

                        // progressDialog.dismiss();
                        Toast.makeText(UpdateCapanignActivity.this, reason, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else
                        Toast.makeText(UpdateCapanignActivity.this, "" + reason, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    //}

                    //  Log.v("Upload", "" + response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Upload_error:", "");
                progressDialog.dismiss();
            }
        });
    }
    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/campaignMaster")
        Call<ResponseBody> upload(


                /*    @Field("product_image") String product_image*/
                @Part("campaign_id") @Nullable RequestBody campaign_id,
                @Part("action") @Nullable RequestBody action,
                @Part("user_id") @Nullable RequestBody user_id,
                @Part("business_id") @Nullable RequestBody business_id,
                @Part("ads_unit_id") @Nullable RequestBody ads_unit_id,
                @Part("from_date") @Nullable RequestBody from_date,
                @Part("to_date") @Nullable RequestBody to_date,
                @Part("is_enable_enquiry") @Nullable RequestBody is_enable_enquiry,
                @Part("is_locationbased_enquiry") @Nullable RequestBody is_locationbased_enquiry,
                @Part("title") @Nullable RequestBody title,
                @Part("description") @Nullable RequestBody description,
                @Part("search") @Nullable RequestBody search,
                @Part("page_no") @Nullable RequestBody page_no,
                @Part @Nullable MultipartBody.Part file);
    }
    private boolean isValid() {
        boolean res=true;
        if(!MyValidator.isValidField(edt_title))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_description))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_fromDate))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_todate))
        {
            res=false;
        }
        return res;
    }

    private void updateLabel1() {
     //   String myFormat = "MM/dd/yy"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edt_todate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edt_fromDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_upload_image) {
            if (UpdateCapanignActivity.super.requestAppPermissions(this))
                camera.selectImage(civ_event_image_upload, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);

    }

}
