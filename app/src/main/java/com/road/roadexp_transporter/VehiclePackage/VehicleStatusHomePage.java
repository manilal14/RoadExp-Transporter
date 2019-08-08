package com.road.roadexp_transporter.VehiclePackage;

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
import com.road.roadexp_transporter.AddNewVehicle.AddVehicleHomePage;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.road.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class VehicleStatusHomePage extends AppCompatActivity{

    private final String TAG = this.getClass().getSimpleName();

    private List<Fragment> mFragmentList;
    private ArrayList<Vehicle> mAllVehicles;

    private ProgressBar mProgressBar;
    private LoginSessionManager mSession;

    private ViewPager mViewPager;
    private VehicleFragmentPagerAdapter mAdapter;
    private int mViewPagerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_vehicle_status_page);
        mProgressBar = findViewById(R.id.progress_bar);

        mViewPagerId = getIntent().getIntExtra("viewpagerId",0);

        mSession = new LoginSessionManager(VehicleStatusHomePage.this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAllVehicles = new ArrayList<>();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragMoving());
        mFragmentList.add(new FragOnWait());
        mFragmentList.add(new FragTurnedOff());

        mViewPager          = findViewById(R.id.viewpager_vehicle);
        TabLayout tabLayout = findViewById(R.id.tabs_vehilce);
        tabLayout.setupWithViewPager(mViewPager);

        mAdapter = new VehicleFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        //Set adapter only after fetching data
        //mViewPager.setAdapter(mAdapter);

        clickListerner();
        fetchAllVehicle();
        
    }

    public void fetchAllVehicle() {

        Log.e(TAG, "called : fetchAllVehicle");
        String URL = BASE_URL + "showvehicle";

        mAllVehicles.clear();

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
                        Toast.makeText(VehicleStatusHomePage.this,"code is not 1",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo   = jsonResult.getJSONObject(i);

                        int verifyFlag  = jo.getInt("verif_flag");
                        String did      = jo.getString("Did");

                        String statusFromdb = jo.getString("status");

                        if(verifyFlag == 0 || did.equals("null"))
                            continue;

                        String tripId  = jo.getString("trip_id");
                        String t_av    = jo.getString("t_av");
                        String d_av    = jo.getString("d_av");

                        int status;
                        if(!tripId.equals("null") && statusFromdb.equals("1"))
                            status = 1;
                        else if(t_av.equals("0") && d_av.equals("0"))
                            status = 2;
                        else
                            status = 3;

                        int vehicleId       = jo.getInt("v_id");
                        String plateNumber  = jo.getString("v_num");
                        String picRcFront   = jo.getString("pic_rc_front");
                        String picRcBack    = jo.getString("pic_rc_back");
                        String picVehicle   = jo.getString("pic_v");
                        String insuranceNum = jo.getString("insurance_num");
                        String addDate      = jo.getString("add_date");

                        String permitType   = jo.getString("permit_type");
                        String rcExpOn      = jo.getString("rc_exp");
                        String vehicleType  = jo.getString("type_name");

                        String dimension    = jo.getString("dimension");
                        String capacity     = jo.getString("capacity");
                        String mappedDriver = jo.getString("d_name");

                        mAllVehicles.add(new Vehicle(vehicleId, plateNumber,picRcFront,picRcBack,picVehicle,insuranceNum, addDate,
                                permitType,rcExpOn,vehicleType, dimension, capacity, mappedDriver, verifyFlag, did, tripId,status));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    Toast.makeText(VehicleStatusHomePage.this, e.toString(),Toast.LENGTH_SHORT).show();

                }

                setViewPager();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
                Toast.makeText(VehicleStatusHomePage.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params =  new HashMap<>();

                String transId = mSession.getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, "trans="+transId);
                params.put("trans",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(VehicleStatusHomePage.this).addToRequestQueue(stringRequest);

    }

    //After all vehicle is fetched
    private void setViewPager() {
        mAdapter = new VehicleFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mViewPagerId);
    }

    public List<Vehicle> fetchParticularVehicle(int status){

        Log.e(TAG, "called :  fetchParticularVehicle "+ "with id "+status);

        List<Vehicle> vehicleList = new ArrayList<>();
        for(int i=0;i<mAllVehicles.size();i++) {
            Vehicle vehicle  = mAllVehicles.get(i);
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

        findViewById(R.id.add_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleStatusHomePage.this, AddVehicleHomePage.class));
            }
        });

    }
}
