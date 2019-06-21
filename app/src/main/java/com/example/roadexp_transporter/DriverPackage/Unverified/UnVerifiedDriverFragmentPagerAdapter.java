package com.example.roadexp_transporter.DriverPackage.Unverified;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class    UnVerifiedDriverFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public UnVerifiedDriverFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
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
            case 0: return "UnMapped";
            case 1: return "Unverified";

        }

        return super.getPageTitle(position);
    }
}
