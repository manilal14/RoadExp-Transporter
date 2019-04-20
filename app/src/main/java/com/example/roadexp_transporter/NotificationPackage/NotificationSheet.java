package com.example.roadexp_transporter.NotificationPackage;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roadexp_transporter.R;

public class NotificationSheet extends BottomSheetDialogFragment {

    private View mRoot;

    public NotificationSheet() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.bottom_sheet_dialog_accept_trip, container, false);
        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRoot.findViewById(R.id.add_new_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Add new Vehicle", Toast.LENGTH_SHORT).show();
            }
        });


        mRoot.findViewById(R.id.turn_on_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Turn on vehicle", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
