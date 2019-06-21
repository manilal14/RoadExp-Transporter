package com.example.roadexp_transporter.DriverPackage.Unverified;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.DriverPackage.DriverAdapter;
import com.example.roadexp_transporter.DriverPackage.DriverHomepage;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class FragNotMapped extends Fragment {

    private View mRootView;
    private List<Driver> mDriverList;

    private UnverifiedDriver mActivity;
    private String TAG = "FragNotMapped";

    public FragNotMapped() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG,"called : onActivityCreated1");

        mActivity = (UnverifiedDriver) getActivity();
        fetchTypeOneDriver();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG, "called : onCreateView1");
        mDriverList = new ArrayList<>();
        mRootView = inflater.inflate(R.layout.fragment_un_verified_frag1, container, false);
        return mRootView;
    }

    private void fetchTypeOneDriver(){

        Log.e(TAG,"called : fetchTypeOneDriver");

        //Type 1 driver
        mDriverList = mActivity.getDriverListFromParent(4);

        Log.e(TAG, "UnmappedDriver = "+ mDriverList.size());

        TextView tv_err = mRootView.findViewById(R.id.error_message);
        if(mDriverList.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No Driver is Unmapped");
        }

        else{
            tv_err.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_driver);
        DriverAdapter adapter     = new DriverAdapter(getActivity(),mDriverList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

}
