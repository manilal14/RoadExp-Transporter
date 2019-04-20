package com.example.roadexp_transporter.AddNewVehicle;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.DriverPackage.DriverFragmentPagerAdapter;
import com.example.roadexp_transporter.DriverPackage.FragDriverType1;
import com.example.roadexp_transporter.DriverPackage.FragDriverType2;
import com.example.roadexp_transporter.DriverPackage.FragDriverType3;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class AddVehicleHomePage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_add_vehicle_home_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        replaceFragment(new AVFrag1(),"av_frag_1");

        translateTruck(0);



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG,"onBackpressed");

        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("av_frag_1");
        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("av_frag_2");

        if(fragment2 != null && fragment2.isVisible()) {
            Log.e(TAG, "onBackpressed2");
            translateTruck(1);
        }

        if(fragment1 != null && fragment1.isVisible()) {
            Log.e(TAG, "onBackpressed1");
            translateTruck(0);
        }




    }
}
