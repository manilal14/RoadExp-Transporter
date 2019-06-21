package com.example.roadexp_transporter.AddNewVehicle;
import android.Manifest;
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
import android.widget.ImageView;

import com.example.roadexp_transporter.AddNewDriver.AddDriverPage;
import com.example.roadexp_transporter.R;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.hasPermissions;


public class AddVehicleHomePage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_vehicle_home_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        clickListener();

        replaceFragment(new AVFrag1(),"av_frag_1");

        String[] PERMISSIONS = {
                Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        if(!hasPermissions(AddVehicleHomePage.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(AddVehicleHomePage.this, PERMISSIONS, PERMISSION_ALL);
            return;
        }



    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void translateTruck(float position){


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float totalScreenWidth = metrics.widthPixels;
        Log.e(TAG,"FullWidth="+totalScreenWidth+"");

        float translateTo = ((position/3) * totalScreenWidth);

        ImageView iv_truck = findViewById(R.id.truck);
        iv_truck.animate().translationX(translateTo);

        Log.e(TAG,"moved to "+translateTo);
    }


    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment,tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.commit();
    }

}
