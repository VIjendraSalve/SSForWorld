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
import java.util.List;

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

public class AddMyProduct extends UtilityRuntimePermission implements View.OnClickListener,Camera.AsyncResponse{

    TextInputEditText edt_productName,edt_brand,edt_price;
    EditText edt_description;
    Button btn_addProduct;
    Spinner spinner_category;
    String productName,brand,price,description;
    private String product_image="";
    private ProgressDialog progressDialog;
    List<SubCategory> subCategoryList;
    private ImageView imageView;
    ArrayAdapter spinnerArrayAdapter;
    private CircleImageView civ_event_image_upload;
     Button ll_upload_image;
    private Camera camera;
    private static final int IMG_REQUEST = 1;
    private String profile_image_name = "", profile_image_path = "";
    Button btn_addOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


       /* imageView=findViewById(R.id.iv_logo);
        imageView.setVisibility(View.GONE);*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);

        title.setText(this.getResources().getString(R.string.addproduct));
        camera = new Camera(this);
        progressDialog=new ProgressDialog(AddMyProduct.this);
        getViews();

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

    private void getViews() {
        edt_productName=findViewById(R.id.edt_productName);
        edt_brand=findViewById(R.id.edt_brand);
        edt_price=findViewById(R.id.edt_price);
        edt_description=findViewById(R.id.edt_description);

        spinner_category=findViewById(R.id.spinner_category);
        civ_event_image_upload=findViewById(R.id.civ_event_image_upload);
        ll_upload_image = (Button) findViewById(R.id.ll_upload_image);

        ll_upload_image.setOnClickListener(this);
        subCategoryList=new ArrayList<>();
        getData();

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                Toast.makeText(AddMyProduct.this, subCategoryList.get(position).getId(), Toast.LENGTH_SHORT).show();
                Shared_Preferences.setPrefs(AddMyProduct.this,Constants.SUB_ID,subCategoryList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_addProduct=findViewById(R.id.btn_addProduct);

        btn_addProduct.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(AddMyProduct.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(AddMyProduct.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        //progressDialog.setCancelable(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        uploadFile(profile_image_path, profile_image_name);
                    }
                } else {
                    Toast.makeText(AddMyProduct.this, "Please Fill Complete Data", Toast.LENGTH_SHORT).show();
                }

            }
        });

      /*  btn_addOffer=findViewById(R.id.btn_addOffer);
        btn_addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/addProduct")
        Call<ResponseBody> upload(
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
        RequestBody subCategory_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(AddMyProduct.this, Constants.SUB_ID));
        Log.d("sub", "uploadFile: "+Shared_Preferences.getPrefs(AddMyProduct.this, Constants.SUB_ID));
        Log.d("sub", "uploadFile: "+edt_productName.getText().toString());
        Log.d("sub", "uploadFile: "+edt_brand.getText().toString());
        Log.d("sub", "uploadFile: "+ edt_price.getText().toString());
        Log.d("sub", "uploadFile: "+  edt_description.getText().toString().trim());
        Log.d("sub", "uploadFile: "+ Shared_Preferences.getPrefs(AddMyProduct.this,Constants.REG_ID));
        RequestBody productName = RequestBody.create(MediaType.parse("text/plain"),edt_productName.getText().toString());
        RequestBody brand1 = RequestBody.create(MediaType.parse("text/plain"), edt_brand.getText().toString());
        RequestBody price1 = RequestBody.create(MediaType.parse("text/plain"), edt_price.getText().toString().trim());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), edt_description.getText().toString().trim());
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(AddMyProduct.this,Constants.REG_ID));

//        RequestBody api_token = RequestBody.create(MediaType.parse("text/plain"),Shared_Preferences.getPrefs(RegisterActivity.this, Constants.API_TOKEN));
        //RequestBody user_last_name = RequestBody.create(MediaType.parse("text/plain"), et_update_profile_lastname.getText().toString());

        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);
        call = service.upload(subCategory_id, productName, brand1, price1, desc,user_id, body);


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
                        Toast.makeText(AddMyProduct.this, reason, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else
                        Toast.makeText(AddMyProduct.this, "" + reason, Toast.LENGTH_SHORT).show();
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

    private void addProduct(String productName, String brand, String price, String description) {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall=getOrderApi.addProduct(
                Shared_Preferences.getPrefs(AddMyProduct.this, Constants.SUBCATEGORY),
                productName,
                brand,
                price,
                description,
                Shared_Preferences.getPrefs(AddMyProduct.this,Constants.REG_ID),
                product_image
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output="";
                try {
                    output=response.body().string();
                    JSONObject jsonObject=new JSONObject(output);
                    boolean res=jsonObject.getBoolean("result");
                    String msg=jsonObject.getString("reason");
                    if(res)
                    {
                        Toast.makeText(AddMyProduct.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddMyProduct.this, msg, Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        if (view.getId() == R.id.ll_upload_image) {
            if (AddMyProduct.super.requestAppPermissions(this))
                camera.selectImage(civ_event_image_upload, 0);
        }
    }

    public interface GetOrderApi
    {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/addProduct")
        Call<ResponseBody> addProduct(
                @Field("subcategory_id") String subcategory_id,
                @Field("product_name") String product_name,
                @Field("brand") String brand,
                @Field("price") String price,
                @Field("description") String description,
                @Field("user_id") String user_id,
                @Field("product_image") String product_image
        );
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD+"/getSubCategoryListing")
        Call<ResponseBody> getCategory(
                @Field("user_id") String user_id,
                @Field("category_id") String category_id
        );
    }

    private void getData() {
        GetOrderApi getOrderApi= MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String reg_id=Shared_Preferences.getPrefs(this, Constants.REG_ID);
        String category_id=   Shared_Preferences.getPrefs(this,Constants.CATEGORY_ID);
        Call<ResponseBody> responseBodyCall=getOrderApi.getCategory(reg_id,
                "1");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output=response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(output);
                        boolean result=jsonObject.getBoolean("result");
                        if (result)
                        {subCategoryList.add(new SubCategory("Select Sub Category"));
                            JSONArray jsonArray=jsonObject.getJSONArray("subCategories");
                            for(int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject object=jsonArray.getJSONObject(i);
                               // subCategoryList.add(object.getString("category_id"));

                                subCategoryList.add(new SubCategory(object));


                            }
                            spinnerArrayAdapter = new ArrayAdapter(AddMyProduct.this, android.R.layout.simple_spinner_dropdown_item, subCategoryList);
                            spinner_category.setPrompt("Select SubCategory");
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
