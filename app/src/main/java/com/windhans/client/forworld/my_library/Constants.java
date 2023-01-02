package com.windhans.client.forworld.my_library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.windhans.client.forworld.MainActivity;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.StartActivities.LoginActivity;

import java.util.Locale;

public class Constants {

    public static final String rs = "\u20B9 ";
    public static final String REG_ID = "reg_id";
    public static final String REG_EMAIL = "reg_email";
    public static final String REG_MOBILE = "reg_mobile";
    public static final String REG_NAME = "reg_fname";
    public static final String IMAGE = "image";
    public static final String REG_LNAME = "lastName";
    public static final String CATEGORY_ID = "category_id";
    public static final String BUSINESS_ID = "BuisnessId";
    public static final String SUBCATEGORY = "subcategory";
    public static final String REG_TYPE = "reg_type";
    public static final String REG_PROFILE_IMAGE = "REG_PROFILE_IMAGE";
    public static final String PRODUCT_ID = "product_id";
    public static final String BUSINESS_NAME = "business_name";
    public static final String BUSINESS_DEC = "business_dec";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NO = "contact_no";
    public static final String SUB_ID = "SUB_ID";
    public static final String PRODUCT_IMAGE = "product_image";
    public static final String PRODUCT_NAME = "product_name";
    public static final String BRAND = "brand";
    public static final String DESCRIPTION = "description";
    public static final String REFERENCE_NUMBER = "reference_number";
    public static final String SELF_REFERENCE_NUMBER = "self_reference_number";
    public static final String IS_VERIFIED = "IS_VERIFIED";
    public static final String IS_PRIME = "IS_PRIME";
    public static final String category_type = "category_type";
    public static final String subcategoryID = "subcategoryID";
    public static final String parent_categoryID = "parent_categoryID";
    public static final String VendorAddress = "VendorAddress";
    public static final String PRICE = "price";
    public static final String LAND_MARK = "LAND_MARK";
    public static final String DISTRICT = "DISTRICT";
    public static final String DISTRICT_NAME = "DISTRICT_NAME";
    public static final String STATE_NAME = "STATE_NAME";
    public static final String BARCODE = "BARCODE";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String Flag = "Flag";
    public static final String NOTIFICATION_TOKEN = "NOTIFICATION_TOKEN";

    public static final String BUSINESS_NAME1 = "business_name";
    public static final String EMAIL_ID = "email_id";
    public static final String ADDRESS = "address";
    public static final String IMAGE1 = "address";
    public static final String NEWADDRESS = "address";
    public static final String PURPOSE = "purpose";
    public static final String CITY = "city";
    public static final String STATUS = "status";
    public static final String PRODUCT_DEC = "dec";
    public static final String LEAD_ID = "leadID";
    public static final String LEAD_USER_ID = "LEAD_USER_ID";
    public static final String BUISNESS_BANNER = "BUISNESS_BANNER";
    public static final String STATE = "STATE";
    public static final String SELLER_NAME = "SELLER_NAME";
    public static final String SHOP_NAME = "SHOP_NAME";
    public static final String BANK_NAME = "BANK_NAME";
    public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
    public static final String IFSC = "IFSC";
    public static final String MEMBER_NAME = "MEMBER_NAME";


    public static final String OfferID = "OfferID";
    public static final String OFFER_NAME = "OFFER_NAME";
    public static final String OFFER_TYPE = "OFFER_TYPE";
    public static final String OFFER_PRICE = "OFFER_PRICE";
    public static final String OFFER_DETAILS = "OFFER_DETAILS";
    public static final String OFFER_VALIDITY = "OFFER_VALIDITY";
    public static final String OFFER_IAMGE = "OFFER_IAMGE";
    public static final String OFFER_CODE = "OFFER_CODE";
    public static final String OFFER_TERM = "OFFER_TERM";
    public static final String OFFER_LIMIT = "OFFER_LIMIT";
    public static final String IS_FAVORITE = "IS_FAVORITE";

