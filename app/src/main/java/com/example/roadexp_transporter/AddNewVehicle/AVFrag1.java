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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.AddNewDriver.ADFrag2;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.BASE;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class AVFrag1 extends Fragment {

    private String TAG = "AVFrag1";
    private View mRoot;

    private AddVehicleHomePage mActivity;

    private ProgressDialog mProgressDialog;

    ArrayList<String> mVehicleList;



    public AVFrag1() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddVehicleHomePage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.avfrag1, container, false);
        mActivity.translateTruck(0);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);
        mVehicleList = new ArrayList<>();

        fetchVehicleType();
        fetchPermitType();



        clickListener();
        return mRoot;
    }

    private void clickListener() {

        final EditText et_vehicle_number    = mRoot.findViewById(R.id.vehicle_number);
        final EditText et_insurance_number  = mRoot.findViewById(R.id.insurance_number);
        final Spinner spinVehicleType       = mRoot.findViewById(R.id.spinner_vehicle_type);
        final Spinner spinPermitType        = mRoot.findViewById(R.id.spinner_permit_type);


        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vehicleNumber = et_vehicle_number.getText().toString().trim();
                String insurance     = et_insurance_number.getText().toString().trim();

                if(vehicleNumber.equals("") || insurance.equals("")){
                    Toast.makeText(getActivity(),"All fields are required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spinVehicleType.getSelectedItem() == null){
                    Toast.makeText(getActivity(),"Vehicle Type is null",Toast.LENGTH_SHORT).show();
                    return;
                }

                String vehicleType = spinVehicleType.getSelectedItem().toString();
                String permitType  = spinPermitType.getSelectedItem().toString();



                Log.e(TAG, "asd= "+vehicleType +" "+permitType+" "+vehicleNumber+" "+insurance);

                sendVehicleDetail(vehicleType,permitType,vehicleNumber,insurance);



            }
        });

    }

    private void fetchVehicleType() {

        Log.e(TAG,"called : fetchVehicleType");
        mProgressDialog.show();

        String URL = BASE_URL + "vType";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Log.e(TAG,response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code != 1){
                                Toast.makeText(getActivity(),"Can't fetch vehicle type",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray result = jsonObject.getJSONArray("result");

                            for(int i=0;i<result.length();i++){
                                String vehicleType = result.getJSONObject(i).getString("type_name");
                                mVehicleList.add(vehicleType);

                            }

                            Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_vehicle_type);

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom,mVehicleList);
                            adapter.setDropDownViewResource(R.layout.spinner_layout_custom);
                            spiSelectDriver.setAdapter(adapter);

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
                Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void fetchPermitType() {

        Spinner spiPermitType = mRoot.findViewById(R.id.spinner_permit_type);

        ArrayList<String> permitList = new ArrayList<>();

        permitList.add("ALL INDIA");
        permitList.add("STATE");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, permitList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);
        spiPermitType.setAdapter(adapter);

    }

    private void sendVehicleDetail(final String vehicleType, final String permitType, final String vehicleNumber, final String insuranceNum){

        Log.e(TAG, "called : sendVehicleDetail");
        mProgressDialog.show();

        String URL = BASE_URL + "addVehiclesInfo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                Log.e(TAG,response);

                try {
                    int code = new JSONObject(response).getInt("code");
                    if(code!=1){
                        Toast.makeText(getActivity(),"Vehicle number already registered",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    replaceFragment(new AVFrag2(),"av_frag_2",vehicleNumber);

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

                String transId = new LoginSessionManager(getActivity()).getTransporterDetailsFromPref().get(TRANS_ID);

                parms.put("t_id",transId);
                parms.put("vehicle_num",vehicleNumber);
                parms.put("insurance_num",insuranceNum);
                parms.put("permit",permitType);
                parms.put("type",vehicleType);
                return parms;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void replaceFragment(Fragment fragment, String tag,String vehcileNum) {

        Bundle bundle = new Bundle();
        bundle.putString("vehicle_num",vehcileNum);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

}
