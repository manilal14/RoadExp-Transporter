package com.example.roadexp_transporter.Reports.TravelReport;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.Driver.TRFragDriver;
import com.example.roadexp_transporter.Reports.TravelReport.Vehicle.TRFragVehicle;

import java.util.ArrayList;
import java.util.List;

public class TravelReportHomePage extends AppCompatActivity {

    List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_travel_report_page);


        mFragmentList = new ArrayList<>();

        mFragmentList.add(new TRFragDriver());
        mFragmentList.add(new TRFragVehicle());



        ViewPager viewPager = findViewById(R.id.viewpager_travel_report);


        TravelReportFragmentPagerAdapter adapter = new TravelReportFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_travel_report);
        tabLayout.setupWithViewPager(viewPager);

        clickListener();
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
