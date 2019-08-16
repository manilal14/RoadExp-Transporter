package com.road.roadexp_transporter.LoginSingUp.onBoarding;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.road.roadexp_transporter.HomePage.AppHomePage;
import com.road.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.road.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class onBoarding extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private List<Fragment> mFragmentList;
    private TextView tv_next, tv_skip;
    private ViewPager viewPager;
    private TextView dot1,dot2,dot3, dot4, dot5;

    private LoginSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        session = new LoginSessionManager(getApplicationContext());

        if(session.onBoardingShown()){
            //startActivity(new Intent(onBoarding.this, SplashScreen.class));
            startActivity(new Intent(onBoarding.this, AppHomePage.class));
            finish();
        }


        setContentView(R.layout.activity_on_boarding);

        mFragmentList = new ArrayList<>();

        mFragmentList.add(new SCPage1());
        mFragmentList.add(new SCPage2());
        mFragmentList.add(new SCPage3());
        mFragmentList.add(new SCPage4());
        mFragmentList.add(new SCPage5());

        viewPager = findViewById(R.id.viewpager_onBoarding);
        viewPager.addOnPageChangeListener(this);

        SCFragmentPagerAdapter adapter = new SCFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        dot5 = findViewById(R.id.dot5);

        dot1.setBackground(getDrawable(R.drawable.indicator_selected));

        tv_next = findViewById(R.id.next);
        tv_skip = findViewById(R.id.skip);

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = viewPager.getCurrentItem();
                if(id == 4){
                    startActivity(new Intent(onBoarding.this,AppHomePage.class));
                    finish();
                }
                else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }

            }
        });


        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(onBoarding.this,AppHomePage.class));
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(final int position) {

        switch (position){

            case 0 :
                dot1.setBackground(getDrawable(R.drawable.indicator_selected));
                dot2.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot3.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot4.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot5.setBackground(getDrawable(R.drawable.indicator_unselected));

                tv_next.setText("Next");
                tv_skip.setVisibility(View.VISIBLE);
                break;

            case 1:
                dot1.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot2.setBackground(getDrawable(R.drawable.indicator_selected));
                dot3.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot4.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot5.setBackground(getDrawable(R.drawable.indicator_unselected));
                tv_next.setText("Next");
                tv_skip.setVisibility(View.VISIBLE);
                break;

            case 2:
                dot1.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot2.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot3.setBackground(getDrawable(R.drawable.indicator_selected));
                dot4.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot5.setBackground(getDrawable(R.drawable.indicator_unselected));
                tv_next.setText("Next");
                tv_skip.setVisibility(View.VISIBLE);
                break;

            case 3:
                dot1.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot2.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot3.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot4.setBackground(getDrawable(R.drawable.indicator_selected));
                dot5.setBackground(getDrawable(R.drawable.indicator_unselected));
                tv_next.setText("Next");
                tv_skip.setVisibility(View.VISIBLE);
                break;

            case 4:
                dot1.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot2.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot3.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot4.setBackground(getDrawable(R.drawable.indicator_unselected));
                dot5.setBackground(getDrawable(R.drawable.indicator_selected));
                tv_next.setText("Next");
                tv_next.setText("Welcome");
                tv_skip.setVisibility(View.INVISIBLE);
                break;

        }
    }
    @Override
    public void onPageScrollStateChanged(int state) { }
}
