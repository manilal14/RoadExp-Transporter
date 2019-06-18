package com.example.roadexp_transporter.AddNewDriver;


import android.app.DatePickerDialog;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.LoginSingUp.LoginPage;
import com.example.roadexp_transporter.R;

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

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;

public class ADFrag1 extends Fragment {

    private String TAG = "ADFrag1";
    private View mRoot;

    private AddDriverPage mActivity;
    private ProgressBar mProgressBar;

    private List<String> mStateList;
    private List<Pair<Integer,String>> mCityList;

    //private List<String> mCityList;


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

        mProgressBar = mRoot.findViewById(R.id.progress_bar);

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
        final EditText tv_mobile       = mRoot.findViewById(R.id.mobile);
        final EditText tv_bank_account = mRoot.findViewById(R.id.bank_account);
        final EditText tv_password     = mRoot.findViewById(R.id.password);
        
        final Spinner spinState = mRoot.findViewById(R.id.spinner_state);
        final Spinner spinCity  = mRoot.findViewById(R.id.spinner_city);


        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name     = tv_name.getText().toString().trim();
                String birthday = tv_birthday.getText().toString().trim();
                String mobile   = tv_mobile.getText().toString().trim();
                String account  = tv_bank_account.getText().toString().trim();
                String password = tv_password.getText().toString().trim();

                int spinStateId = spinState.getSelectedItemPosition();
                int spinCityId  = spinCity.getSelectedItemPosition();


                    if(spinCityId == 0 || spinStateId == 0){
                        Toast.makeText(getActivity(), "Choose State and City",Toast.LENGTH_SHORT).show();
                        return;
                    }

                if(!verifyInput(name, birthday, mobile, account, password))
                    return;

                Bundle bundle = new Bundle();

                Log.e(TAG,"city="+spinCity.getSelectedItem().toString().trim());
                Log.e(TAG,"cityId="+mCityList.get(spinCityId).first);

                int cityId = mCityList.get(spinCityId).first;

                bundle.putString("name",name);
                bundle.putString("state",spinState.getSelectedItem().toString().trim());
                bundle.putString("city", String.valueOf(cityId));
                bundle.putString("birthday",birthday);
                bundle.putString("mobile",mobile);
                bundle.putString("account",account);
                bundle.putString("password",password);
                ADFrag2 frag = new ADFrag2();
                frag.setArguments(bundle);

                replaceFragment(frag,"ad_frag_2");
            }
        });

        mRoot.findViewById(R.id.birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDOBInEditText();
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

    private void sendDriverDetailToDb(final String name, final String state, final String city,
                                      final String birthday, final String mobile, final String account, final String password) {

        Log.e(TAG, "called : sendDriverDetailToDb");

        String URL = BASE_URL + "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> parms = new HashMap<>();

                parms.put("name",name);
                parms.put("state",state);
                parms.put("city",city);
                parms.put("birthday",birthday);

                parms.put("mobile",mobile);
                parms.put("account",account);
                parms.put("password",password);

                return parms;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);



    }


    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void fetchState(){

        Log.e(TAG, "called : fetchState");

        String STATE_URL = BASE_URL + "state";
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
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
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void fetchCity(final String state){

        Log.e(TAG, "called : fetchCity");
        String STATE_URL = BASE_URL + "city";

        mProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
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
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
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

    public void setDOBInEditText() {

        final Calendar calendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        new DatePickerDialog(getActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        Calendar calendar =  Calendar.getInstance();
        String date = sdf.format(calendar.getTime());

        EditText et_dob = mRoot.findViewById(R.id.birthday);
        et_dob.setText(date);
    }

    private boolean verifyInput(String name, String birthday, String mobile, String account, String password) {
        
        if(name.equals("") || birthday.equals("") || mobile.equals("") || account.equals("") || password.equals("")){
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



}
