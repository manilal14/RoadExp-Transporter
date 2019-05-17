package com.example.roadexp_transporter.VehiclePackage;

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
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;

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

public class VehicleStatusHomePage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private List<Fragment> mFragmentList;
    private ArrayList<Vehicle> mAllVehicles;

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
        setContentView(R.layout.activity_vehicle_status_page);
        mProgressBar = findViewById(R.id.progress_bar);

        mSession = new LoginSessionManager(VehicleStatusHomePage.this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAllVehicles = new ArrayList<>();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragMoving());
        mFragmentList.add(new FragOnWait());
        mFragmentList.add(new FragTurnedOff());

        clickListerner();
        fetchAllVehicle();
        
    }

    private void fetchAllVehicle() {

        Log.e(TAG, "called : fetchAllVehicle");
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
                        Toast.makeText(VehicleStatusHomePage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo   = jsonResult.getJSONObject(i);

                        int verifyFlag  = jo.getInt("verif_flag");
                        verifyFlag = 1;

                        if(verifyFlag == 0)
                            continue;

                        int active = jo.getInt("active");
                        if(active == 1)
                            continue;

                        String t_av_str  = jo.getString("t_av");
                        String d_av_str = jo.getString("d_av");

                        if(t_av_str.equals("null") || d_av_str.equals("null"))
                            continue;

                        int t_av = Integer.parseInt(t_av_str);
                        int d_av = Integer.parseInt(d_av_str);


                        int vehicleId       = jo.getInt("v_id");
                        String vehicleType  = jo.getString("type_name");
                        String insuranceNum = jo.getString("insurance_num");
                        String plateNumber  = jo.getString("v_num");

                        String mappedDriver = "na";

                        String picRcFront   = jo.getString("pic_rc_front");
                        String picRcBack    = jo.getString("pic_rc_back");
                        String picVehicle   = jo.getString("pic_v");

                        int trip = 1;

                        int status;
                        if(t_av == 0 && d_av == 0){
                            if(trip == 0)
                                status = 2;
                            else
                                status = 1;
                        }
                        else
                            status = 1 ;

                        status = 3;


                        mAllVehicles.add(new Vehicle(vehicleId, vehicleType,insuranceNum, plateNumber, mappedDriver,
                                picRcFront, picRcBack, picVehicle,status));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

                ViewPager viewPager = findViewById(R.id.viewpager_vehicle);
                TabLayout tabLayout = findViewById(R.id.tabs_vehilce);
                tabLayout.setupWithViewPager(viewPager);

                VehicleFragmentPagerAdapter adapter = new VehicleFragmentPagerAdapter(
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
                Log.e(TAG, "transId="+transId);
                params.put("trans",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(VehicleStatusHomePage.this).addToRequestQueue(stringRequest);

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
