package com.example.roadexp_transporter.AddNewDriver;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.roadexp_transporter.AddNewVehicle.AddVehicleHomePage;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;

public class ADFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddDriverHomePage mActivity;


    public ADFrag3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddDriverHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.adfrag3, container, false);
        mActivity.translateBoy(2);
        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();

            }
        });

        mRoot.findViewById(R.id.all_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }



}
