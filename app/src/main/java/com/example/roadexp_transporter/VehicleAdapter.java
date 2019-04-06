package com.example.roadexp_transporter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.roadexp_transporter.VehicleDetailPackage.DialogDetailHomepage;

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
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        Vehicle vehicle = mVehicleList.get(i);

        h.toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, MapsActivity.class);
                mCtx.startActivity(i);
            }
        });


        h.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogDetailHomepage().show( ( (FragmentActivity)mCtx).getSupportFragmentManager(), null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        ImageView toMap;
        CardView card;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            card =  itemView.findViewById(R.id.card);
            toMap = itemView.findViewById(R.id.to_map);
        }
    }
}
