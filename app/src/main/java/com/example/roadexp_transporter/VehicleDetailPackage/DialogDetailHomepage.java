package com.example.roadexp_transporter.VehicleDetailPackage;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;

import java.util.Objects;


public class DialogDetailHomepage extends DialogFragment {

    private final String TAG = "DialogDetailHomePage";
    private View mRoot;
    public DialogDetailHomepage() {}

    private Vehicle mVehicleDetail;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E6FFFFFF")));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationSlideRightToRight;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");

        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        mRoot = inflater.inflate(R.layout.dialog_detail_home_page, container, false);

        Log.e(TAG, "vehicleType="+mVehicleDetail.getVehicleType());
        setViews();
        clickListener();
        return mRoot;
    }

    private void setViews() {

        CardView cv_vehciel_det = mRoot.findViewById(R.id.vehicle_info);
        CardView cv_travel_det  = mRoot.findViewById(R.id.travel_details);
        CardView cv_map_det     = mRoot.findViewById(R.id.map_driver);
        CardView cv_release_det = mRoot.findViewById(R.id.release_driver);
        CardView cv_turn_off    = mRoot.findViewById(R.id.turn_off);

        TextView tv_travel_det  = mRoot.findViewById(R.id.tv_travel_details);
        TextView tv_map_det     = mRoot.findViewById(R.id.tv_map_driver);
        TextView tv_release_det = mRoot.findViewById(R.id.tv_release_driver);
        TextView tv_turn_off    = mRoot.findViewById(R.id.tv_turn_off);


        int status = mVehicleDetail.getStatus();

        switch (status){
            case 1:
            case 2:
                break;
            case 3:
                break;

            case 4:
                cv_map_det.setEnabled(false);
                cv_release_det.setEnabled(false);
                cv_turn_off.setEnabled(false);

                tv_map_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_turn_off.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));

                break;

            case 5:
                cv_travel_det.setEnabled(false);
                cv_map_det.setEnabled(false);
                cv_release_det.setEnabled(false);
                cv_turn_off.setEnabled(false);

                tv_travel_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_map_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_turn_off.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));

                break;
        }

    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        mRoot.findViewById(R.id.vehicle_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragVehicleInfo frag = new FragVehicleInfo();
               // frag.setArguments(getArguments()); //as it is set on next fin
                replaceFragment(frag);
            }
        });

        mRoot.findViewById(R.id.travel_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragTravelDetails fragTravelDetails =  new FragTravelDetails();
                Bundle bundle = new Bundle();
                bundle.putInt("vehicleId", mVehicleDetail.getVehicleId());
                fragTravelDetails.setArguments(bundle);

               replaceFragment(fragTravelDetails);
            }
        });

        mRoot.findViewById(R.id.map_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new FragMapDriver());
            }
        });

        mRoot.findViewById(R.id.release_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Realese Driver", Toast.LENGTH_SHORT).show();
            }
        });

        mRoot.findViewById(R.id.turn_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Turning off", Toast.LENGTH_SHORT).show();
            }
        });






    }

    private void replaceFragment(Fragment fragment) {

        fragment.setArguments(getArguments());

        FrameLayout f = mRoot.findViewById(R.id.fragment_container_details);
        f.getId();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container_details, fragment);
        ft.commit();

    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "OnDestroView1");
        super.onDestroyView();
        assert getFragmentManager() != null;
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

}
