package com.road.roadexp_transporter.LoginSingUp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.OTP_AUTH_KEY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;

public class SignUpPage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private EditText m_tv_otp;
   // private SmsVerifyCatcher smsVerifyCatcher;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_page);

        mProgressBar = findViewById(R.id.progress_bar);

//        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//
//                Log.e("asd1",message);
//
//                String code = parseCode(message);
//                Log.e("asd2",code);
//
//                m_tv_otp.setText(code);
//                m_tv_otp.setSelection(m_tv_otp.length());
//            }
//        });

        clickListener();
    }

    private void clickListener() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.rootview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"do nothing");
            }
        });

        final TextView tv_name, tv_email, tv_phone;

        tv_name  = findViewById(R.id.name);
        tv_email = findViewById(R.id.email);
        tv_phone = findViewById(R.id.phone);

        findViewById(R.id.singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(new Intent(SignUpPage.this, SingnUpNext.class));
//                startActivity(i);

                String name  = tv_name.getText().toString();
                String email = tv_email.getText().toString();
                String phone = tv_phone.getText().toString();

                if(!verifyCredentials(name, email,phone))
                    return;

                sendOtp(name,email,phone);

            }
        });


    }

    private void sendOtp(final String name, final String email, final String phone) {

        Log.e(TAG,"called : sendOtp");

        String OTP_URL  = "http://api.msg91.com/api/sendotp.php?";
        OTP_URL        += "authkey="+OTP_AUTH_KEY;
        OTP_URL        += "&mobile="+phone;
        OTP_URL        += "&otp_length=6";

        mProgressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, OTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, response);

                try {
                    String result = new JSONObject(response).getString("type");
                    if(result.equals("success") || result.equals("SMS sent by fallback")){
                        openDialog(name,email,phone);
                    }
                    else {
                        Toast.makeText(SignUpPage.this,result,Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpPage.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpPage.this,error.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "error = "+error.getMessage());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(SignUpPage.this).addToRequestQueue(stringRequest);



    }

    private void openDialog(final String name, final String email, final String phone) {

        Log.e(TAG,"called : openDialog");

        LayoutInflater li = LayoutInflater.from(SignUpPage.this);
        View otpView = li.inflate(R.layout.dialog_otp, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpPage.this);
        alertDialogBuilder.setView(otpView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        m_tv_otp = otpView.findViewById(R.id.otp);


        otpView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputedOtp = m_tv_otp.getText().toString().trim();

                if(inputedOtp.length()<6){
                    Toast.makeText(SignUpPage.this,"Enter correct otp",Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyOtp(name, email, phone,inputedOtp);
                alertDialog.dismiss();

            }
        });

//        m_tv_otp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count){}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(m_tv_otp.length() == 6) {
//                    String inputedOtp = m_tv_otp.getText().toString().trim();
//                    verifyOtp(name, email, phone,inputedOtp);
//                    alertDialog.dismiss();
//                }
//            }
//        });



    }

    private void verifyOtp(final String name, final String email , final String phone, String inputedOtp ) {

        Log.e(TAG, "called : verifyOtp");
        Log.e("asd3",inputedOtp);

        String VERIFY_OTP  = "http://api.msg91.com/api/verifyRequestOTP.php?";
        VERIFY_OTP         += "authkey="+OTP_AUTH_KEY;
        VERIFY_OTP         += "&mobile="+phone;
        VERIFY_OTP         += "&otp="+inputedOtp;

        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VERIFY_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, response);
                mProgressBar.setVisibility(View.GONE);
                try {
                    String result = new JSONObject(response).getString("type");
                    String message = new JSONObject(response).getString("message");

                    if(result.equals("success")){

                        Intent i = new Intent(new Intent(SignUpPage.this, SingnUpNext.class));

                        i.putExtra("name",name);
                        i.putExtra("email",email);
                        i.putExtra("phone",phone);

                        startActivity(i);
                    }
                    else {
                        Toast.makeText(SignUpPage.this,message,Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.getMessage());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( RETRY_SECONDS, NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(SignUpPage.this).addToRequestQueue(stringRequest);
    }



    @Override
    protected void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean verifyCredentials(String name, String email, String phone) {

        if(name.equals("") || email.equals("") || phone.equals("") ){
            Toast.makeText(SignUpPage.this,"All field are required",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(phone.length()<10){
            Toast.makeText(SignUpPage.this,"Invalid phone !",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!isEmailValid(email)){
            Toast.makeText(SignUpPage.this,"Invalid email !",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public static boolean isEmailValid(String email) {

//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();

        if(email.contains("@"))
            return true;

        return false;
    }

//    public String parseCode(String message) {
//        Pattern p = Pattern.compile("\\b\\d{6}\\b");
//        Matcher m = p.matcher(message);
//        String code = "";
//        while (m.find()) {
//            code = m.group(0);
//        }
//        return code;
//    }

}
