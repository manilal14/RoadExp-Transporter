package com.example.roadexp_transporter.DriverPackage.Unverified;

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
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.DriverPackage.DriverFragmentPagerAdapter;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.VehiclePackage.UnverifiedVehicle.UnverifiedVehiclePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class UnverifiedDriver extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private List<Fragment> mFragmentList;

    private ProgressBar mProgressBar;
    private LoginSessionManager mSession;

    private List<Driver> mAllVerifiedDriverList;
    private int mCuurentViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_unverified_driver);

        mSession     = new LoginSessionManager(UnverifiedDriver.this);
        mProgressBar = new ProgressBar(UnverifiedDriver.this);

        mCuurentViewPager = getIntent().getIntExtra("currentViewPagerItem",0);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        mAllVerifiedDriverList = new ArrayList<>();

        mFragmentList = new ArrayList<>();

        mFragmentList.add(new FragNotMapped());
        mFragmentList.add(new FragNotVerified());


        clickListener();

        //TestingfetchAllDriver();
        fetchAllUnverifiedDriver();
    }

    //Only After fetching all driver fragments are loaded
    private void fetchAllUnverifiedDriver() {

        Log.e(TAG, "called : fetchAllUnverifiedDriver");

        String URL = BASE_URL + "getDriver";
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=2){
                        Toast.makeText(UnverifiedDriver.this,"Code is not 2",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo = jsonResult.getJSONObject(i);

                        int verifFlag = jo.getInt("verif_flag");
                        String v_type = jo.getString("v_type");

                        int status;

                        //status 4 - unmapped
                        //status 5 - unverified

                        if(verifFlag == 1 && v_type.equals("null"))
                            status = 4;
                        else
                            status = 5;

                        String vType      = jo.getString("v_type");
                        String t_av        = jo.getString("t_av");
                        String d_av        = jo.getString("d_av");

                        String driverId   = jo.getString("Did");
                        String name       = jo.getString("d_name");
                        String mobile     = jo.getString("phn");
                        String state      = jo.getString("state");

                        String addharPic = jo.getString("aadhar_front_pic");
                        String profPic   = jo.getString("prof_pic");
                        String dlFront   = jo.getString("dl_pic_front");
                        String dlBack    = jo.getString("dl_pic_back");
                        String birthday  = jo.getString("birthday");
                        String bankAccd  = jo.getString("bankAccd");
                        String sahar     = jo.getString("sahar");
                        String vehicleId = jo.getString("vehicle_id");

                        String vNum      = jo.getString("v_num");
                        String tripId    = jo.getString("trip_id");


                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        int yearBorn = 0;
                        try {
                            yearBorn = Integer.parseInt(birthday.substring(6,10));
                        }catch (Exception e){
                            Log.e(TAG,"Exception cought while birthdays");
                        }

                        int ageInYear = currentYear - yearBorn;
                        String joiningDate  = jo.getString("add_day").substring(0,10);

                        String noOfSuccessTrip = "N/A";

                        mAllVerifiedDriverList.add(new Driver(driverId,name,mobile,sahar,state,ageInYear,joiningDate,noOfSuccessTrip,vType,
                                vNum,profPic,addharPic,dlFront,dlBack,t_av,d_av,status,1,vehicleId,bankAccd,tripId));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

                ViewPager viewPager = findViewById(R.id.viewpager_driver);
                TabLayout tabLayout = findViewById(R.id.tabs_driver);
                tabLayout.setupWithViewPager(viewPager);

                UnVerifiedDriverFragmentPagerAdapter adapter = new UnVerifiedDriverFragmentPagerAdapter(
                        getSupportFragmentManager(),mFragmentList);

                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(mCuurentViewPager);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
                Toast.makeText(UnverifiedDriver.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params =  new HashMap<>();

                String transId = mSession.getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, transId);
                params.put("transId",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(UnverifiedDriver.this).addToRequestQueue(stringRequest);



    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public List<Driver> getDriverListFromParent(int driverStatus) {

        List<Driver> temp = new ArrayList<>();
        for (int i = 0; i < mAllVerifiedDriverList.size(); i++) {
            Driver driver = mAllVerifiedDriverList.get(i);
            Log.e(TAG,"status "+i+" "+driver.getStatus());
            if (driver.getStatus() == driverStatus)
                temp.add(driver);
        }
        return temp;
    }
}
