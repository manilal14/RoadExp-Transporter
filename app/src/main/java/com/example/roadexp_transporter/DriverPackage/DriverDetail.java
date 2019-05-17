package com.example.roadexp_transporter.DriverPackage;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roadexp_transporter.R;

import org.w3c.dom.Text;

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

        ImageView iv_profile_ = findViewById(R.id.profile_pic);

        TextView tv_name = findViewById(R.id.name);
        TextView tv_mobile = findViewById(R.id.mobile);
        TextView tv_age = findViewById(R.id.age);
        TextView tv_join_date = findViewById(R.id.joining_date);

        TextView tv_trip = findViewById(R.id.no_of_successful_trip);
        TextView tv_mapped = findViewById(R.id.mapped_status);
        TextView tv_v_type = findViewById(R.id.vehicle_type);


        CardView cv_dl = findViewById(R.id.driver_licence);
        CardView tv_rd = findViewById(R.id.release_driver);
        CardView tv_sd = findViewById(R.id.suspend_driver);


        tv_name.setText(mDriver.getName());
        tv_mobile.setText(mDriver.getMobile());
        tv_age.setText(mDriver.getAgeInYear()+"years");
        tv_join_date.setText(mDriver.getJoingDate());
        tv_trip.setText(mDriver.getNoOfSuccesTrip());

        int vehicleId = mDriver.getVehicleId();
        if(vehicleId == 0)
            tv_mapped.setText("No");
        else
            tv_mapped.setText("Yes");

        tv_v_type.setText(mDriver.getVehicleName());



    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
