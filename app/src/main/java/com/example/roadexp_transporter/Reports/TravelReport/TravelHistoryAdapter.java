package com.example.roadexp_transporter.Reports.TravelReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;

import java.util.List;

public class TravelHistoryAdapter extends RecyclerView.Adapter<TravelHistoryAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<TravelHistoryModel> mTravelHistoryList;

    public TravelHistoryAdapter(Context mCtx, List<TravelHistoryModel> travelHistoryList) {
        this.mCtx = mCtx;
        this.mTravelHistoryList = travelHistoryList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VehicleViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_travel_report_detail,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder h, int i) {

        TravelHistoryModel travelHistoryModel = mTravelHistoryList.get(i);


    }

    @Override
    public int getItemCount() {
        return mTravelHistoryList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {



        public VehicleViewHolder(@NonNull View v) {
            super(v); }
    }
}
