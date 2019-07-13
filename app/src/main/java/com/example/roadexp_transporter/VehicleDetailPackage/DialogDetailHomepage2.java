package com.example.roadexp_transporter.VehicleDetailPackage;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;
import com.example.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;


public class DialogDetailHomepage2 extends DialogFragment {

    private final String TAG = "DialogDetailHomePage";
    private View mRoot;
    private Vehicle mVehicleDetail;
    private ProgressBar mProgressBar;


    public DialogDetailHomepage2() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E6FFFFFF")));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationSlideRightToRight;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG,"onCreateView");
        mRoot = inflater.inflate(R.layout.dialog_detail_home_page2, container, false);
        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        mProgressBar = mRoot.findViewById(R.id.progress_bar);
        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        mRoot.findViewById(R.id.vehicle_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragVehicleInfo());
            }
        });

        mRoot.findViewById(R.id.travel_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragTravelDetails());
            }
        });

        mRoot.findViewById(R.id.release_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForConfirmation(mVehicleDetail.getVehicleId(), mVehicleDetail.getDriverId());
            }
        });

        mRoot.findViewById(R.id.turn_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               turnOnDriver(mVehicleDetail.getVehicleId());
            }
        });


    }

    private void askForConfirmation(final int vehicleId, final String driverId) {

        final android.support.v7.app.AlertDialog alertDialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity(),android.
                    R.style.Theme_DeviceDefault_Light_Dialog_MinWidth).create();
        } else {
            alertDialog = new AlertDialog.Builder(getActivity()).create();
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_confirmation,null);

        alertDialog.setView(view);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView tv_title    = view.findViewById(R.id.title);
        TextView tv_message  = view.findViewById(R.id.message);

        tv_title.setText("Please Confirm!");
        tv_message.setText("Are you sure to release the Driver ?");

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseDriver(vehicleId, driverId);
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void releaseDriver(final int vehicleId, final String driverId) {

        Log.e(TAG, "called : releaseDriver");

        String URL = BASE_URL + "removeMapping";
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
                    Toast.makeText(getActivity(),"Driver is released.",Toast.LENGTH_SHORT).show();
                    onDestroyView();

                    ((VehicleStatusHomePage)getActivity()).fetchAllVehicle();

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
                params.put("vid", String.valueOf(vehicleId));
                params.put("Did", driverId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void replaceFragment(Fragment fragment) {

        fragment.setArguments(getArguments());

        FrameLayout f = mRoot.findViewById(R.id.fragment_container_details);
        f.getId();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container_details, fragment);
        ft.commit();

    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "OnDestroView1");
        super.onDestroyView();
        assert getFragmentManager() != null;
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    private void turnOnDriver(final int vehicleId){

        Log.e(TAG, "called : turnOnDriver");

        String URL = BASE_URL + "turnOn";
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
                    Toast.makeText(getActivity(),"Driver turned On",Toast.LENGTH_SHORT).show();
                    onDestroyView();
                    ((VehicleStatusHomePage)getActivity()).fetchAllVehicle();

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
                params.put("v_id", String.valueOf(vehicleId));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"resume called");
    }
}
