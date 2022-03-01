package com.windhans.client.forworld.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.windhans.client.forworld.R;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on 10/14/2017.
 */

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    String body, message, type, url;
    int resourceID;
    Intent intentw = null;
    NotificationCompat.Builder mBuilder;
    Intent notificationIntent;
    Uri notifySound;
    private String title = "BRE",  image;
    private String image_path, json_image_path;
    private DatabaseSqliteHandler db;
    private JSONObject jsonObject;
    public static final String INTENT_FILTER = "INTENT_FILTER";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data Message Body: " + remoteMessage.getData());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification());

        String jsonStr="";
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject();
            jsonObject.put("message",remoteMessage.getNotification());
            jsonStr=jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onMessageReceived: "+jsonStr);

       // JSONObject data = remoteMessage.getData();
        // JSONObject json=null;
        String insertData = "";
        JSONObject json = null;
        try {
            json = new JSONObject(remoteMessage.getData().toString());

        //insertData = "" + json;
        createNotification(json.getString("message"));
       // type = json.getString("type");
        Log.d(TAG, "Notification Message Body: type = " + json);
            jsonObject = new JSONObject(remoteMessage.getData());
            if (jsonObject.has("image")) {
                json_image_path = jsonObject.getString("image");
                image = jsonObject.getString("image");
            }
            if (!jsonObject.getString("title").isEmpty()) {
                title = jsonObject.getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        title = remoteMessage.getNotification().getTitle();
        createNotification(remoteMessage.getNotification().getBody());


       // Log.d(TAG, "onMessageReceived: "+title);

        //createNotification(body,message);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

    public String sendRegistrationToServer(String token) {
        return  token;
    }


    private void createNotification(String messageBody) {

        Intent i = new Intent(this, ActivityNotification.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        db = DatabaseSqliteHandler.getInstance(this);
        db.insert_notification(title, messageBody, image);
        int requestCode = ("someString" + System.currentTimeMillis()).hashCode();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

       /* Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.applogonew)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel("abc", "xyz",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("this is notific");
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);
        }




        notificationManager.notify(0, mNotificationBuilder.build());
    }
}