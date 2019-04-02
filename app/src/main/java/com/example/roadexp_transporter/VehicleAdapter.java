package com.example.roadexp_transporter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<Vehicle> mVehicleList;

    public VehicleAdapter(Context mCtx, List<Vehicle> mVehicleList) {
        this.mCtx = mCtx;
        this.mVehicleList = mVehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_vehicle,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder vehicleViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
