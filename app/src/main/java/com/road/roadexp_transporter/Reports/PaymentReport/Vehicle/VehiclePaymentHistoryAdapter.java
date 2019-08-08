package com.road.roadexp_transporter.Reports.PaymentReport.Vehicle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Reports.PaymentReport.PaymentHistoryModel;

import java.util.List;

public class VehiclePaymentHistoryAdapter extends RecyclerView.Adapter<VehiclePaymentHistoryAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<PaymentHistoryModel> mPaymentHistoryList;


    public VehiclePaymentHistoryAdapter(Context mCtx, List<PaymentHistoryModel> travelHistoryList) {
        this.mCtx = mCtx;
        this.mPaymentHistoryList = travelHistoryList;

    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_payment_report_vehicle,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        PaymentHistoryModel travelHistoryModel = mPaymentHistoryList.get(i);

        h.tv_date.setText(travelHistoryModel.getStartDate());

        h.tv_from.setText(travelHistoryModel.getStartPoint());
        h.tv_to.setText(travelHistoryModel.getEndPoint());
        h.tv_totalFare.setText("Rs."+travelHistoryModel.getTotalFare());

        if(travelHistoryModel.getStops() == 1)
            h.tv_stops.setText(travelHistoryModel.getStops() + " Stop");
        else
            h.tv_stops.setText(travelHistoryModel.getStops()+" Stops");

        h.tv_driver.setText(travelHistoryModel.getDriveName());




    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date, tv_stops, tv_driver;
        private TextView tv_from, tv_to;
        private TextView tv_totalFare;


        public VehicleViewHolder(@NonNull View v) {
            super(v);

            tv_date = v.findViewById(R.id.date);
            tv_stops = v.findViewById(R.id.stops);
            tv_driver = v.findViewById(R.id.driver);
            tv_from = v.findViewById(R.id.from_address_line1);
            tv_to = v.findViewById(R.id.to_address_line1);
            tv_totalFare = v.findViewById(R.id.total_fare);





        }
    }
}
