package com.example.roadexp_transporter.NotificationPackage;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roadexp_transporter.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

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
    public void onBindViewHolder(@NonNull NotiViewHolder h, int i) {

        Notification noti = mNotificationList.get(i);

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

        TextView tv_accept;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_accept = itemView.findViewById(R.id.accept);
        }
    }
}
