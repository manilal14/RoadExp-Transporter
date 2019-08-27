package com.road.roadexp_transporter.VehicleDetailPackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;

import java.util.List;

public class TravelDetailPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<TravelHistoryModel> mTravelList;

    public TravelDetailPagerAdapter(Context context, List<TravelHistoryModel> imagesList) {
        mContext = context;
        mTravelList =imagesList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mTravelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.recycler_view_travel_details, container, false);

        TravelHistoryModel travel = mTravelList.get(position);

        ((TextView)itemView.findViewById(R.id.date)).setText(travel.getDate());


        String s[] = travel.getPickupLoc().split(",");
        String pickupLocation = s[0];
        for(int i=1;i<s.length;i++) {
            pickupLocation += ",\n" + s[i];
        }

        String s2[] = travel.getLastPoint().split(",");
        String lastPoint = s2[0];
        for(int i=1;i<s2.length;i++) {
            lastPoint += ",\n" + s2[i];
        }


        ((TextView)itemView.findViewById(R.id.from)).setText(pickupLocation);
        ((TextView)itemView.findViewById(R.id.to)).setText(lastPoint);

        String intermediate = travel.getIntermediateLoc();
        //Log.e("asdf=",);


        ((TextView)itemView.findViewById(R.id.distance)).setText(intermediate);
        ((TextView)itemView.findViewById(R.id.driver)).setText(travel.getDriveName());
        ((TextView)itemView.findViewById(R.id.weight)).setText(travel.getWeight() +" kg");
        ((TextView)itemView.findViewById(R.id.amount)).setText("\u20b9 "+travel.getAmount());


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
