package com.example.roadexp_transporter.Reports.TravelReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.R;

import java.util.List;

public class TravelHistoryAdapter extends RecyclerView.Adapter<TravelHistoryAdapter.VehicleViewHolder> {

    private Context mCtx;
    private List<TravelHistoryModel> mTravelHistoryList;
    private final String TAG = "TravelHistoryAdapter";

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
    public void onBindViewHolder(@NonNull final VehicleViewHolder holder, int i) {

        TravelHistoryModel model = mTravelHistoryList.get(i);

        holder.tv_weight.setText(model.getWeight()+"Tons");
        holder.tv_load_type.setText(model.getLoadType());

        holder.tv_driver.setText(model.getDriveName());
        holder.tv_fromLine1.setText(model.getPickupLoc());


        holder.tv_to_line1.setText(model.getLastPoint());

        Log.e("mno","bhai "+model.getIntermediateLoc());

        final String s[] = model.getIntermediateLoc().split("^");

        if(model.getIntermediateLoc().equals(""))
            holder.tv_stops.setText(0+" stops");
        else if( s.length == 1)
            holder.tv_stops.setText(s.length+" stop");
        else
            holder.tv_stops.setText(s.length+" stops");

        String interLoca = "";

        for(int k=0;k<s.length;k++)
            interLoca += s[k]+" ";

        final String finalInterLoca = interLoca;

        Log.e("fina",finalInterLoca);

        if(s.length==0)
            holder.tv_expand_inter.setVisibility(View.GONE);


        holder.tv_expand_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"clicked");
                if(s.length==0)
                    holder.tv_expand_inter.setVisibility(View.GONE);
                else{
                    holder.tv_expand_inter.setVisibility(View.VISIBLE);
                    holder.tv_inter_loc_list.setText(finalInterLoca);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mTravelHistoryList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_weight, tv_load_type, tv_stops;
        TextView tv_fromLine1;
        TextView tv_to_line1;
        TextView tv_driver;
        TextView tv_expand_inter;
        TextView tv_inter_loc_list;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_load_type = itemView.findViewById(R.id.load_type);
            tv_stops     = itemView.findViewById(R.id.stops);
            tv_driver    = itemView.findViewById(R.id.driver);
            tv_fromLine1 = itemView.findViewById(R.id.from_address_line1);

            tv_to_line1  = itemView.findViewById(R.id.to_address_line1);
            tv_weight    = itemView.findViewById(R.id.weight);
            tv_expand_inter = itemView.findViewById(R.id.expand_inter_loc);
            tv_expand_inter = itemView.findViewById(R.id.intermediate_list);

        }

    }
}
