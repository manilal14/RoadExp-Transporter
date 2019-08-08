package com.road.roadexp_transporter.DriverPackage;


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

import com.road.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;


public class FragDriverType2 extends Fragment {

    private View mRootView;
    private List<Driver> mDriverTwoList;

    private final String TAG = "FragDriverType1";
    private DriverHomepage mActivity;

    public FragDriverType2() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG,"called : onActivityCreated2");

        mActivity = (DriverHomepage) getActivity();
        fetchTypeTwoDriver();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_driver_for_all, container, false);
        mDriverTwoList = new ArrayList<>();
        return mRootView;
    }

    private void fetchTypeTwoDriver(){

        Log.e(TAG,"called : fetchTypeTwoDriver");
        mDriverTwoList = mActivity.getDriverListFromParent(2);

        Log.e(TAG, "sizeOfDriverType2 = "+ mDriverTwoList.size());

        TextView tv_err = mRootView.findViewById(R.id.error_message);
        if(mDriverTwoList.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No Driver Available");
        }

        else{
            tv_err.setVisibility(View.GONE);
        }


        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_driver);
        DriverAdapter adapter = new DriverAdapter(getActivity(),mDriverTwoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }


}
