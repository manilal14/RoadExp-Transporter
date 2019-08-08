package com.road.roadexp_transporter.VehiclePackage;


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

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class FragMoving extends Fragment {

    private View mRootView;
    private List<Vehicle> mMovingVehicleList;

    private final String TAG = "FragMoving";
    private VehicleStatusHomePage mActivity;


    public FragMoving() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovingVehicleList = new ArrayList<>();
        mActivity = (VehicleStatusHomePage) getActivity();
        fetchMovingVehicle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        return mRootView;
    }

    private void fetchMovingVehicle() {
        Log.e(TAG,"fetched moving vehicle");
        mMovingVehicleList = mActivity.fetchParticularVehicle(1);
        TextView tv_err = mRootView.findViewById(R.id.error_message);

        if(mMovingVehicleList.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No moving vehicle available");
        }

        else{
            tv_err.setVisibility(View.GONE);
        }


        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mMovingVehicleList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

}
