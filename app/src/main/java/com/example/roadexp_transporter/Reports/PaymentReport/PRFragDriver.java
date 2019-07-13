package com.example.roadexp_transporter.Reports.PaymentReport;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.Driver.TravelReportDriverAdapter;

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


public class PRFragDriver extends Fragment {

    private View mRootView;
    private List<Driver> mDriverList;
    private final String TAG = getClass().getSimpleName();
    private ProgressBar mProgressBar;

    public PRFragDriver() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView =  inflater.inflate(R.layout.prfrag_driver, container, false);
        mProgressBar = mRootView.findViewById(R.id.progress_bar);

        mDriverList = new ArrayList<>();
        fetchVerifiedDriver();
        return mRootView;
    }

    private void fetchDriverList(){

//        mDriverList.add(new Driver(1,"Anirudh Sai",1));
//        mDriverList.add(new Driver(2,"Krishn Rajak",1));
//        mDriverList.add(new Driver(3,"Archit",1));
//        mDriverList.add(new Driver(4,"Vishal Singh",1));
//        mDriverList.add(new Driver(5,"Ranjeet Kumar",1));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_payment_report_driver);

        PaymentReportDriverAdapter adapter = new PaymentReportDriverAdapter(getActivity(),mDriverList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

    private void fetchVerifiedDriver() {

        Log.e(TAG, "called : fetchVerifiedDriver");

        String URL = BASE_URL + "getDriver";
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    int code  = jsonResponse.getInt("code");

                    if(!(code==2)){
                        Toast.makeText(getActivity(),"code is not 2 ",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo = jsonResult.getJSONObject(i);

                        String vType  = jo.getString("v_type");
                        int verifFlag = jo.getInt("verif_flag");

                        if(verifFlag == 0)
                            continue;

                        String tripId = jo.getString("trip_id");
                        String t_av   = jo.getString("t_av");
                        String d_av   = jo.getString("d_av");

                        int status = 0;

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
                        //String tripId    = jo.getString("trip_id");


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

                        mDriverList.add(new Driver(driverId,name,mobile,sahar,state,ageInYear,joiningDate,noOfSuccessTrip,vType,
                                vNum,profPic,addharPic,dlFront,dlBack,t_av,d_av,status,1,vehicleId,bankAccd,tripId));
                    }

                    if(mDriverList.size() == 0)
                        mRootView.findViewById(R.id.no_driver).setVisibility(View.VISIBLE);
                    else
                        mRootView.findViewById(R.id.no_driver).setVisibility(View.GONE);

                    RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_payment_report_driver);
                    PaymentReportDriverAdapter adapter = new PaymentReportDriverAdapter(getActivity(),mDriverList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
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

                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, transId);
                params.put("transId",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
