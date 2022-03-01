package com.windhans.client.forworld.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.windhans.client.forworld.Adapter.AdapterServicesImages;
import com.windhans.client.forworld.Model.ServiceImagePOJO;
import com.windhans.client.forworld.Model.Services;
import com.windhans.client.forworld.Model.SubService;
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

import static com.windhans.client.forworld.my_library.MyConfig.createRequestBody;
import static com.windhans.client.forworld.my_library.MyConfig.prepareFilePart;

public class ActivityAddServices extends UtilityRuntimePermission implements View.OnClickListener, Camera.AsyncResponse, AdapterServicesImages.ImageUpdate {

    //Spinner spinner_main_category,spinner_category;
    TextInputEditText edt_serviceName, edt_serviceDescription;
    ProgressDialog progressDialog;
    List<Services> servicesList = new ArrayList<>();
    private ArrayList<SubService> subServiceArrayList = new ArrayList<>();
    ArrayAdapter spinnerArrayAdapter;
    Button btn_addProduct;
    Button ll_upload_image;
    private Camera camera;
    private static final int IMG_REQUEST = 1;
    private String profile_image_name = "", profile_image_path = "";
    CircleImageView civ_event_image_upload;
    private RecyclerView rv_service_image;
    Button iv_add_service_images;
    private ImageView imageView = null;
    private int img_no = 0;
    private boolean flag;
    AdapterServicesImages adapterImages;
    private ArrayList<ServiceImagePOJO> imagePOJOArrayList = new ArrayList<>();
    private boolean isUpdate = false;
    private String profile_image_name1 = "", profile_image_path1 = "";
    private int update_position = 0;
    private String serviceID = "", subserviceID="";
    private Spinner business_services_spinner, business_services_subspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(ActivityAddServices.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        //title.setText("Add Services");
        title.setText(this.getResources().getString(R.string.addservice));
        camera = new Camera(this);
        business_services_spinner = findViewById(R.id.business_services_spinner);
        business_services_spinner.setEnabled(false);
        business_services_subspinner = findViewById(R.id.business_services_subspinner);
        edt_serviceName = findViewById(R.id.edt_serviceName);
        btn_addProduct = findViewById(R.id.btn_addProduct);
        civ_event_image_upload = findViewById(R.id.civ_event_image_upload);
        ll_upload_image = (Button) findViewById(R.id.ll_upload_image_service);


        business_services_subspinner.setVisibility(View.GONE);
        edt_serviceDescription = findViewById(R.id.edt_serviceDescription);
        rv_service_image = findViewById(R.id.rv_service_image);
        iv_add_service_images = findViewById(R.id.iv_add_service_images);
        ll_upload_image.setOnClickListener(this);
        iv_add_service_images.setOnClickListener(this);
        setUpRecyclerView();

        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   addService();
                if (profile_image_path == null) {
                    Toast.makeText(ActivityAddServices.this, "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
                } else {
                    if(validates()) {
                        uploadFile(profile_image_path, profile_image_name);
                    }
                    //onBackPressed();
                }
            }
        });

        getServicesList();
    }

    private boolean validates() {
        boolean res = true;
        if (!MyValidator.isValidField(edt_serviceName)) {
            res = false;
        }
        if (!MyValidator.isValidField(edt_serviceDescription)) {
            res = false;
        }

        return res;
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
                Log.d("path", "uploadFile: " + imagePOJOArrayList.get(i).getImg_path_local());
                Log.d("images_ids", "uploadFile: " + imagePOJOArrayList.get(i).getImg_id());
                Product_Images.add(prepareFilePart("multiple_images[" + j + "]", imagePOJOArrayList.get(i).getImg_path_local()));
                product_images_ids.add(createRequestBody(imagePOJOArrayList.get(i).getImg_id()));
                // Product_Images.remove(imagePOJOArrayList.get(0).getImg_id());
                Log.d("images", "uploadFile: " + product_images_ids);
                //    Log.d("images", "uploadFile: "+Product_Images.size());
                j++;
            }
        }
        //   Product_Images.remove(0);


        // RequestBody user_role_id = RequestBody.create(MediaType.parse("text/plain"), Constants.Reg_id(this));
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), serviceID);
        RequestBody subcategory_id = RequestBody.create(MediaType.parse("text/plain"), subserviceID);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), Shared_Preferences.getPrefs(ActivityAddServices.this, Constants.REG_ID));
        RequestBody service_name = RequestBody.create(MediaType.parse("text/plain"), edt_serviceName.getText().toString());
        RequestBody service_description = RequestBody.create(MediaType.parse("text/plain"), edt_serviceDescription.getText().toString());


        Log.d("category_id", "uploadFile: " + Shared_Preferences.getPrefs(ActivityAddServices.this, "Category_id"));
        Log.d("subcategory_id", "uploadFile: " + Shared_Preferences.getPrefs(ActivityAddServices.this, Constants.SUB_ID));
        Log.d("user_id", "uploadFile: " + Shared_Preferences.getPrefs(ActivityAddServices.this, Constants.REG_ID));


        Call<ResponseBody> call;

        //Toast.makeText(this, "In Method", Toast.LENGTH_SHORT).show();
        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"), "" + profile_image_name);

        call = service.upload(category_id, subcategory_id, user_id, service_name, service_description, Product_Images, body);


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
                        ShowDialog();
                    } else
                        Toast.makeText(ActivityAddServices.this, "" + reason, Toast.LENGTH_SHORT).show();
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

    private void ShowDialog() {
        final Dialog dialog = new Dialog(ActivityAddServices.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog1);

        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();

            }
        });

        dialog.show();
        //  onBackPressed();
    }

    private void setUpRecyclerView() {
        rv_service_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterImages = new AdapterServicesImages(ActivityAddServices.this, imagePOJOArrayList, 2, 0);
        rv_service_image.setAdapter(adapterImages);
    }

    @Override
    public void processFinish(String result, int img_no) {
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

        } else if (img_no == 1) {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d("Image_path", result + " " + img_no);
            profile_image_name1 = imagename;
            profile_image_path1 = result;
            imagePOJOArrayList.add(new ServiceImagePOJO("0", "" + imagename, prepareFilePart("room_image", result), result));
            adapterImages.notifyDataSetChanged();
        } else {
            String[] parts = result.split("/");
            String imagename = parts[parts.length - 1];
            Log.d("Image_path", result + " " + img_no);
            profile_image_name = imagename;
            profile_image_path = result;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.myActivityResult(requestCode, resultCode, data);
       /* if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                civ_register_user_profile_picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }



    private void getServicesList() {
        progressDialog.show();
        servicesList.clear();
        FileUploadService api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
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
                        ArrayAdapter<Services> dataAdapter = new ArrayAdapter<Services>(ActivityAddServices.this, android.R.layout.simple_spinner_item, servicesList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        business_services_spinner.setAdapter(dataAdapter);

                        for (int i = 0; i <servicesList.size() ; i++) {
                            if(servicesList.get(i).getCategory_id().equals(Shared_Preferences.getPrefs(ActivityAddServices.this,Constants.parent_categoryID)))
                                business_services_spinner.setSelection(i);
                        }

                        serviceID = Shared_Preferences.getPrefs(ActivityAddServices.this,Constants.parent_categoryID);

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
        FileUploadService getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(FileUploadService.class);
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


                        ArrayAdapter<SubService> dataAdapter = new ArrayAdapter<SubService>(ActivityAddServices.this, android.R.layout.simple_spinner_item, subServiceArrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        business_services_subspinner.setAdapter(dataAdapter);
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

    @Override
    public void onClick(View view) {
        imageView = null;
        img_no = 0;
        if (view.getId() == R.id.ll_upload_image_service) {
            if (ActivityAddServices.super.requestAppPermissions(this))
                camera.selectImage(civ_event_image_upload, 0);
        }
        if (view.getId() == R.id.iv_add_service_images) {
            imageView = new ImageView(ActivityAddServices.this);
            img_no = 1;
        }
        if (imageView != null && ActivityAddServices.super.requestAppPermissions(ActivityAddServices.this)) {
            camera.selectImage(imageView, img_no);
        } else
            flag = true;
    }

    @Override
    public void onUpdateImage(int IMG_TYPE, int position, ImageView imageView) {

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

    public interface FileUploadService {
        @Multipart
        @POST(MyConfig.SSWORLD + "/addService")
        Call<ResponseBody> upload(

                @Part("category_id") @Nullable RequestBody brand,
                @Part("subcategory_id") @Nullable RequestBody product_name,
                @Part("user_id") @Nullable RequestBody category,
                @Part("name") @Nullable RequestBody subcategory_id,
                @Part("description") @Nullable RequestBody description,
                @Part List<MultipartBody.Part> RoomPartList,
                @Part @Nullable MultipartBody.Part file
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getServiceCategoryListing")
        Call<ResponseBody> getServices(
                @Field("user_id") String user_id
        );

        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getServiceSubCategoryListing")
        Call<ResponseBody> getSubServices(
                @Field("user_id") String user_id,
                @Field("category_id") String category_id
        );
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
