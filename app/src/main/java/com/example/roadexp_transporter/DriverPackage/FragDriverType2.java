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


public class FragDriverType2 extends Fragment {

    private View mRootView;
    private List<Driver> mDriverList;

    public FragDriverType2() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_driver_for_all, container, false);

        mDriverList = new ArrayList<>();
        fetchDriverList();
        return mRootView;
    }

    private void fetchDriverList() {

        mDriverList.add(new Driver(6,"Manjit Singh",2));
        mDriverList.add(new Driver(7,"Uma Shankar",2));
        mDriverList.add(new Driver(8,"Mantu Kumar",2));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_driver);
        DriverAdapter adapter = new DriverAdapter(getActivity(),mDriverList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }



}