    public static final String PINCODE = "PINCODE";
    public static final String BUSINESS_LIECNCE_NO = "BUSINESS_LIECNCE_NO";
    public static final String LIECNCE_NO = "LIECNCE_NO";
    public static final String EXPIRY_DATE = "EXPIRY_DATE";
    public static final String GST_EXEMPTION = "GST_EXEMPTION";
    public static final String GST_NUMBER = "GST_NUMBER";
    public static final String AADHAR_NO = "AADHAR_NO";
    public static final String PAN_NO = "PAN_NO";
    public static final String SHOP_AGE = "SHOP_AGE";
    public static final String DOB = "DOB";
    public static final String GENDER = "GENDER";
    public static final String MOBILE2 = "MOBILE2";
    public static final String GooglePay = "GooglePay";
    public static final String PhonePay = "PhonePay";
    public static final String Paytm = "Paytm";
    public static final String BUSINESS_LIECNCE_TYPE = "BUSINESS_LIECNCE_TYPE";

    public static final String CAMPAIGN_TITLE = "CAMPAIGN_TITLE";
    public static final String CAMPAIGN_DECRIPTION = "CAMPAIGN_DECRIPTION";
    public static final String CAMPAIGN_FROM_DATE = "CAMPAIGN_FROM_DATE";
    public static final String CAMPAIGN_TO_DATE = "CAMPAIGN_TO_DATE";
    public static final String IS_ENABLE_ENQUIRY = "IS_ENABLE_ENQUIRY";
    public static final String IS_LOCATION_BASED_ENQUIRY = "IS_LOCATION_BASED_ENQUIRY";
    public static final String CAMPAIGN_BANNER = "CAMPAIGN_BANNER";
    public static final String CAMPAIGN_ID = "CAMPAIGN_ID";

    public static final int PROFILE_DOC_IMAGE = 11;
    public static final String SIGNUP_PROFILE_DOCUMENT_IMAGES = "pg_profile_document_images";
    public static final int ROOM_IMAGE = 22;
    public static final int ROOM_MEMBER_DOC_IMAGE = 33;


    public static final String PRODUCT_ID_STRING = "PRODUCT_ID_STRING";
    public static final String BUSINESS_ID1 = "BUSINESS_ID1";

    public static final String BUSINESS_IMAGE = "BUSINESS_IMAGE";
    public static final String BUSINESS_IDNEW = "BUSINESS_IDNEW";
    public static final String BUSINESS_CONTACT = "BUSINESS_CONTACT";
    public static final String BUSINESS_EMAIL = "BUSINESS_EMAIL";
    public static final String BUSINESS_ADDRESS = "BUSINESS_ADDRESS";
    public static final String BUSINESS_CITY = "BUSINESS_CITY";
    public static final String BUSINESS_DISTRICT = "BUSINESS_DISTRICT";
    public static final String BUSINESS_STATE = "BUSINESS_STATE";
    public static final String BUSINESS_PINCODE = "BUSINESS_PINCODE";
    public static final String TOTAL_PRODUCT = "TOTAL_PRODUCT";
    public static final String ProductList = "ProductList";
    public static final String Position = "Position";
    public static final String VendorDetailList = "VendorDetailList";
    public static final String PositionOfVendor = "PositionOfVendor";
    public static final String VendorID = "VendorID";
    public static final String ServiceId = "ServiceId";
    public static final String ServiceName = "ServiceName";
    public static final String SubServiceID = "SubServiceID";
    public static final String SubServiceName = "SubServiceName";
    public static final String Service_LIst = "Service_LIst";
    public static final String SUBCATEGORY_ID = "SUBCATEGORY_ID";
    public static final String ProductIDToDisplayListOfVendor = "ProductIDToDisplayListOfVendor";
    public static final String ServiceIDForEnquiry = "ServiceIDForEnquiry";
    public static final String BusinessIDForEnquiry = "BusinessIDForEnquiry";
    public static final String BUSINESS_IDFORCART = "BUSINESS_IDFORCART";
    public static final String DISTRICT_NAME_Location = "DISTRICT_NAME_Location";
    public static final String OfferIDForUpdate = "OfferIDForUpdate";
    public static final String business_banner = "business_banner";
    public static final String LuckyDrawConstant = "LuckyDrawConstant";
    public static final String WalletBalance = "WalletBalance";
    public static final String PRIME_CARD_NUMBER = "PRIME_CARD_NUMBER";
    public static final String PRIME_START_DATE = "PRIME_START_DATE";
    public static final String PRIME_END_DATE = "PRIME_END_DATE";
    public static final String Image = "Image";


