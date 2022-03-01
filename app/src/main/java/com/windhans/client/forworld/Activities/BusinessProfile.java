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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.windhans.client.forworld.Model.BusinessProfileModel;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Medium;
import com.windhans.client.forworld.my_library.CheckNetwork;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class BusinessProfile extends AppCompatActivity {

    ImageView profile_image;
    MyTextView_Poppins_Medium txt_businessName,txt_id,txt_email_id,txt_contact_number,txt_address1;
    MyTextView_Poppins_Medium txt_city,txt_state,txt_country,txt_SellerName,txt_ShopName,txt_ContactNumber,
            txt_googlepay,txt_phonepay,txt_paytm, txt_bankName,txt_acountNo,txt_ifsc,txt_memberName,txt_landMark,txt_district;
    MyTextView_Poppins_Medium txt_dob,txt_gender,txt_pincode,txt_businessLicenceType,txt_liecnceNo,txt_expiry_date,txt_gst_exemption,txt_gst_no,txt_aadhar_no,txt_pan_card_no,txt_shop_age;
    TextView txt_businessDetails;
    List<BusinessProfileModel> businessProfileModelList;
    private ImageView imageView;
    FloatingActionButton fab;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private Button edit;
    ImageView iv_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        iv_edit=findViewById(R.id.iv_edit);
        iv_edit.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Business Profile");
        businessProfileModelList=new ArrayList<>();

        getViews();
        getBusinessDetails();
        fab=findViewById(R.id.fab);
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusinessProfile.this,UpdateBusinessProfile.class);
                intent.putExtra("address",txt_address1.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void getBusinessDetails() {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.getBusinessDetails(Shared_Preferences.getPrefs(BusinessProfile.this,Constants.REG_ID));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    Log.d("BusinessData", "onResponse: "+output);
                    JSONObject jsonObject=new JSONObject(output);
                    boolean result=jsonObject.getBoolean("result");
                    String reason=jsonObject.getString("reason");
                    if (result)
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("businessData");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.DISTRICT_NAME,jsonObject1.getString("district_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.STATE_NAME,jsonObject1.getString("states_name"));

                            businessProfileModelList.add(new BusinessProfileModel(jsonObject1));
                            setData(businessProfileModelList);

                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_ID,jsonObject1.getString("id"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_NAME1,jsonObject1.getString("business_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.CONTACT_NAME,jsonObject1.getString("contact_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.EMAIL_ID,jsonObject1.getString("email"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_EMAIL,jsonObject1.getString("email"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.CONTACT_NO,jsonObject1.getString("contact_mobile"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_CONTACT,jsonObject1.getString("contact_mobile"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.ADDRESS,jsonObject1.getString("address"));
                            Log.d("Address", "onResponse: "+jsonObject1.getString("address"));
                            Log.d("Address", "onResponse: "+Shared_Preferences.getPrefs(BusinessProfile.this,Constants.NEWADDRESS));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.IMAGE1,jsonObject1.getString("business_banner"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.DESCRIPTION,jsonObject1.getString("description"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.DISTRICT,jsonObject1.getString("district"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.CITY,jsonObject1.getString("city"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.STATE,jsonObject1.getString("state"));

                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.SELLER_NAME,jsonObject1.getString("seller_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.SHOP_NAME,jsonObject1.getString("shop_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.CONTACT_NO,jsonObject1.getString("mobile_no2"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BANK_NAME,jsonObject1.getString("bank_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BANK_ACCOUNT,jsonObject1.getString("account_no"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.IFSC,jsonObject1.getString("ifsc"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.MEMBER_NAME,jsonObject1.getString("member_name"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.LAND_MARK,jsonObject1.getString("land_mark"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.PINCODE,jsonObject1.getString("pincode"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_LIECNCE_NO,jsonObject1.getString("licence_no"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.BUSINESS_LIECNCE_TYPE,jsonObject1.getString("business_licence_type"));
                          //  Shared_Preferences.setPrefs(BusinessProfile.this,Constants.LIECNCE_NO,jsonObject1.getString(""));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.EXPIRY_DATE,jsonObject1.getString("expiry_date"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.GST_EXEMPTION,jsonObject1.getString("gst_exemption"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.GST_NUMBER,jsonObject1.getString("gst_no"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.DOB,jsonObject1.getString("birth_date"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.GENDER,jsonObject1.getString("gender"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.AADHAR_NO,jsonObject1.getString("aadhar_no"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.MOBILE2,jsonObject1.getString("mobile_no2"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.GooglePay,jsonObject1.getString("googlepay"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.PhonePay,jsonObject1.getString("phonepay"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.Paytm,jsonObject1.getString("paytm"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.PAN_NO,jsonObject1.getString("pan_card_no"));
                            Shared_Preferences.setPrefs(BusinessProfile.this,Constants.SHOP_AGE,jsonObject1.getString("shop_age"));



                        }
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

    private void setData(List<BusinessProfileModel> businessProfileModelList) {
        for(int i=0;i<businessProfileModelList.size();i++)
        {
            txt_businessName.setText(businessProfileModelList.get(i).getBusiness_name());
            txt_id.setText(businessProfileModelList.get(i).getContact_name());
            txt_email_id.setText(businessProfileModelList.get(i).getEmail());
            txt_address1.setText(businessProfileModelList.get(i).getAddress());
            txt_contact_number.setText(businessProfileModelList.get(i).getContact_mobile());
            txt_businessDetails.setText(businessProfileModelList.get(i).getDescription());
            txt_city.setText(businessProfileModelList.get(i).getCity());
            txt_state.setText(Shared_Preferences.getPrefs(BusinessProfile.this, Constants.STATE_NAME));
            //txt_country.setText(businessProfileModelList.get(i).get);
            String sellerName=businessProfileModelList.get(i).getSeller_name();
            Log.d("nmae", "setData: "+sellerName);

            if(!businessProfileModelList.get(i).getSeller_name().equals("null")) {
                txt_SellerName.setText(businessProfileModelList.get(i).getSeller_name());
            }else {
                txt_SellerName.setText("");
            }

            if(!businessProfileModelList.get(i).getShop_name().equals("null")) {
                txt_ShopName.setText(businessProfileModelList.get(i).getShop_name());
            }else {
                txt_ShopName.setText("");
            }
            txt_ContactNumber.setText(businessProfileModelList.get(i).getContact_mobile());
            txt_bankName.setText(businessProfileModelList.get(i).getBank_name());
            txt_acountNo.setText(businessProfileModelList.get(i).getAccount_no());
            txt_ifsc.setText(businessProfileModelList.get(i).getIfsc());
            txt_memberName.setText(businessProfileModelList.get(i).getMember_name());
            txt_dob.setText(businessProfileModelList.get(i).getDob());

            txt_dob.setText(businessProfileModelList.get(i).getDob());
            txt_gender.setText(businessProfileModelList.get(i).getGender());
            txt_pincode.setText(businessProfileModelList.get(i).getPincode());
            txt_businessLicenceType.setText(businessProfileModelList.get(i).getBusiness_licence_type());
            txt_liecnceNo.setText(businessProfileModelList.get(i).getLicence_no());
            txt_expiry_date.setText(businessProfileModelList.get(i).getExpiry_date());
            txt_gst_exemption.setText(businessProfileModelList.get(i).getGst_exemption());
            if(businessProfileModelList.get(i).getGst_exemption().equals("No")){
                txt_gst_no.setVisibility(View.GONE);
            }else {
                txt_gst_no.setVisibility(View.VISIBLE);
            }
            txt_gst_no.setText(businessProfileModelList.get(i).getGst_no());
            txt_aadhar_no.setText(businessProfileModelList.get(i).getAadhar_no());
            txt_pan_card_no.setText(businessProfileModelList.get(i).getPan_card_no());
            txt_shop_age.setText(businessProfileModelList.get(i).getShop_age());
            txt_district.setText(Shared_Preferences.getPrefs(BusinessProfile.this, Constants.DISTRICT_NAME));
            txt_landMark.setText(businessProfileModelList.get(i).getLand_mark());
            txt_googlepay.setText(businessProfileModelList.get(i).getGooglepay());
            txt_phonepay.setText(businessProfileModelList.get(i).getPhonepay());
            txt_paytm.setText(businessProfileModelList.get(i).getPaytm());


            String image= businessProfileModelList.get(i).getBusiness_banner();
            if (image != null && !image.isEmpty() && !image.equals("null")) {
                Log.d("image", "response: ");

                Glide.with(BusinessProfile.this)
                        .load(MyConfig.JSON_BusinessPath + image)

                        .into(profile_image);
            } else {
                Glide.with(BusinessProfile.this)
                        .load(R.drawable.no_image_available)

                        .into(profile_image);
            }



        }

    }

    private void getViews() {
        txt_businessName=findViewById(R.id.txt_businessName);
        txt_id=findViewById(R.id.txt_id);
        txt_email_id=findViewById(R.id.txt_email_id);
        txt_contact_number=findViewById(R.id.txt_contact_number);
        txt_address1=findViewById(R.id.txt_address1);
        profile_image=findViewById(R.id.profile_image);

        txt_city=findViewById(R.id.txt_city);
        txt_state=findViewById(R.id.txt_state);
        txt_country=findViewById(R.id.txt_country);
        txt_SellerName=findViewById(R.id.txt_SellerName);
        txt_ShopName=findViewById(R.id.txt_ShopName);
        txt_ContactNumber=findViewById(R.id.txt_ContactNumber);
        txt_bankName=findViewById(R.id.txt_bankName);
        txt_acountNo=findViewById(R.id.txt_acountNo);
        txt_ifsc=findViewById(R.id.txt_ifsc);
        txt_memberName=findViewById(R.id.txt_memberName);
        txt_businessDetails=findViewById(R.id.txt_businessDetails);

        txt_dob=findViewById(R.id.txt_dob);
        txt_gender=findViewById(R.id.txt_gender);
        txt_pincode=findViewById(R.id.txt_pincode);
        txt_businessLicenceType=findViewById(R.id.txt_businessLicenceType);
        txt_liecnceNo=findViewById(R.id.txt_liecnceNo);
        txt_expiry_date=findViewById(R.id.txt_expiry_date);
        txt_gst_exemption=findViewById(R.id.txt_gst_exemption);
        txt_gst_no=findViewById(R.id.txt_gst_no);
        txt_aadhar_no=findViewById(R.id.txt_aadhar_no);
        txt_pan_card_no=findViewById(R.id.txt_pan_card_no);
        txt_shop_age=findViewById(R.id.txt_shop_age);
        txt_landMark=findViewById(R.id.txt_landMark);
        txt_district=findViewById(R.id.txt_district);
        txt_googlepay=findViewById(R.id.txt_googlepay);
        txt_phonepay=findViewById(R.id.txt_phonepay);
        txt_paytm=findViewById(R.id.txt_paytm);


    }

    public  interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/getBusinessProfile")
        Call<ResponseBody> getBusinessDetails(
                @Field("user_id") String user_id
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
    @Override
    public void onResume() {
        super.onResume();
        check_connection();
    }

    private void check_connection() {
        if (CheckNetwork.isInternetAvailable(this))  //if connection available
        {

        /*    noConnectionLayout.setVisibility(View.GONE);
            noRecordLayout.setVisibility(View.GONE);*/
            getBusinessDetails();
        } else {

            //noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }
}
