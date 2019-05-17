package com.example.roadexp_transporter.NotificationPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.AddNewVehicle.AddVehicleHomePage;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;

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

                Intent i = new Intent(getActivity(), AddVehicleHomePage.class);

                startActivity(i);
                dismiss();
            }
        });


        mRoot.findViewById(R.id.turn_on_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), VehicleStatusHomePage.class);
                i.putExtra("fragNumber",2);

                startActivity(i);
                dismiss();
            }
        });
    }
}
