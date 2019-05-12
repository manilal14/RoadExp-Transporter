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

public class AVFrag1 extends Fragment {

    private String TAG = "AVFrag1";
    private View mRoot;

    private AddVehicleHomePage mActivity;

    public AVFrag1() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddVehicleHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.avfrag1, container, false);
        mActivity.translateTruck(0);

        fetchVehicleType();
        fetchPermitType();

        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AVFrag2(),"av_frag_2");
            }
        });

    }

    private void fetchVehicleType() {

        Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_vehicle_type);

        ArrayList<String> vehicleList = new ArrayList<>();


        vehicleList.add("");
        vehicleList.add("Kedar Jadav");
        vehicleList.add("Virat Kohli");
        vehicleList.add("MS Dhoni");
        vehicleList.add("Yuvraj Singh");
        vehicleList.add("Saurav Ganguly");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, vehicleList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiSelectDriver.setAdapter(adapter);

    }

    private void fetchPermitType() {

        Spinner spiPermitType = mRoot.findViewById(R.id.spinner_permit_type);

        ArrayList<String> permitList = new ArrayList<>();

        permitList.add("ALL INDIA");
        permitList.add("STATE");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, permitList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);
        spiPermitType.setAdapter(adapter);

    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }

}
