package com.road.roadexp_transporter.DriverPackage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class DriverFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public DriverFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "Moving";
            case 1: return "On Wait";
            case 2: return "Turned Off";
        }

        return super.getPageTitle(position);
    }
}
