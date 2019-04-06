package com.example.roadexp_transporter.VehicleDetailPackage;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.roadexp_transporter.R;

import java.util.Objects;


public class DialogDetailHomepage extends DialogFragment {

    private final String TAG = "DialogDetailHomePage";
    private View mRoot;
    public DialogDetailHomepage() {}

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

        mRoot = inflater.inflate(R.layout.dialog_detail_home_page, container, false);

        clickListener();

        return mRoot;
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
                replaceFragment(new FragVehicleInfo());
            }
        });

        mRoot.findViewById(R.id.travel_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragTravelDetails());
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

        FrameLayout f = mRoot.findViewById(R.id.fragment_container_details);
        f.getId();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container_details, fragment);
        ft.commit();

    }


//    private void openVehicleInfoDialog() {
//
//        AlertDialog.Builder vehicleInfoDialog =  new AlertDialog.Builder(getActivity());
//        vehicleInfoDialog.setView(getLayoutInflater().inflate(R.layout.fragVehicleInfo,null));
//
//        Window window = vehicleInfoDialog.show().getWindow();
//
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.RIGHT;
//
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.getAttributes().windowAnimations = R.style.animationSlideRightToRight;
//        window.setAttributes(wlp);
//
//    }


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
