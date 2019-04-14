package com.example.roadexp_transporter.VehiclePackage;


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

public class FragTurnedOff extends Fragment {

    private View mRootView;
    private List<Vehicle> mTurnedOffVehicles;


    public FragTurnedOff() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView =  inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        mTurnedOffVehicles = new ArrayList<>();
        fetchOnWaitVehicles();

        return mRootView;


    }

    private void fetchOnWaitVehicles() {

        mTurnedOffVehicles.add(new Vehicle(3,3,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Virat Kohli"));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mTurnedOffVehicles);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


    }

}
