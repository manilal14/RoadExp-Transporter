package com.example.roadexp_transporter.VehiclePackage.UnverifiedVehicle;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.VehicleAdapter;
import com.example.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;

import java.util.ArrayList;
import java.util.List;


public class FragVehicleNotMapped extends Fragment {

    private View mRootView;
    private List<Vehicle> mVehicleNotMapped;

    private final String TAG = "FragVehicleNotMapped";
    private UnverifiedVehiclePage mActivity;


    public FragVehicleNotMapped() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mVehicleNotMapped = new ArrayList<>();
        mActivity = (UnverifiedVehiclePage) getActivity();
        fetchMovingVehicle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        return mRootView;
    }

    private void fetchMovingVehicle() {

        mVehicleNotMapped = mActivity.fetchParticularUnverifiedVehicle(2);
        TextView tv_err = mRootView.findViewById(R.id.error_message);

        if(mVehicleNotMapped.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No vehicle available");
        }

        else{
            tv_err.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mVehicleNotMapped);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

}