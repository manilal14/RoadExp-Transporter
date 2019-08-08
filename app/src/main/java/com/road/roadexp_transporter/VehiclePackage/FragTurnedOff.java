package com.road.roadexp_transporter.VehiclePackage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class FragTurnedOff extends Fragment {

    private View mRootView;
    private List<Vehicle> mTurnedOffVehicles;

    private final String TAG = "FragTurnedOff";
    private VehicleStatusHomePage mActivity;


    public FragTurnedOff() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTurnedOffVehicles = new ArrayList<>();
        mActivity = (VehicleStatusHomePage) getActivity();
        fetchTurnedOffVehicle();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =  inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        return mRootView;


    }

    private void fetchTurnedOffVehicle() {
        mTurnedOffVehicles = mActivity.fetchParticularVehicle(3);

        TextView tv_err = mRootView.findViewById(R.id.error_message);
        if(mTurnedOffVehicles.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No vehicle available");
        }
        else{
            tv_err.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mTurnedOffVehicles);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


    }

}