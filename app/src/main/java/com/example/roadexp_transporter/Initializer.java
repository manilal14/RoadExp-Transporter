package com.example.roadexp_transporter;

import android.app.Application;

import com.example.roadexp_transporter.NetworkState.ConnectivityReceiver;

import net.gotev.uploadservice.UploadService;


public class Initializer extends Application {

    private static Initializer mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        mInstance = this;

    }

    public static synchronized Initializer getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
