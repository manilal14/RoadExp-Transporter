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


public class FragMoving extends Fragment {

    private View mRootView;
    private List<Vehicle> mMovingVehicleList;

    public FragMoving() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_vehicles_for_all, container, false);

        mMovingVehicleList = new ArrayList<>();
        fetchMovingVehicle();


        return mRootView;
    }

    private void fetchMovingVehicle() {

        mMovingVehicleList.add(new Vehicle(1,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Kedar Yadav"));


        mMovingVehicleList.add(new Vehicle(4,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Viru Paji"));

        mMovingVehicleList.add(new Vehicle(5,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Yuvraj Singh"));

        mMovingVehicleList.add(new Vehicle(6,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Mahi Bhai"));

        mMovingVehicleList.add(new Vehicle(8,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Saurav Dada"));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mMovingVehicleList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

}
