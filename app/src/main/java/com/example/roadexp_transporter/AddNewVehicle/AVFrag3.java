package com.example.roadexp_transporter.AddNewVehicle;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.roadexp_transporter.R;

import java.util.ArrayList;

public class AVFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddVehicleHomePage mActivity;


    public AVFrag3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddVehicleHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.avfrag3, container, false);
        fetchDriverList();
        clickListener();

        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
               mActivity.translateTruck(1);
            }
        });

        mRoot.findViewById(R.id.all_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.translateTruck(0);
                getActivity().finish();
            }
        });

    }

    private void fetchDriverList() {

        Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_select_driver);

        ArrayList<String> driverLIst = new ArrayList<>();


        driverLIst.add("");
        driverLIst.add("Kedar Jadav");
        driverLIst.add("Virat Kohli");
        driverLIst.add("MS Dhoni");
        driverLIst.add("Yuvraj Singh");
        driverLIst.add("Saurav Ganguly");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, driverLIst);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiSelectDriver.setAdapter(adapter);

    }


}
