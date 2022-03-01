package com.windhans.client.forworld.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.windhans.client.forworld.Activities.ActivityLead;
import com.windhans.client.forworld.Activities.ActivityMyProduct;
import com.windhans.client.forworld.Activities.ActivityMyService;
import com.windhans.client.forworld.Activities.ActivityOfferListing;
import com.windhans.client.forworld.Activities.ActivityOrderListing;
import com.windhans.client.forworld.Activities.Activity_My_Balance;
import com.windhans.client.forworld.Activities.Activity_Share;
import com.windhans.client.forworld.Activities.Activity_share_business;
import com.windhans.client.forworld.Activities.BuisnessHome;
import com.windhans.client.forworld.Activities.ProfileActivity;
import com.windhans.client.forworld.Adapter.OfferAdapter;
import com.windhans.client.forworld.Model.LeadDetailsNew;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Bold;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Medium;
import com.windhans.client.forworld.customfonts.MyTextView_Poppins_Regular;
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


public class HomeFragment1 extends Fragment {
    ImageView imageView_business, imageView_product, imageView_lead;
    TextView txt_lead, tv_name, txt_buinessList;
    private View retView;
    RecyclerView recycler_view;
    OfferAdapter offerAdapter;
    List<String> offerarrayList = new ArrayList<>();
    List<Integer> stringImageList = new ArrayList<>();
    LinearLayout cardView_productList, cardView_lead, cardView_businessList, card_view_earning, card_view_completed_order, cardView_digital_card;
    private ArrayList<String> sliderImages1 = new ArrayList<>();
    private int index;
    private MyTextView_Poppins_Regular tv_welcome_user_name;
    LinearLayout ll_offer;
    TextView tv_offer;
    MyTextView_Poppins_Bold tv_lead_count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        retView = inflater.inflate(R.layout.dashboard_layout, container, false);
        // retView = inflater.inflate(R.layout.fragment_home1, container, false);
        imageView_business = retView.findViewById(R.id.imageView_business);
        imageView_product = retView.findViewById(R.id.imageView_product);
        imageView_lead = retView.findViewById(R.id.imageView_lead);
        txt_lead = retView.findViewById(R.id.txt_lead);
        txt_buinessList = retView.findViewById(R.id.txt_buinessList);
        recycler_view = retView.findViewById(R.id.recycler_view);
        cardView_productList = retView.findViewById(R.id.cardView_productList);
        cardView_lead = retView.findViewById(R.id.cardView_lead);
        cardView_businessList = retView.findViewById(R.id.cardView_businessList);
        card_view_earning = retView.findViewById(R.id.card_view_earning);
        card_view_completed_order = retView.findViewById(R.id.card_view_completed_order);
        cardView_digital_card = retView.findViewById(R.id.cardView_digital_card);
        tv_welcome_user_name = retView.findViewById(R.id.tv_welcome_user_name);
        ll_offer = retView.findViewById(R.id.ll_offer);
        tv_offer = retView.findViewById(R.id.tv_offer);
        tv_lead_count = retView.findViewById(R.id.tv_lead_count);
        tv_welcome_user_name.setText("Welcome, "+Shared_Preferences.getPrefs(getContext(), Constants.REG_NAME));

        getLeadCount();
        String user_type = Shared_Preferences.getPrefs(getContext(), Constants.REG_TYPE);
        if (user_type.equals("1")) {
            txt_lead.setText(getResources().getString(R.string.enquiry));
        } else {
            txt_lead.setText(getResources().getString(R.string.lead));
        }
      /*  tv_name=retView.findViewById(R.id.tv_name);
        tv_name.setText(Shared_Preferences.getPrefs(getContext(),Constants.REG_NAME));*/

