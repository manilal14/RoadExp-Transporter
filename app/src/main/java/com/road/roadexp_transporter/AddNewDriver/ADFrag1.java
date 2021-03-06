package com.road.roadexp_transporter.AddNewDriver;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.MY_DATE_FORMAT;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;

public class ADFrag1 extends Fragment {

    private String TAG = "ADFrag1";
    private View mRoot;

    private AddDriverPage mActivity;
    private ProgressDialog mProgressDialog;

    private List<String> mStateList;
    private List<Pair<Integer,String>> mCityList;

    //private List<String> mCityList;

    private Calendar mCalendar;


    public ADFrag1() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddDriverPage) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.adfrag1, container, false);
        mActivity.translateBoy(0);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");

        mStateList = new ArrayList<>();
        mCityList = new ArrayList<>();

        mStateList.add("Choose State");
        mCityList.add(new Pair(0,"Choose City"));

        fetchState();
        clickListener();

        return mRoot;
    }



    private void clickListener() {

        final EditText tv_name         = mRoot.findViewById(R.id.name);
        final EditText tv_birthday     = mRoot.findViewById(R.id.birthday);
        final EditText tv_license_exp  = mRoot.findViewById(R.id.license_expire);
        final EditText tv_mobile       = mRoot.findViewById(R.id.mobile);
        final EditText tv_bank_account = mRoot.findViewById(R.id.bank_account);
        final EditText tv_ifse         = mRoot.findViewById(R.id.ifse);
        final EditText tv_holder_name  = mRoot.findViewById(R.id.bank_account_holder_name);
        final EditText tv_password     = mRoot.findViewById(R.id.password);
        
        final Spinner spinState = mRoot.findViewById(R.id.spinner_state);
        final Spinner spinCity  = mRoot.findViewById(R.id.spinner_city);


        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name          = tv_name.getText().toString().trim();
                String birthday      = tv_birthday.getText().toString().trim();
                String licenseExpire = tv_license_exp.getText().toString().trim();
                String mobile        = tv_mobile.getText().toString().trim();
                String account       = tv_bank_account.getText().toString().trim();
                String ifse          = tv_ifse.getText().toString().trim();
                String holderName    = tv_holder_name.getText().toString().trim();
                String password      = tv_password.getText().toString().trim();

                int spinStateId      = spinState.getSelectedItemPosition();
                int spinCityId       = spinCity.getSelectedItemPosition();

                if(spinCityId == 0 || spinStateId == 0){
                    Toast.makeText(getActivity(), "Choose State and City",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!verifyInput(name, birthday, licenseExpire, mobile, account,ifse,holderName, password))
                    return;

                Bundle bundle = new Bundle();

                Log.e(TAG,"city="+spinCity.getSelectedItem().toString().trim());
                Log.e(TAG,"cityId="+mCityList.get(spinCityId).first);

                int cityId = mCityList.get(spinCityId).first;

                bundle.putString("name",name);
                bundle.putString("state",spinState.getSelectedItem().toString().trim());
                bundle.putString("city", String.valueOf(cityId));
                bundle.putString("birthday",birthday);
                bundle.putString("license_exp",licenseExpire);
                bundle.putString("mobile",mobile);
                bundle.putString("account",account);

                bundle.putString("ifse",ifse);
                bundle.putString("holder_name",holderName);

                bundle.putString("password",password);

                ADFrag2 frag = new ADFrag2();
                frag.setArguments(bundle);

                replaceFragment(frag,"ad_frag_2");
            }
        });

        mRoot.findViewById(R.id.birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate((EditText) v);
            }
        });

        mRoot.findViewById(R.id.license_expire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate((EditText) v);
            }
        });


        spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = spinState.getSelectedItem().toString();
                Log.e(TAG, "state selected "+state);
                fetchCity(state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }


    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void fetchState(){

        Log.e(TAG, "called : fetchState");

        String STATE_URL = BASE_URL + "state";
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                Log.e(TAG, response);

                mStateList.clear();
                mStateList.add("Choose State");

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(getActivity(),"State can't be fetched",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++){
                        String state = jsonResult.getJSONObject(i).getString("state");
                        mStateList.add(state);
                    }

                    Spinner spiState = mRoot.findViewById(R.id.spinner_state);
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, mStateList);

                    adapter.setDropDownViewResource(R.layout.spinner_layout_custom);
                    spiState.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e(TAG, error.toString());
                Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void fetchCity(final String state){

        Log.e(TAG, "called : fetchCity");
        String STATE_URL = BASE_URL + "city";

        mProgressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                Log.e(TAG, response);

                mCityList.clear();
                mCityList.add(new Pair(0,"Choose City"));

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(getActivity(),"City can't be fetched",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++){
                        int id = jsonResult.getJSONObject(i).getInt("id");
                        String city = jsonResult.getJSONObject(i).getString("city");
                        mCityList.add(new Pair(id, city));
                    }


                    Spinner spiCity = mRoot.findViewById(R.id.spinner_city);

                    List<String> cityString = new ArrayList<>();
                    for (int i=0;i<mCityList.size();i++){
                        cityString.add(mCityList.get(i).second);
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom,cityString);

                    adapter.setDropDownViewResource(R.layout.spinner_layout_custom);
                    spiCity.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e(TAG, error.toString());
                Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("state", state);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    public void setDate(final EditText editText) {

        mCalendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }
        };
        new DatePickerDialog(getActivity(), date, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel(EditText editText) {
        SimpleDateFormat sdf = new SimpleDateFormat(MY_DATE_FORMAT, Locale.getDefault());

        String date = sdf.format(mCalendar.getTime());
        editText.setText(date);
    }

    private boolean verifyInput(String name, String birthday,String licenseExp, String mobile,
                                String account, String ifse, String holderName, String password) {
        
        if(name.equals("") || birthday.equals("") || licenseExp.equals("") || mobile.equals("") || account.equals("") ||
                ifse.equals("") || holderName.equals("") || password.equals("")){
            Toast.makeText(getActivity(), "All fields are required",Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if(mobile.length()<10){
            Toast.makeText(getActivity(), "Wrong mobile number",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(account.length()<9 || account.length()>18){
            Toast.makeText(getActivity(), "Wrong bank account number",Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if(password.length()<6){
            Toast.makeText(getActivity(), "Password is too small",Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
