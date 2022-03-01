package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.windhans.client.forworld.Model.District;
import com.windhans.client.forworld.Model.State;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.MyValidator;
import com.windhans.client.forworld.my_library.Shared_Preferences;
import com.windhans.client.forworld.my_library.UtilityRuntimePermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class UpdateBusinessProfile extends UtilityRuntimePermission implements View.OnClickListener,Camera.AsyncResponse {

    CircleImageView profile_image;
    EditText edt_businessName,edt_conatctName,edt_emailID,edt_contactNo,edt_address,edt_description;
    EditText edt_city,edt_state,edt_sellerName,edt_shopName,edt_bankName,edt_accountNumber,edt_ifsc,edt_memberName,edt_landMark,edt_district;
    EditText edt_pincodeNumber,edt_businessLiecnceType,edt_LiecnceNo,edt_expiry_date,edt_gst_exemption,edt_gst_no,edt_aadhar_no,
            edt_panCard,edt_shopage,edt_gender,edt_DOB,edt_contactNo2, edt_googlepay, edt_phonepay,edt_paytm;
    Button btn_update;
    private Camera camera;
    private static final int IMG_REQUEST = 1;
    private String profile_image_name = "", profile_image_path = "";
    private ProgressDialog progressDialog;
    Button ll_upload_image;

    private ArrayList<State> stateArrayList = new ArrayList<>();
    private ArrayList<District> districtArrayList = new ArrayList<>();
    private SearchableSpinner spinner_state, spinner_district;
    private String stateId="", districtId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_business_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.updatebusinessprofile));
        progressDialog = new ProgressDialog(UpdateBusinessProfile.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);

        getViews();
        setData();
        camera = new Camera(this);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
               /* {
                    productName=edt_productName.getText().toString();
                    brand=edt_brand.getText().toString();
                    price=edt_price.getText().toString();
                    description=edt_description.getText().toString();
                    addProduct(productName,brand,price,description);
                }*/
                {
                    if (profile_image_path == null) {
                        Toast.makeText(UpdateBusinessProfile.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        uploadFile(profile_image_path, profile_image_name);
                    }
                } else {
                    Toast.makeText(UpdateBusinessProfile.this, "Please Fill Complete Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String result, int img_no) {
        String[] parts = result.split("/");
        String imagename = parts[parts.length - 1];
        Log.d("Image_path", result + " " + img_no);
        profile_image_name = imagename;
        profile_image_path = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);
      /*  if(requestCode==IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                civ_register_user_profile_picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/updateBusinessProfile")
        Call<ResponseBody> upload(
                /*    @Field("product_image") String product_image*/
                @Part("user_id") @Nullable RequestBody userId,
                @Part("business_id") @Nullable RequestBody businessID,
                @Part("business_name") @Nullable RequestBody businessName,
                @Part("description") @Nullable RequestBody dec,
                @Part("contact_mobile") @Nullable RequestBody contact_mobile,
                @Part("email") @Nullable RequestBody email,
                @Part("address") @Nullable RequestBody address,
                @Part("land_mark") @Nullable RequestBody landMark,
                @Part("city") @Nullable RequestBody city,
                @Part("district") @Nullable RequestBody district,
                @Part("state") @Nullable RequestBody state,
                @Part("contact_name") @Nullable RequestBody contact,
                @Part("pincode") @Nullable RequestBody pincode,
                @Part("business_licence_type") @Nullable RequestBody licenceType,
                @Part("licence_no") @Nullable RequestBody licenceNo,
                @Part("expiryDate") @Nullable RequestBody expiryDate,
                @Part("itr") @Nullable RequestBody itr,
                @Part("shop_age") @Nullable RequestBody shop_age,
                @Part("gst_exemption") @Nullable RequestBody gst_exemption,
                @Part("gst_no") @Nullable RequestBody gst_no,
                @Part("dob") @Nullable RequestBody dob,
                @Part("gender") @Nullable RequestBody gender,
                @Part("aadhar_no") @Nullable RequestBody aadharNo,
                @Part("pan_card_no") @Nullable RequestBody pan_card,
                @Part("bank_name") @Nullable RequestBody bank_name,
                @Part("account_no") @Nullable RequestBody account_no,
                @Part("ifsc") @Nullable RequestBody ifsc,
                @Part("member_name") @Nullable RequestBody member_name,
                @Part("remark") @Nullable RequestBody remark,
                @Part("seller_name") @Nullable RequestBody seller_name,
                @Part("shop_name") @Nullable RequestBody shop_name,
                @Part("mobile_no2") @Nullable RequestBody mobile_no2,
                @Part("googlepay") @Nullable RequestBody googlepay,
                @Part("phonepay") @Nullable RequestBody phonepay,
                @Part("paytm") @Nullable RequestBody paytm,
                @Part @Nullable MultipartBody.Part file);

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getStates")
        public Call<ResponseBody> getStates(
                @Field("user_id") String user_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getDistrict")
        public Call<ResponseBody> getDistrict(
                @Field("user_id") String user_id,
                @Field("state_id") String state_id
        );
    }

    private void uploadFile(String profile_image_path, String profile_image_name) {

        FileUploadService service = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {

            File file = new File(profile_image_path);


            // String mimeType= URLConnection.guessContentTypeFromName(file.getName());

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name

            body = MultipartBody.Part.createFormData("business_banner", profile_image_name, requestFile);
        }


        // RequestBody user_role_id = RequestBody.create(MediaType.parse("text/plain"), Constants.Reg_id(this));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.REG_ID));
        Log.d("userid", "uploadFile: "+ Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.REG_ID));
        Log.d("bid", "uploadFile: "+ Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.BUSINESS_ID));
        Log.d("name", "uploadFile: "+edt_conatctName.getText().toString());
        Log.d("businame", "uploadFile: "+edt_businessName.getText().toString());
        Log.d("des", "uploadFile: "+  edt_description.getText().toString());
        Log.d("email", "uploadFile: "+  edt_emailID.getText().toString().trim());
        Log.d("add", "uploadFile: "+   edt_address.getText().toString());
        RequestBody business_id = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_ID));
        RequestBody business_name = RequestBody.create(MediaType.parse("text/plain"), edt_businessName.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_description.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edt_emailID.getText().toString().trim());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edt_address.getText().toString());
        RequestBody contact_name = RequestBody.create(MediaType.parse("text/plain"), edt_conatctName.getText().toString());
        RequestBody pincode = RequestBody.create(MediaType.parse("text/plain"), edt_pincodeNumber.getText().toString());
        RequestBody licenceType = RequestBody.create(MediaType.parse("text/plain"), edt_businessLiecnceType.getText().toString());
        RequestBody licenceNo = RequestBody.create(MediaType.parse("text/plain"), edt_LiecnceNo.getText().toString());
        RequestBody expiryDate = RequestBody.create(MediaType.parse("text/plain"), edt_expiry_date.getText().toString());
        RequestBody gst_exemption = RequestBody.create(MediaType.parse("text/plain"), edt_expiry_date.getText().toString());
        RequestBody gst_no = RequestBody.create(MediaType.parse("text/plain"), edt_gst_no.getText().toString());
        RequestBody aadharNo = RequestBody.create(MediaType.parse("text/plain"), edt_aadhar_no.getText().toString());
        RequestBody pan_card = RequestBody.create(MediaType.parse("text/plain"), edt_panCard.getText().toString());
        RequestBody shop_age = RequestBody.create(MediaType.parse("text/plain"), edt_shopage.getText().toString());
        RequestBody itr = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), edt_DOB.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), edt_gender.getText().toString());
        RequestBody bank_name = RequestBody.create(MediaType.parse("text/plain"), edt_bankName.getText().toString());
        RequestBody account_no = RequestBody.create(MediaType.parse("text/plain"), edt_accountNumber.getText().toString());
        RequestBody ifsc = RequestBody.create(MediaType.parse("text/plain"), edt_ifsc.getText().toString());
        RequestBody member_name = RequestBody.create(MediaType.parse("text/plain"), edt_memberName.getText().toString());
        RequestBody remark = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody seller_name = RequestBody.create(MediaType.parse("text/plain"),edt_sellerName.getText().toString());
        RequestBody shop_name = RequestBody.create(MediaType.parse("text/plain"),edt_shopName.getText().toString());
        RequestBody mobile2 = RequestBody.create(MediaType.parse("text/plain"),edt_contactNo2.getText().toString());
        RequestBody landMark = RequestBody.create(MediaType.parse("text/plain"),edt_landMark.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"),edt_city.getText().toString());
        RequestBody district = RequestBody.create(MediaType.parse("text/plain"),edt_district.getText().toString());
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),edt_state.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"),edt_contactNo2.getText().toString());
        RequestBody googlepay = RequestBody.create(MediaType.parse("text/plain"),edt_googlepay.getText().toString());
        RequestBody phonepay = RequestBody.create(MediaType.parse("text/plain"),edt_phonepay.getText().toString());
        RequestBody paytm = RequestBody.create(MediaType.parse("text/plain"),edt_paytm.getText().toString());