        cardView_productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Shared_Preferences.getPrefs(getContext(), Constants.category_type).equals("1")) {
                    Intent intent = new Intent(getContext(), ActivityOfferListing.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        card_view_completed_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityOrderListing.class);
                startActivity(intent);
            }
        });

        cardView_digital_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_share_business.class);
                startActivity(intent);
            }
        });

        card_view_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_My_Balance.class);
                startActivity(intent);
            }
        });

        cardView_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(getContext(), ActivityAddProductNewFlow.class);
                startActivity(intent);*/
                Intent intent = new Intent(getContext(), ActivityLead.class);
                startActivity(intent);
            }
        });

        if (user_type.equals("1")) {
            txt_buinessList.setText("Buisness List");
            cardView_businessList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*       Intent intent=new Intent(getContext(), Activity_Buisness_Category.class);
                    startActivity(intent);*/
                  /*  Intent intent=new Intent(getContext(), ActivityBuisnessListing.class);
                    startActivity(intent);*/
                }
            });
        } else {

            cardView_businessList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dexter.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {
                                        // do you work now
                                        if(Shared_Preferences.getPrefs(getContext(), Constants.category_type).equals("1")) {
                                            Intent intent = new Intent(getContext(), ActivityMyProduct.class);
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(getContext(), ActivityMyService.class);
                                            startActivity(intent);
                                        }
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        // permission is denied permenantly, navigate user to app settings
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .onSameThread()
                            .check();
                }
            });
        }



        if(Shared_Preferences.getPrefs(getContext(), Constants.category_type) != null) {
            if (Shared_Preferences.getPrefs(getContext(), Constants.category_type).equals("1")) {
                txt_buinessList.setText(getResources().getString(R.string.myproduct));
                tv_offer.setText(getResources().getString(R.string.offers));

            } else {
                txt_buinessList.setText(getResources().getString(R.string.myservice));
                tv_offer.setText(getResources().getString(R.string.myprofile));
            }
        }else {
            txt_buinessList.setText(getResources().getString(R.string.myproduct));
            tv_offer.setText(getResources().getString(R.string.offers));
        }




       /* offerarrayList.add("hdhhs");
        offerarrayList.add("hdhhs");
        offerarrayList.add("hdhhs");
        offerarrayList.add("hdhhs");
        offerarrayList.add("hdhhs");
        stringImageList.add(R.drawable.no_image_available);
*/
     /*   offerAdapter = new OfferAdapter(getContext(),offerarrayList,stringImageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(offerAdapter);*/
        prepareSlider();
        return retView;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void prepareSlider() {

        sliderImages1.add("http://godapark.com/slider/for1.jpg");
        sliderImages1.add("http://godapark.com/slider/for2.jpg");
        sliderImages1.add("http://godapark.com/slider/for1.jpg");

       /* sliderImages.put("Trackon", R.drawable.slider2);
        sliderImages.put("Trackon", R.drawable.slider1);*/


        SliderLayout imgSliderLayout = (SliderLayout) retView.findViewById(R.id.imgSliderLayout);
        //ArrayList<String> img_maps = new ArrayList<>();
        Log.d("my_tag", "prepareSlider: " + sliderImages1);
 /*
        for (String name : sliderImages.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            imgSliderLayout.addSlider(textSliderView);
        }*/


        for (int i = 0; i < sliderImages1.size(); i++) {
            index++;
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            // Log.d("image", sliderImages.get(i));
            defaultSliderView
                    //.description(name)
                    .image(sliderImages1.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString("extra", sliderImages1.get(i));

            imgSliderLayout.addSlider(defaultSliderView);
        }

         /*   defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    Log.d("click", "click");
                    Intent intent = new Intent(ActivityViewRoomDetails.this, ImageViewZoom.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.ROOM_IMAGES, addpost);
                    bundle.putInt("position", index);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }*/


        imgSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        imgSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgSliderLayout.setCustomAnimation(new DescriptionAnimation());
        imgSliderLayout.setDuration(3000);

     /*   imgSliderLayout.stopAutoCycle();

        for (int i = 0; i < sliderImages1.size(); i++) {

            final TextSliderView sliderView = new TextSliderView(DashboardActivity.this);
            sliderView.description("");
            sliderView.image(sliderImages1.get(i));

            sliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            imgSliderLayout.addSlider(sliderView);

        }

        imgSliderLayout.setCurrentPosition(0, true);

        // play from first image, when all images loaded
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgSliderLayout.startAutoCycle();
            }
        }, 5000);*/
    }

    public interface GetOrderApi {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/getLeadCountByVendor")
        Call<ResponseBody> getLeadData(
                @Field("user_id") String user_id
        );

    }

    private void getLeadCount() {

        GetOrderApi getOrderApi = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(GetOrderApi.class);
        String user_id = Shared_Preferences.getPrefs(getContext(), Constants.REG_ID);
        Call<ResponseBody> responseBodyCall = getOrderApi.getLeadData(user_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("VijendraLeadData", "onResponse: " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            Log.d("leadCount", "onResponse: "+jsonObject.getString("leadCount"));
                            tv_lead_count.setText(""+jsonObject.getString("leadCount"));
                        }else{

                        }
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
}