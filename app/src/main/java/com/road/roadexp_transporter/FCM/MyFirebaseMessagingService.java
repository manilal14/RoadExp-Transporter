package com.road.roadexp_transporter.FCM;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessageing";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {}

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notifyUser(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        storeTokenInSP(token);
    }

    private void storeTokenInSP(String refreshedToken) {
        SharedPrefFcm.getmInstance(getApplicationContext()).storeToken(refreshedToken);
        Log.e(TAG, "token stored in sharedPref : "+SharedPrefFcm.getmInstance(getApplicationContext()).getToken());
    }

    public void notifyUser(String title ,String notification){

        MyFcmNotificationManager myFcmNotificationManager = new MyFcmNotificationManager(getApplicationContext());
        myFcmNotificationManager.createNotification(title,notification);
    }
}



