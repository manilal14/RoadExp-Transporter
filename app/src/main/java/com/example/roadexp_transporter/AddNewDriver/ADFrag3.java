package com.example.roadexp_transporter.AddNewDriver;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;

public class ADFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddDriverPage mActivity;


    public ADFrag3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddDriverPage) getActivity();
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
