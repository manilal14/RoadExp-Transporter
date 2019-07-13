package com.example.roadexp_transporter.AddNewDriver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.roadexp_transporter.R;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.hasPermissions;

public class AddDriverPage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private final int PERMISSION_ALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver_home_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        replaceFragment(new ADFrag1(),"ad_frag_1");
        clickListener();

        String[] PERMISSIONS = {
                Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        if(!hasPermissions(AddDriverPage.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(AddDriverPage.this, PERMISSIONS, PERMISSION_ALL);
            return;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"called");
    }

    public void translateBoy(float position){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float totalScreenWidth = metrics.widthPixels;
        Log.e(TAG,"FullWidth="+totalScreenWidth+"");

        float translateTo = ((position/3) * totalScreenWidth);

        ImageView iv_walker = findViewById(R.id.walker);
        iv_walker.animate().translationX(translateTo);

        Log.e(TAG,"moved to "+translateTo);
    }


    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.commit();
    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }








}
