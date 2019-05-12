package com.example.roadexp_transporter.Reports.PaymentReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<PaymentHistoryModel> mPaymentHistoryList;

    public PaymentHistoryAdapter(Context mCtx, List<PaymentHistoryModel> travelHistoryList) {
        this.mCtx = mCtx;
        this.mPaymentHistoryList = travelHistoryList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_payment_report_detail,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        PaymentHistoryModel travelHistoryModel = mPaymentHistoryList.get(i);


    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {



        public VehicleViewHolder(@NonNull View v) {
            super(v); }
    }
}
