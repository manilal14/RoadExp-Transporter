package com.example.roadexp_transporter.LoginSingUp;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.NotificationPackage.AppHomePage;
import com.example.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;

public class LoginPage extends AppCompatActivity {

    private final String  TAG = this.getClass().getSimpleName();

    private ProgressBar mProgressBar;
    private LoginSessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        Log.e(TAG, "called onCreate");

        mProgressBar = findViewById(R.id.progress_bar);
        mSession     = new LoginSessionManager(LoginPage.this);

        clickListener();
    }

    private void clickListener() {

        final TextView tv_phone = findViewById(R.id.mobile);
        final TextView tv_pass   = findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone  = tv_phone.getText().toString().trim();
                String pass   = tv_pass.getText().toString().trim();

                if(phone.equals("") || pass.equals("")){
                    Toast.makeText(LoginPage.this, "Both fields are required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone.length()<10){
                    Toast.makeText(LoginPage.this, "Wrong mobile",Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyCredentialsFromDb(phone,pass);
            }
        });

        findViewById(R.id.singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, SignUpPage.class));
                finish();
            }
        });

        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this,"Google",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this,"fb",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyCredentialsFromDb(final String phone, final String password) {

        Log.e(TAG, "called : verifyCredentialsFromDb");

        String LOGIN_URL = BASE_URL + "login";
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG,response);

                mProgressBar.setVisibility(View.GONE);

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(LoginPage.this,"Wrong Credentials",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject jsonResult = jsonResponse.getJSONArray("result").getJSONObject(0);

                    String transId   = jsonResult.getString("id");
                    String transName = jsonResult.getString("name");
                    String transCity = jsonResult.getString("sahar");

                    //String transMob  = jsonResult.getString("mob");

                    String transState   = jsonResult.getString("state");
                    String transBankAcc = jsonResult.getString("bankAcc");

                    mSession.createLoginSession(transId,transName,password,phone,transCity,transState,transBankAcc);
                    startActivity(new Intent(LoginPage.this,AppHomePage.class));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"verifyCredentials : Exception cought  " + e );
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e("error",error.toString());
                Toast.makeText(LoginPage.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put("mob",phone);
                params.put("password",password);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(LoginPage.this).addToRequestQueue(stringRequest);

    }
}
