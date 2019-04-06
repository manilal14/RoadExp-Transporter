package com.example.roadexp_transporter.VehicleDetailPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;

import java.util.Objects;


public class FragVehicleInfo extends Fragment {

    private String TAG = "FragVehicleInfo";
    private View mRoot;

    public FragVehicleInfo() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG,"onCreateView");
        mRoot =  inflater.inflate(R.layout.dialog_vehicle_info, container, false);
        clickListener();

        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Backbutton is clicked");
                onDestroyView();

            }
        });

        mRoot.findViewById(R.id.whole_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Do nothing");
            }
        });
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "OnDestroView2");
        super.onDestroyView();
        assert getFragmentManager() != null;
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

}
