package com.example.roadexp_transporter.NotificationPackage;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roadexp_transporter.DriverPackage.IntermediateLocation;
import com.example.roadexp_transporter.DriverPackage.IntermediateLocationAdapter;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.getFormattedDate;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.getUnixInSec;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

    private final String TAG = "NotificationAdapter";

    private Context mCtx;
    private List<Notification> mNotificationList;



    public NotificationAdapter(Context mCtx, List<Notification> mNotificationList) {
        this.mCtx = mCtx;
        this.mNotificationList = mNotificationList;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotiViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_notification,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotiViewHolder h, int i) {

        Notification noti = mNotificationList.get(i);

        h.from.setText(noti.getPickupPoint());

        h.from2.setText("from2");
        h.to.setText(noti.getEndPoint());
        h.to2.setText("to2");

        h.vehicleType.setText(noti.getVehicleType()+"");

        h.dimention.setText(noti.getDimention());
        h.ordered_by.setText(noti.getOrderedBy());
        h.loadWeight.setText(noti.getLoadWeight());
        h.loadType.setText(noti.getLoadType());

        h.expireDate.setText(getFormattedDate(TAG, noti.getExpireOn()));
        h.amount.setText("\u20B9"+noti.getAmount());
        h.capacity.setText(noti.getCapacity()+" T");



        //Related to intermediate location
        String interPoints[] = noti.getIntermediatePoints().split(",");
        String interMobile[] = noti.getIntermediateMobile().split(",");

        int size;
        if(interMobile.length<interMobile.length)
            size = interMobile.length;
        else
            size = interPoints.length;

        if(size == 0)
            h.count.setVisibility(View.GONE);
        else
            h.count.setVisibility(View.VISIBLE);

        h.count.setText("+"+size);
        h.btn_intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(h.recyclerView.getVisibility() == View.VISIBLE)
                    h.recyclerView.setVisibility(View.GONE);

                else
                    h.recyclerView.setVisibility(View.VISIBLE);
            }
        });

        List <IntermediateLocation> locationsList = new ArrayList<>();

        for(int k=0;k<size;k++) {
            locationsList.add(new IntermediateLocation(interPoints[k], interMobile[k]));
        }

        IntermediateLocationAdapter adapter = new IntermediateLocationAdapter(mCtx,locationsList);
        h.recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        h.recyclerView.setAdapter(adapter);





        h.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationSheet notificationBSDF = new NotificationSheet();
                notificationBSDF.show(((AppCompatActivity)mCtx).getSupportFragmentManager(), notificationBSDF.getTag());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder {

        TextView from, from2, to, to2, vehicleType, dimention, ordered_by, loadWeight,loadType;
        TextView expireDate, amount;
        TextView capacity;

        RecyclerView recyclerView;
        TextView count;

        LinearLayout btn_intermediate;

        TextView tv_accept;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);

            from        = itemView.findViewById(R.id.from);
            from2       = itemView.findViewById(R.id.from2);
            to          = itemView.findViewById(R.id.to);
            to2         = itemView.findViewById(R.id.to2);
            vehicleType = itemView.findViewById(R.id.vehicle_type);
            dimention   = itemView.findViewById(R.id.dimention);
            ordered_by  = itemView.findViewById(R.id.ordered_by);

            loadWeight  = itemView.findViewById(R.id.load_weight);
            loadType    = itemView.findViewById(R.id.load_type);
            expireDate  = itemView.findViewById(R.id.expire_date);
            amount      = itemView.findViewById(R.id.amount);

            capacity    = itemView.findViewById(R.id.capacity);

            count            = itemView.findViewById(R.id.count);
            recyclerView     = itemView.findViewById(R.id.recycler_view_intermediate_point);
            btn_intermediate = itemView.findViewById(R.id.btn_intermediate);

            tv_accept   = itemView.findViewById(R.id.accept);
        }
    }


}
