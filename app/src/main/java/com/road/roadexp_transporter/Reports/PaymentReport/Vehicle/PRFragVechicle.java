package com.road.roadexp_transporter.Reports.PaymentReport.Vehicle;


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


public class PRFragVechicle extends Fragment {

    private View mRootView;
    private List<Vehicle> mVehicleList;
    private final String TAG = getClass().getSimpleName();
    private ProgressBar mProgressBar;

    public PRFragVechicle() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.prfrag_vechicle, container, false);
        mProgressBar = mRootView.findViewById(R.id.progress_bar);

        mVehicleList = new ArrayList<>();
        fetchVerifiedVehicleList();
        return mRootView;
    }


    private void fetchVerifiedVehicleList(){

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
                        Toast.makeText(getActivity(),"code is not 1",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++) {

                        JSONObject jo   = jsonResult.getJSONObject(i);

                        int verifyFlag  = jo.getInt("verif_flag");
                        String did      = jo.getString("Did");

                        if(verifyFlag == 0)
                            continue;

                        String tripId  = jo.getString("trip_id");
                        String t_av    = jo.getString("t_av");
                        String d_av    = jo.getString("d_av");

                        int status = 0;

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

                        String mappedDriver = "N/A";

                        mVehicleList.add(new Vehicle(vehicleId, plateNumber,picRcFront,picRcBack,picVehicle,insuranceNum, addDate,
                                permitType,rcExpOn,vehicleType, dimension, capacity, mappedDriver, verifyFlag, did, tripId,status));
                    }

                    if(mVehicleList.size() == 0)
                        mRootView.findViewById(R.id.no_vehicle).setVisibility(View.VISIBLE);
                    else
                        mRootView.findViewById(R.id.no_vehicle).setVisibility(View.GONE);



                    RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_payment_report_vehicle);
                    PaymentReportVehicleAdapter adapter = new PaymentReportVehicleAdapter(getActivity(),mVehicleList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
                Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params =  new HashMap<>();

                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, "transId="+transId);
                params.put("trans",transId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}
