package com.example.roadexp_transporter.DriverPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.VehiclePackage.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.VehicleAdapter;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;


public class FragDriverType1 extends Fragment {

    private View mRootView;
    private List<Driver> mDriverList;

    public FragDriverType1() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_driver_for_all, container, false);

        mDriverList = new ArrayList<>();
        fetchDriverList();


        return mRootView;
    }

    private void fetchDriverList(){

        mDriverList.add(new Driver(1,"Anirudh Sai",1));
        mDriverList.add(new Driver(2,"Krishn Rajak",1));
        mDriverList.add(new Driver(3,"Archit",1));
        mDriverList.add(new Driver(4,"Vishal Singh",1));
        mDriverList.add(new Driver(5,"Ranjeet Kumar",1));



        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_driver);
        DriverAdapter adapter = new DriverAdapter(getActivity(),mDriverList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

}
