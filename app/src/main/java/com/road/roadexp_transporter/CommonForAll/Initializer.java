package com.road.roadexp_transporter.CommonForAll;

import android.app.Application;


public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }
}