//        RequestBody api_token = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(RegisterActivity.this, Constants.API_TOKEN));
        //RequestBody user_last_name = RequestBody.create(MediaType.parse("text/plain"), et_update_profile_lastname.getText().toString());

        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);
        call = service.upload(user_id, business_id, business_name, description,mobile, email,address,landMark,city,district,
                state,contact_name,pincode,licenceType,licenceNo,expiryDate,itr,shop_age,gst_exemption,gst_no,dob,gender,
                aadharNo,pan_card,bank_name,account_no,ifsc,member_name,remark,seller_name,shop_name,mobile2,
                googlepay, phonepay, paytm,body);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.v("Upload", "success");
                String output = "";
                try {
                    output = response.body().string();

                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    Log.d("Result_New", output);

                    String reason = jsonObject.getString("reason");
                    Log.d("message", reason);
                    /*if (jsonObject.getBoolean(Constants.IS_SESSION_EXPIRED)) {
                        Constants.setIsSessionExpired(ActivityUpdateProfile.this);
                    } else {*/
                    if (res) {

                        // progressDialog.dismiss();
                        Toast.makeText(UpdateBusinessProfile.this, reason, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else
                        Toast.makeText(UpdateBusinessProfile.this, "" + reason, Toast.LENGTH_SHORT).show();
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

    private boolean isValid() {
        boolean res=true;
        if(!MyValidator.isValidField(edt_businessName))
        {
            res=false;
        }
        if (!MyValidator.isValidField(edt_conatctName))
        {
            res=false;
        }
        if (!MyValidator.isValidField(edt_address))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_emailID))
        {
            res=false;
        }

      /*  if(!MyValidator.isValidField(edt_description))
        {
            res=false;
        }*/
        return res;
    }

    private void setData() {
        Intent intent=getIntent();
        String image= Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.IMAGE1);
        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Log.d("image", "response: "+image);

            Glide.with(UpdateBusinessProfile.this)
                    .load(MyConfig.JSON_BusinessImage + image)
                    .into(profile_image);
        } else {
            Log.d("image", "response: "+image);
            Glide.with(UpdateBusinessProfile.this)
                    .load(R.drawable.no_image_available)

                    .into(profile_image);
        }
        edt_businessName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_NAME1));
        edt_conatctName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.CONTACT_NAME));
        edt_emailID.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.EMAIL_ID));
        edt_contactNo.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_CONTACT));
        Log.d("Add", "setData: "+Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.NEWADDRESS));
        String add=intent.getStringExtra("address");
        //edt_address.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.NEWADDRESS));
        edt_address.setText(add);
        edt_description.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DESCRIPTION));

        edt_city.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.CITY));
        edt_state.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.STATE));
        edt_sellerName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.SELLER_NAME));
        edt_shopName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.SHOP_NAME));
        edt_bankName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BANK_NAME));
        edt_accountNumber.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BANK_ACCOUNT));
        edt_ifsc.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.IFSC));
        edt_memberName.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.MEMBER_NAME));

        edt_pincodeNumber.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.PINCODE));
        edt_businessLiecnceType.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_LIECNCE_TYPE));
        edt_LiecnceNo.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_LIECNCE_NO));
        edt_expiry_date.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.EXPIRY_DATE));
        edt_gst_exemption.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.GST_EXEMPTION));
        edt_gst_no.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.GST_NUMBER));
        edt_aadhar_no.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.AADHAR_NO));
        edt_panCard.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.PAN_NO));
        edt_shopage.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.SHOP_AGE));
        edt_gender.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.GENDER));
        edt_DOB.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DOB));
        edt_contactNo2.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.BUSINESS_CONTACT));
        edt_landMark.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.LAND_MARK));
        edt_district.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DISTRICT));
        edt_googlepay.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.GooglePay));
        edt_phonepay.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.PhonePay));
        edt_paytm.setText(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.Paytm));

    }

    private void getViews() {
        profile_image=findViewById(R.id.profile_image);
        edt_businessName=findViewById(R.id.edt_businessName);
        edt_conatctName=findViewById(R.id.edt_conatctName);
        edt_emailID=findViewById(R.id.edt_emailID);
        edt_contactNo=findViewById(R.id.edt_contactNo);
        edt_description=findViewById(R.id.edt_description);
        edt_address=findViewById(R.id.edt_address);

        edt_city=findViewById(R.id.edt_city);
        edt_state=findViewById(R.id.edt_state);
        edt_sellerName=findViewById(R.id.edt_sellerName);
        edt_shopName=findViewById(R.id.edt_shopName);
        edt_bankName=findViewById(R.id.edt_bankName);
        edt_accountNumber=findViewById(R.id.edt_accountNumber);
        edt_ifsc=findViewById(R.id.edt_ifsc);
        edt_memberName=findViewById(R.id.edt_memberName);


        edt_pincodeNumber=findViewById(R.id.edt_pincodeNumber);
        edt_businessLiecnceType=findViewById(R.id.edt_businessLiecnceType);
        edt_LiecnceNo=findViewById(R.id.edt_LiecnceNo);
        edt_expiry_date=findViewById(R.id.edt_expiry_date);
        edt_gst_exemption=findViewById(R.id.edt_gst_exemption);
        edt_gst_no=findViewById(R.id.edt_gst_no);
        edt_aadhar_no=findViewById(R.id.edt_aadhar_no);
        edt_panCard=findViewById(R.id.edt_panCard);
        edt_shopage=findViewById(R.id.edt_shopage);
        edt_DOB=findViewById(R.id.edt_DOB);
        edt_gender=findViewById(R.id.edt_gender);
        edt_contactNo2=findViewById(R.id.edt_contactNo2);
        edt_landMark=findViewById(R.id.edt_landMark);
        edt_district=findViewById(R.id.edt_district);
        edt_googlepay=findViewById(R.id.edt_googlepay);
        edt_phonepay=findViewById(R.id.edt_phonepay);
        edt_paytm=findViewById(R.id.edt_paytm);

        btn_update=findViewById(R.id.btn_update);
        ll_upload_image = (Button) findViewById(R.id.ll_upload_image);

        spinner_state = (SearchableSpinner)findViewById(R.id.spinner_state);
        spinner_district = (SearchableSpinner)findViewById(R.id.spinner_district);

        ll_upload_image.setOnClickListener(this);
        getStateData();
    }

    private void getStateData() {
        stateArrayList.clear();
        progressDialog.show();
        FileUploadService api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getStates(
                Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.REG_ID));
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org", "onResponsecatlist: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        progressDialog.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("state_name");
                        // Parsing json
                        //stateArrayList.add(new State("-1","Select State"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                stateArrayList.add(new State(obj));
                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }


                        Log.d("listState", "onResponse: " + stateArrayList.size());
                        ArrayAdapter<State> dataAdapter = new ArrayAdapter<State>(UpdateBusinessProfile.this, android.R.layout.simple_spinner_item, stateArrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_state.setAdapter(dataAdapter);

                        for (int i = 0; i <stateArrayList.size() ; i++) {

                            if(stateArrayList.get(i).getId().equals(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.STATE))){
                                spinner_state.setSelection(i);
                            }

                        }

                        stateId = Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.STATE);
                        getDistrictData(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.STATE));

                        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //  On selecting a spinner item
                                //String item = adapterView.getItemAtPosition(i).toString();
                                stateId = stateArrayList.get(i).getId();
                                if(Integer.parseInt(stateId)>0) {
                                    getDistrictData(stateId);
                                }
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });

                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(UpdateBusinessProfile.this, ""+jsonObject.getString("reason"), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getDistrictData(String stateID) {
        districtArrayList.clear();
        progressDialog.show();
        FileUploadService api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getDistrict(
                Shared_Preferences.getPrefs(UpdateBusinessProfile.this, Constants.REG_ID),
                stateID);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {

                    output = response.body().string();
                    Log.d("org", "onResponsecatlist: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        progressDialog.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("state_name");
                        // Parsing json
                        //districtArrayList.add(new District("-1","Select District", "0"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                districtArrayList.add(new District(obj));
                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }
                        Log.d("DistrictID", "onResponse: " + Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DISTRICT));
                        ArrayAdapter<District> dataAdapter = new ArrayAdapter<District>(UpdateBusinessProfile.this, android.R.layout.simple_spinner_item, districtArrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_district.setAdapter(dataAdapter);

                        for (int i = 0; i <districtArrayList.size() ; i++) {
                            if(districtArrayList.get(i).getId().equals(Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DISTRICT))){
                                spinner_district.setSelection(i);
                            }
                        }

                        districtId = Shared_Preferences.getPrefs(UpdateBusinessProfile.this,Constants.DISTRICT);
                        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //  On selecting a spinner item
                                //String item = adapterView.getItemAtPosition(i).toString();
                                districtId = districtArrayList.get(i).getId();
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });
                    }else {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_upload_image) {
            if (UpdateBusinessProfile.super.requestAppPermissions(this))
                camera.selectImage(profile_image, 0);
        }
    }
}
