package com.example.roadexp_transporter.Reports.PaymentReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<PaymentHistoryModel> mPaymentHistoryList;

    private Driver mDriver;

    public PaymentHistoryAdapter(Context mCtx, List<PaymentHistoryModel> travelHistoryList, Driver driver) {
        this.mCtx = mCtx;
        this.mPaymentHistoryList = travelHistoryList;
        mDriver = driver;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_payment_report_detail,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        PaymentHistoryModel travelHistoryModel = mPaymentHistoryList.get(i);

        h.tv_date.setText(travelHistoryModel.getStartDate());

        h.tv_from.setText(travelHistoryModel.getStartPoint());
        h.tv_to.setText(travelHistoryModel.getEndPoint());
        h.tv_totalFare.setText("Rs."+travelHistoryModel.getTotalFare());
        h.tv_driver_cut.setText("Rs."+travelHistoryModel.getDriverCut());

        if(travelHistoryModel.getStops() == 1)
            h.tv_stops.setText(travelHistoryModel.getStops() + " Stop");
        else
            h.tv_stops.setText(travelHistoryModel.getStops()+" Stops");

        h.tv_driver.setText(mDriver.getName());



        if(travelHistoryModel.getRemaining().charAt(0) == '-'){
            h.tv_remining_text.setText("To be paid");
            h.tv_remining.setText("Rs."+travelHistoryModel.getRemaining().substring(1));
        }
        else {
            h.tv_remining_text.setText("Remaining");
            h.tv_remining.setText("Rs."+travelHistoryModel.getRemaining());
        }

    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date, tv_stops, tv_driver;
        private TextView tv_from, tv_to;
        private TextView tv_totalFare, tv_driver_cut, tv_remining;
        private TextView tv_remining_text;

        public VehicleViewHolder(@NonNull View v) {
            super(v);

            tv_date = v.findViewById(R.id.date);
            tv_stops = v.findViewById(R.id.stops);
            tv_driver = v.findViewById(R.id.driver);
            tv_from = v.findViewById(R.id.from_address_line1);
            tv_to = v.findViewById(R.id.to_address_line1);
            tv_totalFare = v.findViewById(R.id.total_fare);
            tv_driver_cut = v.findViewById(R.id.driver_cut);
            tv_remining = v.findViewById(R.id.remaining);

            tv_remining_text = v.findViewById(R.id.remaining_text);




        }
    }
}
