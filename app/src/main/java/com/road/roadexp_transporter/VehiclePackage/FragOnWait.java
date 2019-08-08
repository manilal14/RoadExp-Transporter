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


public class FragOnWait extends Fragment {

    private View mRootView;
    private List<Vehicle> mOnWaitVehicles;

    private final String TAG = "FragOnWait";
    private VehicleStatusHomePage mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mOnWaitVehicles = new ArrayList<>();
        mActivity = (VehicleStatusHomePage) getActivity();
        fetchOnWaitVehicles();
    }



    public FragOnWait() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView =  inflater.inflate(R.layout.frag_vehicles_for_all, container, false);
        return mRootView;
    }

    private void fetchOnWaitVehicles() {

        mOnWaitVehicles = mActivity.fetchParticularVehicle(2);

        TextView tv_err = mRootView.findViewById(R.id.error_message);
        if(mOnWaitVehicles.size() == 0){
            tv_err.setVisibility(View.VISIBLE);
            tv_err.setText("No vehicle available");
        }

        else{
            tv_err.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_vehicles);
        VehicleAdapter adapter = new VehicleAdapter(getActivity(),mOnWaitVehicles);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


    }

}
