package com.road.roadexp_transporter.VehicleDetailPackage;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;
import com.road.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;


public class DialogDetailHomepage extends DialogFragment {

    private final String TAG = "DialogDetailHomePage";
    private View mRoot;
    public DialogDetailHomepage() {}

    private Vehicle mVehicleDetail;
    private ProgressBar mProgressBar;

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

        mRoot          = inflater.inflate(R.layout.dialog_detail_home_page, container, false);
        mVehicleDetail = (Vehicle) getArguments().getSerializable("vehicle_detail");
        mProgressBar   = mRoot.findViewById(R.id.progress_bar);

        Log.e(TAG, "vehicleType="+mVehicleDetail.getVehicleType());
        setViews();
        clickListener();
        return mRoot;
    }

    private void setViews() {

        CardView cv_curent_trip = mRoot.findViewById(R.id.current_trip);

        CardView cv_vehciel_det = mRoot.findViewById(R.id.vehicle_info);
        CardView cv_travel_det  = mRoot.findViewById(R.id.travel_details);
        CardView cv_map_det     = mRoot.findViewById(R.id.map_driver);
        CardView cv_release_det = mRoot.findViewById(R.id.release_driver);
        CardView cv_turn_off    = mRoot.findViewById(R.id.turn_off);

        TextView tv_travel_det  = mRoot.findViewById(R.id.tv_travel_details);
        TextView tv_map_det     = mRoot.findViewById(R.id.tv_map_driver);
        TextView tv_release_det = mRoot.findViewById(R.id.tv_release_driver);
        TextView tv_turn_off    = mRoot.findViewById(R.id.tv_turn_off);


        int status = mVehicleDetail.getStatus();

        switch (status){
            case 1:
                cv_curent_trip.setVisibility(View.VISIBLE);
                cv_map_det.setEnabled(false);
                cv_release_det.setEnabled(false);
                cv_turn_off.setEnabled(false);

                tv_map_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_turn_off.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                break;
            case 2:
                cv_curent_trip.setVisibility(View.GONE);
                cv_map_det.setEnabled(false);
                cv_release_det.setEnabled(false);

                tv_map_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                break;
            case 3:
                cv_curent_trip.setVisibility(View.GONE);
                break;

            case 4:
                cv_curent_trip.setVisibility(View.GONE);
                cv_travel_det.setEnabled(false);
                cv_release_det.setEnabled(false);
                cv_turn_off.setEnabled(false);

                tv_travel_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_turn_off.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));

                break;

            case 5:
                cv_curent_trip.setVisibility(View.GONE);
                cv_travel_det.setEnabled(false);
                cv_map_det.setEnabled(false);
                cv_release_det.setEnabled(false);
                cv_turn_off.setEnabled(false);

                tv_travel_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_map_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_release_det.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));
                tv_turn_off.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_dark_grey));

                break;
        }

    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        mRoot.findViewById(R.id.current_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragCurrentTripDetails frag =  new FragCurrentTripDetails();
                replaceFragment(frag);
            }
        });

        mRoot.findViewById(R.id.vehicle_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragVehicleInfo frag = new FragVehicleInfo();
                replaceFragment(frag);
            }
        });

        mRoot.findViewById(R.id.travel_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragTravelDetails fragTravelDetails =  new FragTravelDetails();
                replaceFragment(fragTravelDetails);
            }
        });

        mRoot.findViewById(R.id.map_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragMapDriver());
            }
        });

        mRoot.findViewById(R.id.release_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Realese Driver", Toast.LENGTH_SHORT).show();
            }
        });

        mRoot.findViewById(R.id.turn_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffDriver(mVehicleDetail.getVehicleId(), mVehicleDetail.getDriverId());
            }
        });
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


    private void turnOffDriver(final int vehicleId, final String driverId){

        Log.e(TAG, "called : turnOffDriver");

        String URL = BASE_URL + "turnOff";
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
                    Toast.makeText(getActivity(),"Vehicle turned Off",Toast.LENGTH_SHORT).show();
                    onDestroyView();
                    ((VehicleStatusHomePage)getActivity()).fetchAllVehicle();

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
                params.put("v_id", String.valueOf(vehicleId));
                params.put("Did",driverId);
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
