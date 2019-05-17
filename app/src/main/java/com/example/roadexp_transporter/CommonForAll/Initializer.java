package com.example.roadexp_transporter.CommonForAll;

import android.app.Application;

import com.example.roadexp_transporter.BuildConfig;


public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }
}
