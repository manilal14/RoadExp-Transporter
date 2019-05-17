package com.example.roadexp_transporter.DriverPackage;

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
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;

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

public class DriverHomepage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private List<Fragment> mFragmentList;

    private ProgressBar mProgressBar;
    private LoginSessionManager mSession;

    private List<Driver> mAllVerifiedDriverList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_driver_home_page);

        Log.e(TAG,"called :onCreate");

        mSession     = new LoginSessionManager(DriverHomepage.this);
        mProgressBar = new ProgressBar(DriverHomepage.this);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        mAllVerifiedDriverList = new ArrayList<>();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragDriverType1());
        mFragmentList.add(new FragDriverType2());
        mFragmentList.add(new FragDriverType3());

        clickListener();

        //TestingfetchAllDriver();
        fetchAllDriver();


    }

    //Only After fetching all driver fragments are loaded
    private void fetchAllDriver() {

        Log.e(TAG, "called : fetchAllDriver");

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

                    if(code!=1){
                        Toast.makeText(DriverHomepage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo = jsonResult.getJSONObject(i);
                        String vehicleId  = jo.getString("vehicle_id");

                        if(vehicleId.equals("null"))
                            continue;
                        int isVerified  = jo.getInt("verif_flag");
                        if(isVerified == 0)
                            continue;

                        String id       = jo.getString("Did");
                        String name     = jo.getString("d_name");
                        String mobile   = jo.getString("phn");
                        String city     = jo.getString("sahar");
                        String state    = jo.getString("state");

                        String birthday  = jo.getString("birthday");


                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        int yearBorn = 0;
                        try {
                            yearBorn = Integer.parseInt(birthday.substring(6,10));
                        }catch (Exception e){
                            Log.e(TAG,"Exception cought while birthdays");
                        }

                        int ageInYear = currentYear - yearBorn;


                        String joiningDate  = jo.getString("add_day").substring(0,10);

                        String noOfSuccesTrip = "na";
                        String vehicleName  = "vehicleName";
                        String vehicleNumber = "number";

                        String profilePic = jo.getString("prof_pic");
                        String dlPicFront  = jo.getString("dl_pic_front");
                        String dlPicBack  = jo.getString("dl_pic_back");

                        int t_av  = Integer.parseInt(jo.getString("t_av"));
                        int d_av  = Integer.parseInt(jo.getString("d_av"));

                        int trip = 1;

                        int status;
                        if(t_av == 0 && d_av == 0){
                            if(trip == 0)
                                status = 2;
                            else
                                status = 1;
                        }

                        else
                            status = 3 ;

                        mAllVerifiedDriverList.add(new Driver(id,name,mobile,city,state,ageInYear,joiningDate,noOfSuccesTrip,vehicleName,
                                vehicleNumber,profilePic,dlPicFront,dlPicBack,t_av,d_av,status,isVerified,Integer.parseInt(vehicleId)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

                ViewPager viewPager = findViewById(R.id.viewpager_driver);
                TabLayout tabLayout = findViewById(R.id.tabs_driver);
                tabLayout.setupWithViewPager(viewPager);

                DriverFragmentPagerAdapter adapter = new DriverFragmentPagerAdapter(
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
                Log.e(TAG, transId);
                params.put("transId",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(DriverHomepage.this).addToRequestQueue(stringRequest);



    }

    private void TestingfetchAllDriver(){

        // for testing only
        for(int i=1;i<=6;i++){

            String vehicleId = "1";
            int isVerified = 1;

            String id       = i+"";
            String name     = "mani"+i;
            String mobile   = "7907977801"+i;
            String city     = "sahar"+i;
            String state    = "state"+i;

            int ageInYear = +i;


            String joiningDate  = "joiningDate"+i;

            String noOfSuccesTrip = "na"+i;
            String vehicleName  = "vehicleName"+i;
            String vehicleNumber = "number";

            String profilePic = ""+i;
            String dlPicFront  = ""+i;
            String dlPicBack  = ""+i;

            int t_av = 0;
            int d_av  = 0;

            int status;

            if(i==1 || i == 2)
                status = 1;
            else if(i==3 || i==4)
                status = 2;
            else
                status = 3;

            mAllVerifiedDriverList.add(new Driver(id,name,mobile,city,state,ageInYear,joiningDate,noOfSuccesTrip,vehicleName,
                    vehicleNumber,profilePic,dlPicFront,dlPicBack,t_av,d_av,status,isVerified,Integer.parseInt(vehicleId)));
        }

        ViewPager viewPager = findViewById(R.id.viewpager_driver);
        TabLayout tabLayout = findViewById(R.id.tabs_driver);
        tabLayout.setupWithViewPager(viewPager);

        DriverFragmentPagerAdapter adapter = new DriverFragmentPagerAdapter(
                getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(adapter);

    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    //return driverList according to status

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
