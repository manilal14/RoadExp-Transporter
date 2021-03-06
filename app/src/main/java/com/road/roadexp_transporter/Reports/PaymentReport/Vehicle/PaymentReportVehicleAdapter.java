package com.road.roadexp_transporter.Reports.PaymentReport.Vehicle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;

import java.util.List;

public class PaymentReportVehicleAdapter extends RecyclerView.Adapter<PaymentReportVehicleAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<Vehicle> mVehicleList;

    public PaymentReportVehicleAdapter(Context mCtx, List<Vehicle> mVehicleList) {
        this.mCtx = mCtx;
        this.mVehicleList = mVehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_report_card1,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        final Vehicle vehicle = mVehicleList.get(i);

        h.tv_name.setText(vehicle.getVehicleType());
        h.tv_number.setText(vehicle.getPlateNumber());

        h.iv_image.setImageResource(R.drawable.truck);

        h.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCtx.startActivity(new Intent(mCtx, VehiclePaymentHistoryPage.class)
                .putExtra("vehicle_detail",vehicle));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView iv_image;
        TextView tv_name, tv_number;

        public VehicleViewHolder(@NonNull View v) {
            super(v);

            card      = v.findViewById(R.id.card);
            iv_image  = v.findViewById(R.id.image);
            tv_name   = v.findViewById(R.id.name);
            tv_number = v.findViewById(R.id.number);
        }
    }
}
