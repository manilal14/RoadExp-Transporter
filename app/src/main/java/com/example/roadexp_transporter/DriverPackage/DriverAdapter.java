package com.example.roadexp_transporter.DriverPackage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roadexp_transporter.VehicleStatus.MapsActivity;
import com.example.roadexp_transporter.R;


import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<Driver> mDriverList;

    public DriverAdapter(Context mCtx, List<Driver> mDriverList) {
        this.mCtx = mCtx;
        this.mDriverList = mDriverList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_driver,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        Driver driver = mDriverList.get(i);

        h.tv_name.setText(driver.getName());

        int status = driver.getStatus();

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
                mCtx.startActivity(new Intent(mCtx, DriverDetail.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDriverList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView iv_drivePic,to_map;
        TextView tv_name;


        public VehicleViewHolder(@NonNull View v) {
            super(v);
            card            = v.findViewById(R.id.card);
            iv_drivePic     = v.findViewById(R.id.driver_pic);
            tv_name         = v.findViewById(R.id.driver_name);
            to_map          = v.findViewById(R.id.to_map);

        }
    }
}
