package com.windhans.client.forworld.my_library;

import android.content.Context;


/**
 * Created by Darpan on 10-04-2015.
 */
public class Shared_Preferences {

    static String prefName = "VTap";
    static android.content.SharedPreferences preferences;
    static android.content.SharedPreferences.Editor editor;

    public static void setPrefs(Context context, String prefKey, String prefValue) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(prefKey, prefValue);
        editor.commit();
    }

    public static String getPrefs(Context context, String prefKey) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        return preferences.getString(prefKey, null);
    }

    public static void clearPref(Context context) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear().commit();
    }

    public static void clearPref1(Context context, String prefKey) {
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        preferences.edit().remove(prefKey).commit();
        //editor = preferences.edit();
        //editor.clear().commit();
    }

   /* public static void  saveSharedPrefs(UserBasicInfoPojo userBasicInfoPojo,Context context){
        preferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ROLE_ID, userBasicInfoPojo.getRole_id());
        editor.putString(FIRST_NAME, userBasicInfoPojo.getUser_first_name());
        editor.putString(LAST_NAME, userBasicInfoPojo.getUser_last_name());
        editor.putString(MOBILENO, userBasicInfoPojo.getRole_mobile());
        editor.putString(EMAIL, userBasicInfoPojo.getRole_email());
        editor.putString(USER_GENDER, userBasicInfoPojo.getUser_gender());
        editor.putString(USER_DOB, userBasicInfoPojo.getUser_dob());
        editor.putString(IMAGENAME, userBasicInfoPojo.getUser_profile_image());
        editor.commit();
    }*/


}
