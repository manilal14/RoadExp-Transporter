package com.example.roadexp_transporter.DriverPackage;

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

import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class DriverHomepage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Driver> mDriverList;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_driver_home_page);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        mDriverList = new ArrayList<>();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragDriverType1());
        mFragmentList.add(new FragDriverType2());
        mFragmentList.add(new FragDriverType3());

        ViewPager viewPager = findViewById(R.id.viewpager_driver);

        DriverFragmentPagerAdapter adapter = new DriverFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs_driver);
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
