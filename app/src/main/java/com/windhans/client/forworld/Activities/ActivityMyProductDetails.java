package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.windhans.client.forworld.Model.SubCategory;
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
import java.io.IOException;
import java.util.ArrayList;

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

public class ActivityMyProductDetails extends UtilityRuntimePermission implements View.OnClickListener,Camera.AsyncResponse {

    TextInputEditText edt_productName,edt_brand,edt_price;
    EditText edt_description;
    Button btn_update;
    Spinner spinner_category;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    private Button ll_upload_image;
    private CircleImageView civ_event_image_upload;
    private ArrayList<SubCategory> stringList;
    private ArrayAdapter spinnerArrayAdapter;

    private Camera camera;
    private static final int IMG_REQUEST = 1;
    private String profile_image_name = "", profile_image_path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


     /*   imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.details));
        camera = new Camera(this);
        progressDialog=new ProgressDialog(ActivityMyProductDetails.this);
        getViews();
    }

    private void getViews() {
        edt_productName=findViewById(R.id.edt_productName);
        edt_brand=findViewById(R.id.edt_brand);
        edt_price=findViewById(R.id.edt_price);
        edt_description=findViewById(R.id.edt_description);

        spinner_category=findViewById(R.id.spinner_category);
        ll_upload_image = (Button) findViewById(R.id.ll_upload_image);
        civ_event_image_upload=findViewById(R.id.civ_event_image_upload);
        ll_upload_image.setOnClickListener(this);
        stringList=new ArrayList<>();
        getData();

        setData();

        String image=Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.PRODUCT_IMAGE);
        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Log.d("image", "response: ");

            Glide.with(ActivityMyProductDetails.this)
                    .load(MyConfig.JSON_BusinessImage + image)

                    .into(civ_event_image_upload);
        } else {
            Glide.with(ActivityMyProductDetails.this)
                    .load(R.drawable.no_image_available)

                    .into(civ_event_image_upload);
        }





        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                Toast.makeText(ActivityMyProductDetails.this, stringList.get(position).getId(), Toast.LENGTH_SHORT).show();
                String subID=Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.SUB_ID);
                for(int i=0;i<stringList.size();i++)

                {
                    String sub=stringList.get(i).getId();
                    Log.d("cate", "getViews: "+stringList.size());
                    if(subID.equals(sub))
                    {
                        spinner_category.setSelection(i);
                        Log.d("cate1", "getViews: "+sub);
                    }
                }
              /*  int subCategory= Integer.parseInt(Shared_Preferences.getPrefs(ActivityMyProductDetails.this, Constants.SUB_ID));
                String sub= String.valueOf(stringList.get(position).getId());
                spinner_category.setSelection(Integer.parseInt(sub));
                Shared_Preferences.setPrefs(ActivityMyProductDetails.this, Constants.SUB_ID,stringList.get(position).getId());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_update=findViewById(R.id.btn_update);
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
                        Toast.makeText(ActivityMyProductDetails.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(ActivityMyProductDetails.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        //progressDialog.setCancelable(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        uploadFile(profile_image_path, profile_image_name);
                    }
                } else {
                    Toast.makeText(ActivityMyProductDetails.this, "Please Fill Complete Data", Toast.LENGTH_SHORT).show();
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
        @POST(MyConfig.SSWORLD + "/updateProduct")
        Call<ResponseBody> upload(
                @Part("product_id") @Nullable RequestBody product_id,
                @Part("subcategory_id") @Nullable RequestBody subcategory_id,
                @Part("product_name") @Nullable RequestBody product_name,
                @Part("brand") @Nullable RequestBody brand,
                @Part("price") @Nullable RequestBody price,
                @Part("description") @Nullable RequestBody description,
                @Part("user_id") @Nullable RequestBody user_id,
                @Part @Nullable MultipartBody.Part file);
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

            body = MultipartBody.Part.createFormData("product_image", profile_image_name, requestFile);
        }


        // RequestBody user_role_id = RequestBody.create(MediaType.parse("text/plain"), Constants.Reg_id(this));
        RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityMyProductDetails.this, Constants.PRODUCT_ID));
       String product= Shared_Preferences.getPrefs(ActivityMyProductDetails.this, Constants.PRODUCT_ID);
        Log.d("ids", "uploadFile: "+product);
        RequestBody subCategory_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityMyProductDetails.this, Constants.SUB_ID));
        RequestBody productName = RequestBody.create(MediaType.parse("text/plain"),edt_productName.getText().toString());
        RequestBody brand1 = RequestBody.create(MediaType.parse("text/plain"), edt_brand.getText().toString());
        RequestBody price1 = RequestBody.create(MediaType.parse("text/plain"), edt_price.getText().toString().trim());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), edt_description.getText().toString().trim());
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.REG_ID));

//        RequestBody api_token = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(RegisterActivity.this, Constants.API_TOKEN));
        //RequestBody user_last_name = RequestBody.create(MediaType.parse("text/plain"), et_update_profile_lastname.getText().toString());

        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);

        call = service.upload(product_id,subCategory_id, productName, brand1, price1, desc,user_id, body);


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
                        Toast.makeText(ActivityMyProductDetails.this, reason, Toast.LENGTH_SHORT).show();
                        clearAllValues();
                        onBackPressed();
                    } else
                        Toast.makeText(ActivityMyProductDetails.this, "" + reason, Toast.LENGTH_SHORT).show();
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

    private void clearAllValues() {
        edt_productName.setText("");
        edt_description.setText("");
        edt_price.setText("");
        edt_brand.setText("");
    }

    private boolean isValid() {
        boolean res=true;
        if(!MyValidator.isValidSpinner(spinner_category))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_productName))
        {
            res=false;
        }
        if (!MyValidator.isValidField(edt_brand))
        {
            res=false;
        }
        if (!MyValidator.isValidField(edt_price))
        {
            res=false;
        }
        if(!MyValidator.isValidField(edt_description))
        {
            res=false;
        }
        return res;
    }

    private void setData() {
        edt_productName.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.PRODUCT_NAME));
        edt_brand.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.BRAND));
        edt_price.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.PRICE));
        edt_description.setText(Shared_Preferences.getPrefs(ActivityMyProductDetails.this,Constants.DESCRIPTION));

    }

    private void getData() {
        AddMyProduct.GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(AddMyProduct.GetOrderApi.class);
        String reg_id=Shared_Preferences.getPrefs(this, Constants.REG_ID);
        String category_id=   Shared_Preferences.getPrefs(this,Constants.CATEGORY_ID);
        Log.d("ids", "getData: "+reg_id+category_id);
        Call<ResponseBody> responseBodyCall=getOrderApi.getCategory(reg_id,
                category_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output=response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if (result)
                        {
                            stringList.add(new SubCategory("Select Sub Categories"));

                            JSONArray jsonArray=jsonObject.getJSONArray("subCategories");
                            for(int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject object=jsonArray.getJSONObject(i);
                                // stringList.add(object.getString("category_id"));

                                stringList.add(new SubCategory(object));


                            }


                          //  spinner_category.setPrompt("Select SubCategory");
                            spinnerArrayAdapter = new ArrayAdapter(ActivityMyProductDetails.this, android.R.layout.simple_spinner_dropdown_item, stringList);
                            spinner_category.setAdapter(spinnerArrayAdapter);
                        }
                        progressDialog.dismiss();




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_upload_image) {
            if (ActivityMyProductDetails.super.requestAppPermissions(this))
                camera.selectImage(civ_event_image_upload, 0);
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


}
