package com.example.roadexp_transporter.VehicleStatus;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.roadexp_transporter.AddNewVehicle.AddVehicleHomePage;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.VehiclePackage.FragMoving;
import com.example.roadexp_transporter.VehiclePackage.FragOnWait;
import com.example.roadexp_transporter.VehiclePackage.FragTurnedOff;
import com.example.roadexp_transporter.VehiclePackage.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.VehicleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VehicleStatusHomePage extends AppCompatActivity {

    private List<Fragment> mFragmentList;
    private ArrayList<Vehicle> mVehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_vehicle_status_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mVehicleList = new ArrayList<>();
        //fetchVehicleList();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragMoving());
        mFragmentList.add(new FragOnWait());
        mFragmentList.add(new FragTurnedOff());

        ViewPager viewPager = findViewById(R.id.viewpager);

        VehicleFragmentPagerAdapter adapter = new VehicleFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        clickListerner();
        
    }


    private void clickListerner() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.add_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleStatusHomePage.this, AddVehicleHomePage.class));
            }
        });

    }
}
