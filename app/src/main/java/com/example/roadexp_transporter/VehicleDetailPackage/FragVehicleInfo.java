package com.example.roadexp_transporter.VehicleDetailPackage;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.roadexp_transporter.DriverPackage.DriverDetail;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;

import java.util.Objects;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_RC_BACK;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_RC_FRONT;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_VEHCHICLE_PIC;


public class FragVehicleInfo extends Fragment {

    private String TAG = "FragVehicleInfo";
    private View mRoot;

    private Vehicle mVehicleDetail;

    public FragVehicleInfo() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG,"onCreateView");
        mRoot =  inflater.inflate(R.layout.frag_vehicle_info, container, false);

        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        setViews();
        clickListener();
        return mRoot;
    }

    private void setViews() {

        TextView tv_vehcileType   = mRoot.findViewById(R.id.vehicle_type);
        TextView tv_vehicleNumber = mRoot.findViewById(R.id.vehicle_number);
        TextView tv_disTravel     = mRoot.findViewById(R.id.distance_travelled);
        TextView tv_mappedDriver  = mRoot.findViewById(R.id.mapped_driver);

        tv_vehicleNumber.setText(mVehicleDetail.getPlateNumber());
        tv_vehcileType.setText(mVehicleDetail.getVehicleType());
        tv_mappedDriver.setText(mVehicleDetail.getMappedDriver());



    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Backbutton is clicked");
                onDestroyView();

            }
        });

        mRoot.findViewById(R.id.whole_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Do nothing");
            }
        });

        mRoot.findViewById(R.id.vehicle_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_VEHCHICLE_PIC+mVehicleDetail.getPicVehicle());
            }
        });

        mRoot.findViewById(R.id.pic_rc_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_RC_FRONT+mVehicleDetail.getPicRcFront());
            }
        });

        mRoot.findViewById(R.id.pic_rc_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_RC_BACK+mVehicleDetail.getPicRcBack());
            }
        });
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "OnDestroView2");
        super.onDestroyView();
        assert getFragmentManager() != null;
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    private void setImageDialog(String imagePath){

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image_view, null);
        dialog.setView(view);

        ImageView imageView        = view.findViewById(R.id.imageView);
        final ProgressBar imageProgressBar = view.findViewById(R.id.image_progress_bar);

        Glide.with(getActivity())
                .load(imagePath)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

        dialog.show();
        dialog.create();



    }

}
