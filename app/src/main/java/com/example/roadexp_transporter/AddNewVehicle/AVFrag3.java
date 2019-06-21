package com.example.roadexp_transporter.AddNewVehicle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class AVFrag3 extends Fragment {

    private String TAG = "AVFrag3";
    private View mRoot;

    private AddVehicleHomePage mActivity;

    private ProgressDialog mProgressDialog;
    private String mVehicleNum;


    public AVFrag3() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddVehicleHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.avfrag3, container, false);
        mActivity.translateTruck(2);
        //fetchDriverList();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);

        mVehicleNum = getArguments().getString("vehicle_num");
        Log.e(TAG,mVehicleNum);

        clickListener();

        return mRoot;
    }

    private void clickListener() {

//        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               getActivity().onBackPressed();
//
//            }
//        });

        mRoot.findViewById(R.id.all_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFinalResponse();
            }
        });

    }

    private void sendFinalResponse() {

        Log.e(TAG, "called : sendFinalResponse");
        mProgressDialog.show();

        String URL = BASE_URL + "uflag";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                Log.e(TAG,response);

                try {
                    int code = new JSONObject(response).getInt("code");
                    if(code!=1){
                        Toast.makeText(getActivity(),"Code is not 1",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(),"Vehicle added successfully",Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e(TAG,error.toString());
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parms = new HashMap<>();
                parms.put("v_num",mVehicleNum);
                parms.put("u_flag","1");
                return parms;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    private void fetchDriverList() {

        Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_select_driver);

        ArrayList<String> driverLIst = new ArrayList<>();


        driverLIst.add("");
        driverLIst.add("Kedar Jadav");
        driverLIst.add("Virat Kohli");
        driverLIst.add("MS Dhoni");
        driverLIst.add("Yuvraj Singh");
        driverLIst.add("Saurav Ganguly");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, driverLIst);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiSelectDriver.setAdapter(adapter);

    }


}
