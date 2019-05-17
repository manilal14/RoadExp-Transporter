package com.example.roadexp_transporter.VehiclePackage.UnverifiedVehicle;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.AddNewVehicle.AddVehicleHomePage;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.DriverPackage.Unverified.UnverifiedDriver;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.FragMoving;
import com.example.roadexp_transporter.VehiclePackage.FragOnWait;
import com.example.roadexp_transporter.VehiclePackage.FragTurnedOff;
import com.example.roadexp_transporter.VehiclePackage.VehicleFragmentPagerAdapter;
import com.example.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class UnverifiedVehiclePage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private List<Fragment> mFragmentList;
    private ArrayList<Vehicle> mAllUnverifiedVehicles;

    private ProgressBar mProgressBar;
    private LoginSessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_unverified_vehicle);
        mProgressBar = findViewById(R.id.progress_bar);

        mSession = new LoginSessionManager(UnverifiedVehiclePage.this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAllUnverifiedVehicles = new ArrayList<>();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragVehicleUnverified());
        mFragmentList.add(new FragVehicleNotMapped());

        clickListerner();
        fetchAllUnverifiedVehicle();
    }

    private void fetchAllUnverifiedVehicle() {

        Log.e(TAG, "called : fetchAllUnverifiedVehicle");
        String URL = BASE_URL + "showvehicle";

        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(UnverifiedVehiclePage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo   = jsonResult.getJSONObject(i);

                        int verifyFlag  = jo.getInt("verif_flag");

                        verifyFlag = 0;

                        if(verifyFlag != 1)
                            continue;

                        int active = jo.getInt("active");


                        String t_av_str  = jo.getString("t_av");
                        String d_av_str = jo.getString("d_av");


                        int vehicleId       = jo.getInt("v_id");
                        String vehicleType  = jo.getString("type_name");
                        String insuranceNum = jo.getString("insurance_num");
                        String plateNumber  = jo.getString("v_num");

                        String mappedDriver = "na";

                        String picRcFront   = jo.getString("pic_rc_front");
                        String picRcBack    = jo.getString("pic_rc_back");
                        String picVehicle   = jo.getString("pic_v");

                        int status;

                        if(t_av_str.equals("null") || d_av_str.equals("null"))
                            status = 2;
                        else
                            status = 1;

                        mAllUnverifiedVehicles.add(new Vehicle(vehicleId, vehicleType,insuranceNum, plateNumber, mappedDriver,
                                picRcFront, picRcBack, picVehicle,status));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

                ViewPager viewPager = findViewById(R.id.viewpager_vehicle);
                TabLayout tabLayout = findViewById(R.id.tabs_vehilce);
                tabLayout.setupWithViewPager(viewPager);

                UnverifiedVehicleFragmentPagerAdapter adapter = new UnverifiedVehicleFragmentPagerAdapter(
                        getSupportFragmentManager(),mFragmentList);

                viewPager.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params =  new HashMap<>();
                String transId = mSession.getTransporterDetailsFromPref().get(TRANS_ID);
                //Log.e(TAG, "transId="+transId);
                params.put("trans",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(UnverifiedVehiclePage.this).addToRequestQueue(stringRequest);

    }

    public List<Vehicle> fetchParticularUnverifiedVehicle(int status){

        Log.e(TAG, "called :  fetchParticularUnverifiedVehicle"+ "with id "+status);

        List<Vehicle> vehicleList = new ArrayList<>();
        for(int i=0;i<mAllUnverifiedVehicles.size();i++) {
            Vehicle vehicle  = mAllUnverifiedVehicles.get(i);
            if(vehicle.getStatus() == status)
                vehicleList.add(vehicle);
        }
        return vehicleList;
    }


    private void clickListerner() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