    public static void EditTextAnim(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //float finalRadius = (float) Math.max((int) event.getX(), (int) event.getY()) * 1.2f;
                if (event.getAction() == MotionEvent.ACTION_DOWN && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !editText.hasFocus()) {

                    ViewAnimationUtils.createCircularReveal(editText,
                            editText.getWidth() / 2,
                            1,
                            0,
                            editText.getWidth() / 2).start();

                    //Log.d("my_tag", "onTouch: CenterX "+(int) event.getX()+"\nCenterY" +(int) event.getY()+"\nHeight "+editText.getHeight()+"\nWidth "+editText.getWidth());
                }
                return false;
            }
        });
    }

    public static String Reg_id(Context context) {
        String role_id = "0";
        if (Shared_Preferences.getPrefs(context, REG_ID) != null)
            role_id = Shared_Preferences.getPrefs(context, REG_ID);
        return role_id;
    }

    public static String Reg_mobile(Context context) {
        String reg_mobile = "0";
        if (Shared_Preferences.getPrefs(context, REG_MOBILE) != null)
            reg_mobile = Shared_Preferences.getPrefs(context, REG_MOBILE);
        return reg_mobile;
    }

    public static void loadImage(Context context, String string_url, final ImageView imageView, int placeholder) {

        Glide.with(context).load(string_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(imageView);



    }


    public static void showDocument(Context context, String string_url) {
        final Dialog dialog = new Dialog(context, R.style.MaterialDialog);
        dialog.setContentView(R.layout.dialog_view_image);
        dialog.setCancelable(false);
        final ImageView iv_doc = (ImageView) dialog.findViewById(R.id.iv_doc);
        final ProgressBar dialog_progress_bar = (ProgressBar) dialog.findViewById(R.id.dialog_progress_bar);
       /* Glide.with(context).load(string_url)

                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        iv_doc.setImageDrawable(resource);
                        dialog_progress_bar.setVisibility(View.GONE);
                        return true;
                    }
                })
                .placeholder(R.mipmap.ic_placeholder_square)
                .error(R.mipmap.ic_placeholder_square)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_doc);*/

        Glide.with(context).load(string_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        iv_doc.setImageDrawable(resource);
                        dialog_progress_bar.setVisibility(View.GONE);
                        return true;
                    }
                })
                .into(iv_doc);
        dialog.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MaterialDialog; //style id
        dialog.show();
    }


    public static void change_language(final Activity activity) {
        int languageSelection;

        if (Shared_Preferences.getPrefs(activity, "select") == null) {
            languageSelection = 0;
        } else {
            languageSelection = Integer.parseInt(Shared_Preferences.getPrefs(activity, "select"));
        }

        final Intent intent;

        Log.d("regid", "change_language: "+Shared_Preferences.getPrefs(activity, Constants.REG_ID));
        if (Shared_Preferences.getPrefs(activity, Constants.REG_ID) != null)
            intent = new Intent(activity, MainActivity.class);
        else
            intent = new Intent(activity, LoginActivity.class);

        final CharSequence[] items = activity.getResources().getStringArray(R.array.lang);


        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.select_language));

        builder.setSingleChoiceItems(items, languageSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                changeLang("en", activity);
                                Shared_Preferences.setPrefs(activity, "select", "0");
                                break;
                            case 1:
                                changeLang("hi", activity);
                                Shared_Preferences.setPrefs(activity, "select", "1");
                                break;
                            case 2:
                                changeLang("mr", activity);
                                Shared_Preferences.setPrefs(activity, "select", "2");
                                break;


                        }


//                        dialog.dismiss();


                    }



                });

        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, activity.getResources().getString(R.string.set_lang_toast), Toast.LENGTH_SHORT).show();
                activity.startActivity(intent);
                activity.finish();
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();

    }

    public static void changeLang(String lang, Activity activity) {

        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        //saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
        // updateTexts();
    }
}
