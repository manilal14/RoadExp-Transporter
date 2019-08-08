package com.road.roadexp_transporter.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.road.roadexp_transporter.HomePage.AppHomePage;
import com.road.roadexp_transporter.R;

import java.util.Date;


public class MyFcmNotificationManager {


    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public MyFcmNotificationManager(Context context) {
        mContext = context;
    }


    public void createNotification(String title, String message)
    {


        Intent resultIntent = new Intent(mContext , AppHomePage.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext,"");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher))
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +mContext.getPackageName()+"/"+R.raw.alarm_sound))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        mNotificationManager.notify(m, mBuilder.build());
    }

}
