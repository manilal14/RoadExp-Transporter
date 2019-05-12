package com.example.roadexp_transporter.Reports.PaymentReport;

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

import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryPage;

import java.util.List;

public class PaymentReportDriverAdapter extends RecyclerView.Adapter<PaymentReportDriverAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<Driver> mDriverList;

    public PaymentReportDriverAdapter(Context mCtx, List<Driver> mDriverList) {
        this.mCtx = mCtx;
        this.mDriverList = mDriverList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_report_card1,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        Driver driver = mDriverList.get(i);


        h.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCtx.startActivity(new Intent(mCtx, PaymentHistoryPage.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDriverList.size();
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
