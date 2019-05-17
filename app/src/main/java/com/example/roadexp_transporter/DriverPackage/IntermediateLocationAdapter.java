package com.example.roadexp_transporter.DriverPackage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roadexp_transporter.R;

import java.util.List;

public class IntermediateLocationAdapter extends RecyclerView.Adapter<IntermediateLocationAdapter.InterViewHolder> {

    private Context mCtx;
    private List<IntermediateLocation> mList;

    public IntermediateLocationAdapter(Context mCtx, List<IntermediateLocation> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public InterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InterViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_intermediate,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InterViewHolder h, int i) {

        final IntermediateLocation intermediate = mList.get(i);

        h.location.setText(intermediate.getLocation());

        h.mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(mCtx, intermediate.getPhone());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class InterViewHolder extends RecyclerView.ViewHolder {

        TextView location;
        ImageView mobile;

        public InterViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.location);
            mobile   = itemView.findViewById(R.id.mobile);

        }
    }

    public static void callIntent(Context context, String phoneNumber){

        String phone = phoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);

    }
}
