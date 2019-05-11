package com.example.roadexp_transporter.groupingVehicles;

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

import com.example.roadexp_transporter.VehicleStatus.MapsActivity;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.VehiclePackage.Vehicle;
import com.example.roadexp_transporter.VehicleDetailPackage.DialogDetailHomepage;

import java.util.List;

public class GroupedVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    private List<MyListItem> consolidatedItemList;

    public GroupedVehicleAdapter(Context mCtx, List<MyListItem> consolidatedItemList) {
        this.mCtx = mCtx;
        this.consolidatedItemList = consolidatedItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case MyListItem.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.recycler_view_vehicle, parent,false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case MyListItem.TYPE_STATUS:
                View v2 = inflater.inflate(R.layout.recycler_view_vehicle_status, parent, false);
                viewHolder = new StatusViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        switch (viewHolder.getItemViewType()) {

            case MyListItem.TYPE_GENERAL:

                GeneralItems generalItem   = (GeneralItems) consolidatedItemList.get(i);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) viewHolder;

                Vehicle vehicle = generalItem.getVehicle();

                generalViewHolder.tv_vehicle_type.setText(vehicle.getVehicleType());
                generalViewHolder.tv_plate_no.setText(vehicle.getPlateNunmber());
                generalViewHolder.tv_driver.setText(vehicle.getMapedDriverName());

                int status = vehicle.getStatus();
                switch (status){
                    case 1: generalViewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.green)); break;
                    case 2: generalViewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.yellow)); break;
                    case 3: generalViewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mCtx,R.color.black)); break;

                }

                if(status == 3)
                    generalViewHolder.to_map.setVisibility(View.GONE);
                else {
                    generalViewHolder.to_map.setVisibility(View.VISIBLE);
                    generalViewHolder.to_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mCtx, MapsActivity.class);
                            mCtx.startActivity(i);
                        }
                    });

                }

                generalViewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DialogDetailHomepage().show( ( (FragmentActivity)mCtx).getSupportFragmentManager(), null);
                    }
                });




                break;

            case MyListItem.TYPE_STATUS:
                StatusItem statusItem = (StatusItem) consolidatedItemList.get(i);
                StatusViewHolder statusViewHolder = (StatusViewHolder) viewHolder;

                status = statusItem.getStatus();

                String statusMessage = "";

                switch (status){
                    case 1: statusMessage = "Moving";break;
                    case 2: statusMessage = "On wait";break;
                    case 3: statusMessage = "Turned off";break;
                }

                statusViewHolder.tv_status.setText(statusMessage);

                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedItemList.get(position).getType();
    }


    @Override
    public int getItemCount() {
        return consolidatedItemList.size();
    }



    class GeneralViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView ic_icon,to_map;
        TextView tv_vehicle_type, tv_plate_no, tv_driver;

        public GeneralViewHolder(View v) {
            super(v);

            card            = v.findViewById(R.id.card);
            ic_icon         = v.findViewById(R.id.image_truck);
            to_map          = v.findViewById(R.id.to_map);
            tv_vehicle_type = v.findViewById(R.id.vehicle_type);
            tv_plate_no     = v.findViewById(R.id.plate_number);
            tv_driver       = v.findViewById(R.id.driver);

        }
    }


    class StatusViewHolder extends RecyclerView.ViewHolder {

        TextView tv_status;
        public StatusViewHolder(View v) {
            super(v);
            this.tv_status =  v.findViewById(R.id.status);
        }
    }


}
