/*
package com.windhans.client.forworld.Notification;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;


*/
/**
 * Created by pc on 10/14/2017.
 *//*


public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceId {

    private static final String TAG = "MyAndroidFCMIIDService";
    SharedPreferences pref;

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mSharedPreference.edit();

        // store device token for use latter in application.
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // 0 - for private mode
        final SharedPreferences.Editor editor2 = pref.edit();
        editor2.putString("FCM_DeviceToken", refreshedToken);
        editor2.commit();
    }

    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}*/
