package com.example.roadexp_transporter.DriverPackage;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.roadexp_transporter.R;


import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_AADHAR;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_DL_BACK;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_DL_FRONT;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_PROFILE_PIC;

public class DriverDetail extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Driver mDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_driver_detail);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        mDriver = (Driver) getIntent().getSerializableExtra("driver_detail");

        setData();
        clickListener();
    }

    private void setData() {

        final ImageView iv_profile = findViewById(R.id.profile_pic);

        TextView tv_name      = findViewById(R.id.name);
        TextView tv_mobile    = findViewById(R.id.mobile);
        TextView tv_age       = findViewById(R.id.age);
        TextView tv_join_date = findViewById(R.id.joining_date);

        TextView tv_trip      = findViewById(R.id.no_of_successful_trip);
        TextView tv_mapped    = findViewById(R.id.mapped_status);
        TextView tv_v_type    = findViewById(R.id.vehicle_type);


        final CardView cv_aadhar   = findViewById(R.id.pic_aadhar);
        CardView cv_rc_front = findViewById(R.id.pic_rc_front);
        CardView cv_rc_back  = findViewById(R.id.pic_rc_back);
        CardView tv_rd       = findViewById(R.id.release_driver);
        CardView tv_sd       = findViewById(R.id.suspend_driver);


        tv_name.setText(mDriver.getName());
        tv_mobile.setText(mDriver.getMobile());
        tv_age.setText(mDriver.getAgeInYear()+"years");
        tv_join_date.setText(mDriver.getJoingDate());
        tv_trip.setText(mDriver.getNoOfSuccesTrip());

        String vehicleId = mDriver.getVehicleId();
        if(vehicleId.equals("null"))
            tv_mapped.setText("No");
        else
            tv_mapped.setText("Yes");

        tv_v_type.setText(mDriver.getVehicleName());

        setProfilePic(BASE_PROFILE_PIC+mDriver.getProfilePic(),iv_profile);

        cv_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_AADHAR+mDriver.getAadharPic());
            }
        });

        cv_rc_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_DL_FRONT+mDriver.getDlPicFront());
            }
        });

        cv_rc_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageDialog(BASE_DL_BACK+mDriver.getDlPicBack());
            }
        });






    }

    private void setProfilePic(String imagePath, ImageView imageView) {

        Glide.with(DriverDetail.this)
                .load(imagePath)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(imageView);

    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setImageDialog(String imagePath){

        AlertDialog.Builder dialog = new AlertDialog.Builder(DriverDetail.this);
        View view = LayoutInflater.from(DriverDetail.this).inflate(R.layout.dialog_image_view, null);
        dialog.setView(view);

        ImageView imageView        = view.findViewById(R.id.imageView);
        final ProgressBar imageProgressBar = view.findViewById(R.id.image_progress_bar);

        Glide.with(DriverDetail.this)
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
