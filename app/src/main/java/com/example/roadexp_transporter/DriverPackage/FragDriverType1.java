package com.example.roadexp_transporter.DriverPackage;


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

import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;


public class FragDriverType1 extends Fragment {

    private View mRootView;
    private List<Driver> mDriverList;

    private DriverHomepage mActivity;
    private String TAG = "FragDriverType1";

    public FragDriverType1() {}


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG,"called : onActivityCreated1");

        mActivity = (DriverHomepage) getActivity();
        fetchTypeOneDriver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG, "called : onCreateView1");
        mRootView = inflater.inflate(R.layout.frag_driver_for_all, container, false);

        mDriverList = new ArrayList<>();
        return mRootView;
    }

    private void fetchTypeOneDriver(){

        Log.e(TAG,"called : fetchTypeOneDriver");

        //Type 1 driver
        mDriverList = mActivity.getDriverListFromParent(1);

        Log.e(TAG, "sizeOfDriverType1 = "+ mDriverList.size());

        TextView tv_err = mRootView.findViewById(R.id.error_message);
        if(mDriverList.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No Driver Available");
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
