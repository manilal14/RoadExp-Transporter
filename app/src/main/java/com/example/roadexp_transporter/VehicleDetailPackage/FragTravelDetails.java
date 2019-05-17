package com.example.roadexp_transporter.VehicleDetailPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.roadexp_transporter.DriverPackage.DriverHomepage;
import com.example.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;


public class FragTravelDetails extends Fragment {

    private String TAG = "FragTravelDetails";
    private View mRoot;

    private int mVehicleId;
    private ProgressBar mProgressBar;

    public FragTravelDetails() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        mRoot = inflater.inflate(R.layout.frag_travel_details, container, false);

        mVehicleId = getArguments().getInt("vehicleId");
        Log.e(TAG, "VehicleId="+mVehicleId);

        mProgressBar = mRoot.findViewById(R.id.progress_bar);


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

    private void fetchTravelDetail(){

        Log.e(TAG, "called : fetchTravelDetail");

        //Need URL
        String URL = BASE_URL + "";

        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);


//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    int code = jsonResponse.getInt("code");
//
//                    if(code!=1){
//                        Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    JSONArray jsonResult = jsonResponse.getJSONArray("result");
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, e.toString());
//                }

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
                params.put("vehicleId", mVehicleId+"");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

}
