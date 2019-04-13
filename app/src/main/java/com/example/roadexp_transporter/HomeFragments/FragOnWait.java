package com.example.roadexp_transporter.HomeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;


public class FragOnWait extends Fragment {

    private View mRootView;
    private List<Vehicle> mOnWaitVehicles;



    public FragOnWait() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView =  inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        mOnWaitVehicles = new ArrayList<>();
        fetchOnWaitVehicles();

        return mRootView;
    }

    private void fetchOnWaitVehicles() {

        mOnWaitVehicles.add(new Vehicle(2,2,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Isant Sharma"));

        mOnWaitVehicles.add(new Vehicle(7,2,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Rahul Dravid"));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mOnWaitVehicles);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


    }

}
