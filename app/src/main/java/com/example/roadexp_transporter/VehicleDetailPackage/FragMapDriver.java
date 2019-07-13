package com.example.roadexp_transporter.VehicleDetailPackage;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
import com.example.roadexp_transporter.Reports.MissedLoad.MissedAdapter;
import com.example.roadexp_transporter.Reports.MissedLoad.MissedLoad;
import com.example.roadexp_transporter.Reports.MissedLoad.MissedLoadsPage;
import com.example.roadexp_transporter.Review.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.UnverifiedVehicle.UnverifiedVehiclePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class FragMapDriver extends Fragment {

    private String TAG = "FragTravelDetails";
    private View mRoot;
    private Vehicle mVehicleDetail;
    private ProgressBar mProgressBar;
    //driver pair containg ints id and name
    private List<Pair<String, String>> mDriverPairList;

    public FragMapDriver() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frag_map_driver, container, false);

        mProgressBar = mRoot.findViewById(R.id.progress_bar);
        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        mDriverPairList = new ArrayList<>();

        fetchUnMappedDriver();
        clickListener();
        return mRoot;
    }

    private void fetchUnMappedDriver() {

        Log.e(TAG, "called : fetchUnMappedDriver");

        String URL = BASE_URL + "mapdriver";
        mProgressBar.setVisibility(View.VISIBLE);

        mDriverPairList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        mProgressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if(code!=1){
                                Toast.makeText(getActivity(),"code is not 1",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray("result");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String driverId = obj.getString("Did");
                                String driverName = obj.getString("d_name");
                                mDriverPairList.add(new Pair(driverId,driverName));
                            }

                            Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_select_driver);

                            List<String> driverList = new ArrayList<>();
                            for(int i=0;i<mDriverPairList.size();i++)
                                driverList.add(mDriverPairList.get(i).second);

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, driverList);
                            adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

                            spiSelectDriver.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
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
                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, "trandId="+transId);
                params.put("tid",transId);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void clickListener() {

        ((TextView)mRoot.findViewById(R.id.vehicle_type)).setText(mVehicleDetail.getVehicleType());
        ((TextView)mRoot.findViewById(R.id.vehicle_number)).setText(mVehicleDetail.getPlateNumber());

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();

            }
        });

        mRoot.findViewById(R.id.whole_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Do nothing");
            }
        });

        mRoot.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_select_driver);

                if(mDriverPairList.size()==0){
                    Toast.makeText(getActivity(),"No driver to map",Toast.LENGTH_SHORT).show();
                    return;
                }

                int pos = spiSelectDriver.getSelectedItemPosition();
                String driverId = mDriverPairList.get(pos).first;

                Toast.makeText(getActivity(), "driverId="+driverId,Toast.LENGTH_SHORT).show();

                //Need to map Driver
                mapDriver(driverId,mVehicleDetail.getVehicleId());

            }
        });

    }

    private void mapDriver(final String driverId, final int vehicleId){

        Log.e(TAG, "called : mapDriver");

        String URL = BASE_URL + "mapdriver_code";
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
                        Toast.makeText(getActivity(),"Code is not 1",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(),"Driver is mapped",Toast.LENGTH_SHORT).show();
                    onDestroyView();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
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

                params.put("Did",driverId);
                params.put("vid", vehicleId+"");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

}
