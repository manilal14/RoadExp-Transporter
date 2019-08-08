package com.road.roadexp_transporter.VehicleDetailPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;
import com.road.roadexp_transporter.Review.Vehicle;
import com.road.roadexp_transporter.WrapContentHeightViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;


public class FragTravelDetails extends Fragment {

    private String TAG = "FragTravelDetails";
    private View mRoot;

    private Vehicle mVehicleDetail;
    private ProgressBar mProgressBar;
    private List<TravelHistoryModel> mTravelHistoryList;

    public FragTravelDetails() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        mRoot = inflater.inflate(R.layout.frag_travel_details, container, false);

        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        mProgressBar = mRoot.findViewById(R.id.progress_bar);
        mTravelHistoryList = new ArrayList<>();

        ((TextView)mRoot.findViewById(R.id.vehicle_type)).setText(mVehicleDetail.getVehicleType());
        ((TextView)mRoot.findViewById(R.id.vehicle_number)).setText(mVehicleDetail.getPlateNumber());

        fetchVehicleTravelHistory(mVehicleDetail.getVehicleId());
        clickListener();
        return mRoot;
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

    private void fetchVehicleTravelHistory(final int vehicleId) {

        Log.e(TAG,"called : fetchVehicleTravelHistory");
        mProgressBar.setVisibility(View.VISIBLE);

        String URL = BASE_URL + "getHistoryTrip_ve";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG,response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if(code!=2){
                                Toast.makeText(getActivity(),"code is not 2", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray result = jsonObject.getJSONArray("result");

                            for(int i=0;i<result.length();i++){

                                JSONObject obj = result.getJSONObject(i);

                                int loadId            = obj.getInt("load_id");
                                String date           = obj.getString("time").substring(0,10);
                                String load_type      = obj.getString("type");
                                String amount         = obj.getString("amount");
                                String vehicleType    = obj.getString("vehicle_type");
                                String pickupLocation = obj.getString("pickup_location");
                                String city           = obj.getString("city");
                                String lastPoint      = obj.getString("last_point");
                                String destCity       = obj.getString("dest_city");
                                String weight         = obj.getString("weight");
                                String intermediateLoc = obj.getString("intermediate_loc");
                                String driverName = "N/A";

                                mTravelHistoryList.add(new TravelHistoryModel(loadId,date,load_type,amount, vehicleType, pickupLocation,
                                        city, lastPoint, destCity, weight, intermediateLoc, driverName));
                            }

                            WrapContentHeightViewPager viewPager = mRoot.findViewById(R.id.viewpager_travel_detail);


                            if(mTravelHistoryList.size() == 0) {
                                viewPager.setVisibility(View.GONE);
                                mRoot.findViewById(R.id.no_travel_detail).setVisibility(View.VISIBLE);
                            }
                            else {
                                viewPager.setVisibility(View.VISIBLE);
                                mRoot.findViewById(R.id.no_travel_detail).setVisibility(View.GONE);
                            }

                            PagerAdapter adapter = new TravelDetailPagerAdapter(getActivity(),mTravelHistoryList);
                            viewPager.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG,error.toString());
                Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("vid", String.valueOf(vehicleId));
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

}
