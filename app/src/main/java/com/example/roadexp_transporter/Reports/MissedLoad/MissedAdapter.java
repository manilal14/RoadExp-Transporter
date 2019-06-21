package com.example.roadexp_transporter.Reports.MissedLoad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.R;

import java.util.List;

public class MissedAdapter extends RecyclerView.Adapter<MissedAdapter.MissedViewHolder> {

    private Context mCtx;
    private List<MissedLoad> mMissedList;

    public MissedAdapter(Context mCtx, List<MissedLoad> mMissedList) {
        this.mCtx = mCtx;
        this.mMissedList = mMissedList;
    }

    @NonNull
    @Override
    public MissedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MissedViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_missed_load,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MissedViewHolder holder, int i) {
        MissedLoad missedLoad = mMissedList.get(i);


        holder.tv_date.setText(missedLoad.getDate());
        holder.tv_driver.setText(missedLoad.getDriverName());
        holder.tv_fromLine1.setText(missedLoad.getPickup());

        holder.tv_fromLine2.setText("");
        holder.tv_to_line1.setText(missedLoad.getLastPont());
        holder.tv_to_line2.setText("");

        holder.tv_weight.setText(missedLoad.getWeight());

        String s[] = missedLoad.getInterMediateLoc().split(",");

        if(s.length == 1)
            holder.tv_stops.setText(s.length+" stop");
        else
            holder.tv_stops.setText(s.length+" stops");
    }

    @Override
    public int getItemCount() {
        return mMissedList.size();
    }

    public class MissedViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_stops, tv_driver, tv_fromLine1, tv_fromLine2;
        TextView tv_to_line1, tv_to_line2, tv_weight;

        public MissedViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.date);
            tv_stops = itemView.findViewById(R.id.stops);
            tv_driver = itemView.findViewById(R.id.driver);
            tv_fromLine1 = itemView.findViewById(R.id.from_address_line1);
            tv_fromLine2 = itemView.findViewById(R.id.from_address_line2);
            tv_to_line1 = itemView.findViewById(R.id.to_address_line1);
            tv_to_line2= itemView.findViewById(R.id.to_address_line2);
            tv_weight = itemView.findViewById(R.id.weight);
        }
    }
}
