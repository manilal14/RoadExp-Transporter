package com.example.roadexp_transporter.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.roadexp_transporter.MapsActivity;
import com.example.roadexp_transporter.R;
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

        h.tv_vehicle_type.setText(vehicle.getVehicleType());
        h.tv_plate_no.setText(vehicle.getPlateNunmber());
        h.tv_driver.setText(vehicle.getMapedDriverName());

        int status = vehicle.getStatus();

        switch (status){
            case 1: h.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.green)); break;
            case 2: h.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.yellow)); break;
            case 3: h.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.light_red_gradient)); break;

        }

        if(status == 3)
            h.to_map.setVisibility(View.GONE);
        else {
            h.to_map.setVisibility(View.VISIBLE);
            h.to_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, MapsActivity.class);
                    mCtx.startActivity(i);
                }
            });

        }

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

        CardView card;
        ImageView ic_icon,to_map;
        TextView tv_vehicle_type, tv_plate_no, tv_driver;

        public VehicleViewHolder(@NonNull View v) {
            super(v);
            card            = v.findViewById(R.id.card);
            ic_icon         = v.findViewById(R.id.image_truck);
            to_map          = v.findViewById(R.id.to_map);
            tv_vehicle_type = v.findViewById(R.id.vehicle_type);
            tv_plate_no     = v.findViewById(R.id.plate_number);
            tv_driver       = v.findViewById(R.id.driver);
        }
    }
}
