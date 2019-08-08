package com.road.roadexp_transporter.LoginSingUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class SingnUpNext extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar mProgressBar;

    private List<String> mStateList;
    private List<Pair<Integer,String>> mCityList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_page);

        TextView textView = findViewById(R.id.text);
        String text = "<font color=#000000>Verify your</font> <font color=#F6685E>Identity</font>";
        textView.setText(Html.fromHtml(text));

        mProgressBar = findViewById(R.id.progress_bar);

        mStateList = new ArrayList<>();
        mCityList = new ArrayList<>();

        mStateList.add("Choose State");
      //  mCityList.add("Choose City");
        mCityList.add(new Pair(0,"Choose City"));

        fetchState();

        clickListener();
    }

    private void clickListener() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final TextView tv_pass    = findViewById(R.id.password);
        final TextView tv_Conpass = findViewById(R.id.confirm_password);
        final Spinner spinState   = findViewById(R.id.spinner_state);
        final Spinner spinCity    = findViewById(R.id.spinner_city);

        final EditText tv_bank_account = findViewById(R.id.bank_account);

        final EditText tv_ifsc        = findViewById(R.id.ifsc);
        final EditText tv_holder_name = findViewById(R.id.bank_account_holder_name);

        final EditText tv_fleetSize    = findViewById(R.id.fleet_size);

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


        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass      = tv_pass.getText().toString();
                String cnfPass   = tv_Conpass.getText().toString();

                String account   = tv_bank_account.getText().toString().trim();

                String ifsc          = tv_ifsc.getText().toString().trim();
                String holder_name   = tv_holder_name.getText().toString().trim();

                String fleetSize = tv_fleetSize.getText().toString().trim();

                int spinStateId = spinState.getSelectedItemPosition();
                int spinCityId  = spinCity.getSelectedItemPosition();

                if(spinCityId == 0 || spinStateId == 0){
                    Toast.makeText(SingnUpNext.this, "Choose State and City",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!verifyCredential(pass,cnfPass,account,fleetSize, ifsc, holder_name))
                    return;

                int cityId = mCityList.get(spinCityId).first;

                registerUser(pass,account,fleetSize, spinState.getSelectedItem().toString().trim(), String.valueOf(cityId),
                        ifsc, holder_name);

            }
        });

    }



    private boolean verifyCredential(String pass, String conPass, String account, String fleetSize, String ifse, String holderName) {

        if(pass.equals("") || conPass.equals("") || account.equals("") || fleetSize.equals("") || ifse.equals("")
                || holderName.equals("")){
            Toast.makeText(SingnUpNext.this,"All fields are required",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!pass.equals(conPass)){
            Toast.makeText(SingnUpNext.this,"Confirm Password is not matching",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(account.length()<9 || account.length()>18){
            Toast.makeText(SingnUpNext.this, "Wrong bank account number",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

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
                        Toast.makeText(SingnUpNext.this,"State can't be fetched",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++){
                        String state = jsonResult.getJSONObject(i).getString("state");
                        mStateList.add(state);
                    }

                    Spinner spiState = findViewById(R.id.spinner_state);
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(SingnUpNext.this, R.layout.spinner_layout_custom, mStateList);

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
        MySingleton.getInstance(SingnUpNext.this).addToRequestQueue(stringRequest);

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
                        Toast.makeText(SingnUpNext.this,"City can't be fetched",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++){
                        int id = jsonResult.getJSONObject(i).getInt("id");
                        //mCityList.add(city);
                        String city = jsonResult.getJSONObject(i).getString("city");
                        mCityList.add(new Pair(id, city));
                    }


                    Spinner spiCity = findViewById(R.id.spinner_city);

                    List<String> cityString = new ArrayList<>();
                    for (int i=0;i<mCityList.size();i++){
                        cityString.add(mCityList.get(i).second);
                    }


                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(SingnUpNext.this, R.layout.spinner_layout_custom, cityString);
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
        MySingleton.getInstance(SingnUpNext.this).addToRequestQueue(stringRequest);

    }




    private void registerUser(final String pass, final String account, final String fleetSize,
                              final String state, final String city, final String ifsc, final String holderName) {

        Log.e(TAG,"called : registerUser");

        mProgressBar.setVisibility(View.VISIBLE);

        final String name  = getIntent().getStringExtra("name");
        final String email = getIntent().getStringExtra("email");
        final String phone = getIntent().getStringExtra("phone");

//        final String name  = "name";
//        final String email = "manigmail";
//        final String phone = "123456789";

        String URL = BASE_URL + "addTrans";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int code = jsonObject.getInt("code");

                    String transId ="0";

                    if(code == 0){
                        Toast.makeText(SingnUpNext.this,"login failed",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(code == 1){
                        Toast.makeText(SingnUpNext.this,"Already registered!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else if(code == 2){
                        transId = jsonObject.getString("id");
                    }

                    new LoginSessionManager(SingnUpNext.this).createLoginSession(transId,name,pass,phone,city,state,account);
                    startActivity(new Intent(SingnUpNext.this, AddTransPic.class).putExtra("mob",phone));


                } catch (JSONException e) {
                    e.printStackTrace();
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

                params.put("name",name);
                params.put("email",email);
                params.put("mob",phone);
                params.put("password",pass);
                params.put("bankAcc",account);
                params.put("fleet_size",fleetSize);
                params.put("state",state);
                params.put("city",city);

                params.put("ifsc",ifsc);
                params.put("acc_name",holderName);

                return params;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(SingnUpNext.this).addToRequestQueue(stringRequest);

    }
}
