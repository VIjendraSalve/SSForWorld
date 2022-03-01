package com.windhans.client.forworld.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.windhans.client.forworld.Adapter.AdapterImages;
import com.windhans.client.forworld.Adapter.ServiceUpdateImagesAdapter;
import com.windhans.client.forworld.Model.ImagePOJO;
import com.windhans.client.forworld.Model.MyServiceModel;
import com.windhans.client.forworld.Model.Service_Images;
import com.windhans.client.forworld.Model.Services;
import com.windhans.client.forworld.Model.SubService;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Camera;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
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

import static com.windhans.client.forworld.my_library.MyConfig.createRequestBody;
import static com.windhans.client.forworld.my_library.MyConfig.prepareFilePart;


public class ActivityUpdateService extends UtilityRuntimePermission implements View.OnClickListener, ServiceUpdateImagesAdapter.RecyclerViewItemInterface,Camera.AsyncResponse {


    TextInputEditText edt_serviceName,edt_serviceDescription;
    List<Services> servicesList = new ArrayList<>();
    private ArrayList<SubService> subServiceArrayList = new ArrayList<>();
    ArrayList<MyServiceModel> serviceDetailModelArrayList = new ArrayList<>();
    ArrayAdapter spinnerArrayAdapter;
    Button btn_addProduct;
    String category_id,sub_categoryid;
    private CircleImageView civ_event_image_upload;
    ProgressDialog progressDialog;
    Button ll_upload_image;
    private Camera camera;
    private RecyclerView rv_product_image,recycler_view_images;
    Button iv_add_product_images;
    private ImageView imageView = null;
    private int img_no=0;
    private boolean flag;
    private boolean isUpdate = false;
    private String profile_image_name = "", profile_image_path = "";
    private String profile_image_name1 = "", profile_image_path1 = "";
    ArrayList<Service_Images> service_imagesArrayList=new ArrayList<>();
    ServiceUpdateImagesAdapter multipleServiceImagesAdapter;
    private int position1;
    private AdapterImages adapterImages;
    private ArrayList<ImagePOJO> imagePOJOArrayList = new ArrayList<>();
    private int update_position = 0;
    private String serviceID = "", subserviceID="";
    private Spinner business_services_spinner, business_services_subspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressDialog = new ProgressDialog(ActivityUpdateService.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        camera = new Camera(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText(this.getResources().getString(R.string.update));

        civ_event_image_upload = findViewById(R.id.civ_event_image_upload);
        ll_upload_image = (Button) findViewById(R.id.ll_upload_image);
        business_services_spinner = findViewById(R.id.business_services_spinner);
        business_services_subspinner = findViewById(R.id.business_services_subspinner);
        business_services_spinner.setEnabled(false);
        edt_serviceName=findViewById(R.id.edt_serviceName);
        edt_serviceDescription=findViewById(R.id.edt_serviceDescription);
        btn_addProduct=findViewById(R.id.btn_addProduct);

        ll_upload_image.setOnClickListener(this);
        serviceDetailModelArrayList =ActivityUpdateService.this.getIntent().getParcelableArrayListExtra(Constants.Service_LIst);
        Log.d("size", "onCreate: "+serviceDetailModelArrayList.size());
        position1 = this.getIntent().getIntExtra(Constants.Position, 0);
        //title.setText(serviceDetailModelArrayList.get(position1).getName());
        edt_serviceName.setText(serviceDetailModelArrayList.get(position1).getName());
        edt_serviceDescription.setText(serviceDetailModelArrayList.get(position1).getDescription());
      //  edt_category.setText(serviceDetailModelArrayList.get(position1).getCategory_name());
       // tv_description.setText(serviceDetailModelArrayList.get(position1).getDescription());
        String image =serviceDetailModelArrayList.get(position1).getBanner_image();
        if (image != null && !image.isEmpty() && !image.equals("null")) {
            Glide.with(ActivityUpdateService.this)
                    .load(MyConfig.JSON_ServicePath + image)
                    .into(civ_event_image_upload);

        } else {
            Glide.with(ActivityUpdateService.this)
                    .load(R.drawable.no_image)
                    .into(civ_event_image_upload);
        }
        rv_product_image=findViewById(R.id.rv_product_image);
        iv_add_product_images=findViewById(R.id.iv_add_product_images);
        iv_add_product_images.setOnClickListener(this);
        recycler_view_images=findViewById(R.id.recycler_view_images);
        service_imagesArrayList=serviceDetailModelArrayList.get(position1).getService_imagesArrayList();
        Log.d("sizeImages", "onCreate: "+service_imagesArrayList.size());
        recycler_view_images=findViewById(R.id.recycler_view_images);
        multipleServiceImagesAdapter = new ServiceUpdateImagesAdapter(ActivityUpdateService.this, service_imagesArrayList, this);
        // GridLayoutManager mLayoutManager2 = new GridLayoutManager(this,2);
        recycler_view_images.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view_images.setItemAnimator(new DefaultItemAnimator());
        recycler_view_images.setAdapter(multipleServiceImagesAdapter);
        multipleServiceImagesAdapter.notifyDataSetChanged();
        setUpRecyclerView();
        getServicesList();
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  addService();
                if (profile_image_path == null) {
                    Toast.makeText(ActivityUpdateService.this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(profile_image_path, profile_image_name);
                    }
                    //onBackPressed();

            }


        });
    }

    private void getServicesList() {
        progressDialog.show();
        servicesList.clear();
        ActivityAddServices.FileUploadService api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(ActivityAddServices.FileUploadService.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) api.getServices(
                "");

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String output = "";
                try {
                    progressDialog.dismiss();
                    output = response.body().string();
                    Log.d("org", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    if (res) {
                        // remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        JSONArray jsonArray = jsonObject.getJSONArray("categorylist");
                        servicesList.add(new Services("-1","Select Service Name",null));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                servicesList.add(new Services(obj));

                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                        Log.d("list", "onResponse: " + servicesList.size());
                        ArrayAdapter<Services> dataAdapter = new ArrayAdapter<Services>(ActivityUpdateService.this, android.R.layout.simple_spinner_item, servicesList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        business_services_spinner.setAdapter(dataAdapter);

                        String category_name=Shared_Preferences.getPrefs(ActivityUpdateService.this,"CategoryIDForSend");
                        for(int j=0;j<servicesList.size();j++)
                        {
                            String category_name1=servicesList.get(j).getCategory_id();
                            if(category_name.equals(category_name1))
                            {
                                business_services_spinner.setSelection(j);
                            }
                        }

                       // getSubserviceList(Shared_Preferences.getPrefs(ActivityUpdateService.this,"SubCategoryIDForSend"));

                        business_services_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if(!servicesList.get(i).getCategory_id().equals("-1")) {
                                    business_services_subspinner.setVisibility(View.VISIBLE);
                                    serviceID = servicesList.get(i).getCategory_id();
                                    getSubserviceList(serviceID);
                                }

                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });

                        progressDialog.dismiss();

                    } else {
                        //noRecordLayout.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getSubserviceList(String serviceID) {
        progressDialog.show();
        subServiceArrayList.clear();

        Log.d("Valude", "getSubserviceList: "+serviceID);
        ActivityAddServices.FileUploadService getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(ActivityAddServices.FileUploadService.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.getSubServices(
                Shared_Preferences.getPrefs(this, Constants.REG_ID),
                serviceID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    Log.d("SubcategoryList", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            JSONArray jsonArray = jsonObject.getJSONArray("subCategories");
                            subServiceArrayList.add(new SubService("-1","Select Subcategory",null));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                subServiceArrayList.add(new SubService(object));
                            }
                        }


                        ArrayAdapter<SubService> dataAdapter = new ArrayAdapter<SubService>(ActivityUpdateService.this, android.R.layout.simple_spinner_item, subServiceArrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        business_services_subspinner.setAdapter(dataAdapter);

                        String category_name=Shared_Preferences.getPrefs(ActivityUpdateService.this,"SubCategoryIDForSend");
                        Log.d("category_name", "onResponse: "+category_name);
                        for(int j=0;j<subServiceArrayList.size();j++)
                        {
                            if(category_name.equals(subServiceArrayList.get(j).getCategory_id()))
                            {
                                business_services_subspinner.setSelection(j);
                            }
                        }

                        business_services_subspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                //  On selecting a spinner item
                                String item = adapterView.getItemAtPosition(i).toString();
                                subserviceID = subServiceArrayList.get(i).getCategory_id();
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });

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

    private void uploadFile(String profile_image_path, String profile_image_name) {

        FileUploadService service = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);

        MultipartBody.Part body = null;
        if (!profile_image_path.equalsIgnoreCase("")) {

            File file = new File(profile_image_path);


            // String mimeType= URLConnection.guessContentTypeFromName(file.getName());

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name

            body = MultipartBody.Part.createFormData("banner_image", profile_image_name, requestFile);
        }

        List<MultipartBody.Part> Product_Images = new ArrayList<>();
        List<RequestBody> product_images_ids = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < imagePOJOArrayList.size(); i++) {
            if (imagePOJOArrayList.get(i).getImage_path_multipart() != null) {
                Log.d("path", "uploadFile: "+imagePOJOArrayList.get(i).getImg_path_local());
                Log.d("images_ids", "uploadFile: "+imagePOJOArrayList.get(i).getImg_id());
                Product_Images.add(prepareFilePart("multiple_images[" + j + "]", imagePOJOArrayList.get(i).getImg_path_local()));
                product_images_ids.add(createRequestBody(imagePOJOArrayList.get(i).getImg_id()));
                // Product_Images.remove(imagePOJOArrayList.get(0).getImg_id());
                Log.d("images", "uploadFile: "+product_images_ids);
                //    Log.d("images", "uploadFile: "+Product_Images.size());
                j++;
            }
        }
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), serviceID);
        RequestBody subCategory_id = RequestBody.create(MediaType.parse("text/plain"), subserviceID);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityUpdateService.this, Constants.REG_ID));
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), edt_serviceName.getText().toString());
        RequestBody service_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityUpdateService.this,"Service_id"));
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_serviceDescription.getText().toString().trim());
        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);

        call = service.upload(category_id,subCategory_id,user_id,name,service_id,description,Product_Images, body);


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

                    if (res) {

                        Toast.makeText(ActivityUpdateService.this, reason, Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    } else
                        Toast.makeText(ActivityUpdateService.this, reason, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //onBackPressed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Upload_error:", "");
            }
        });
    }

    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/updateService")
        Call<ResponseBody> upload(
                @Part("category_id") @Nullable RequestBody category_id,
                @Part("subcategory_id") @Nullable RequestBody subcategory_id,
                @Part("user_id") @Nullable RequestBody role_id,
                @Part("name") @Nullable RequestBody name,
                @Part("service_id") @Nullable RequestBody service_id,
                @Part("description") @Nullable RequestBody description,
                @Part List<MultipartBody.Part> RoomPartList,
                @Part @Nullable MultipartBody.Part file
        );
    }

    private void setUpRecyclerView() {
        rv_product_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterImages = new AdapterImages(ActivityUpdateService.this, imagePOJOArrayList, 2, 0);
        rv_product_image.setAdapter(adapterImages);
    }

    @Override
    public void processFinish(String result, int img_no) {
        Log.d("Image_path123", result + " " + img_no);
        if (isUpdate) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d("Image_path", result + " " + img_no);
            profile_image_name1 = imagename;
            profile_image_path1 = result;
            imagePOJOArrayList.get(update_position).setImg_name(imagename);
            imagePOJOArrayList.get(update_position).setImage_path_multipart(prepareFilePart("room_image[" + update_position + "]", result));
            imagePOJOArrayList.get(update_position).setImg_path_local(result);
            adapterImages.notifyItemChanged(update_position);
            adapterImages.notifyDataSetChanged();

        } else if (img_no == 1){
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d("Image_path", result + " " + img_no);
            profile_image_name1 = imagename;
            profile_image_path1 = result;
            imagePOJOArrayList.add(new ImagePOJO("0", "" + imagename, prepareFilePart("room_image", result), result));
            adapterImages.notifyDataSetChanged();
        }
        else {
            String[] parts1 = result.split("/");
            String imagename1 = parts1[parts1.length - 1];
            Log.d("Image_path", result + " " + img_no);
            profile_image_name = imagename1;
            profile_image_path = result;
        }
    }


    private void addService() {
        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        Call<ResponseBody> responseBodyCall = getOrderApi.addService(
                Shared_Preferences.getPrefs(ActivityUpdateService.this, "Category_id"),
                Shared_Preferences.getPrefs(ActivityUpdateService.this, Constants.SUB_ID),
                Shared_Preferences.getPrefs(ActivityUpdateService.this, Constants.REG_ID),
                edt_serviceName.getText().toString(),
                Shared_Preferences.getPrefs(ActivityUpdateService.this,"Service_id")

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");
                    String msg = jsonObject.getString("reason");
                    if (res) {
                        Toast.makeText(ActivityUpdateService.this, msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(ActivityUpdateService.this, msg, Toast.LENGTH_SHORT).show();
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
        imageView = null;
        img_no = 0;
        if (view.getId() == R.id.ll_upload_image) {
            if (ActivityUpdateService.super.requestAppPermissions(this))
                camera.selectImage(civ_event_image_upload, 0);
        }

        if(view.getId()==R.id.iv_add_product_images)
        {
            imageView = new ImageView(ActivityUpdateService.this);
            img_no = 1;
        }
        if (imageView != null && ActivityUpdateService.super.requestAppPermissions(ActivityUpdateService.this)) {
            camera.selectImage(imageView, img_no);
        } else
            flag = true;
    }

    @Override
    public void onItemClick(int position, String path) {
       // service_imagesArrayList.remove(position);

        for(int i=0;i<service_imagesArrayList.size();i++)
        {  String id=service_imagesArrayList.get(i).getImage_id();
            if(id.contains(path))
            {
                service_imagesArrayList.remove(i);
                Toast.makeText(ActivityUpdateService.this, "deleted", Toast.LENGTH_SHORT).show();
                multipleServiceImagesAdapter.notifyDataSetChanged();
            }
        }
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/updateService")
        Call<ResponseBody> addService(
                @Field("category_id") String category_id,
                @Field("subcategory_id") String subcategory_id,
                @Field("role_id") String user_id,
                @Field("name") String name,
                @Field("service_id") String service_id

        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getSubCategoryListing")
        Call<ResponseBody> getCategory(
                @Field("user_id") String user_id,
                @Field("category_id") String category_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/vendorCategoryList")
        Call<ResponseBody> getMainCategory(
                @Field("role_id") String user_id
        );
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
        return super.onOptionsItemSelected(item);
    }
}
