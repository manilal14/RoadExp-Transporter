package com.example.roadexp_transporter.LoginSingUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadexp_transporter.NotificationPackage.AppHomePage;
import com.example.roadexp_transporter.R;

public class OtpPage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_page);

        TextView textView = findViewById(R.id.text);
        String text = "<font color=#000000>Verify your</font> <font color=#F6685E>Identity</font>";
        textView.setText(Html.fromHtml(text));



        clickListener();
    }

    private void clickListener() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final EditText et1,et2,et3,et4,et5,et6;
        final TextView tv_pass    = findViewById(R.id.password);
        final TextView tv_Conpass = findViewById(R.id.confirm_password);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                    et2.requestFocus();

                else if(s.length()==0)
                    et1.clearFocus();
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                    et3.requestFocus();

                else if(s.length()==0)
                    et1.requestFocus();


            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                    et4.requestFocus();

                else if(s.length()==0)
                    et2.requestFocus();
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                    et5.requestFocus();

                else if(s.length()==0)
                    et3.requestFocus();


            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==1)
                    et6.requestFocus();
                else if(s.length()==0)
                    et4.requestFocus();
            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                    et5.requestFocus();
            }
        });




        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputedOtp = et1.getText().toString().trim()+
                        et2.getText().toString().trim()+
                        et3.getText().toString().trim()+
                        et4.getText().toString().trim()+
                        et5.getText().toString().trim()+
                        et6.getText().toString().trim();

                String pass    = tv_pass.getText().toString();
                String cnfPass = tv_Conpass.getText().toString();

                if(!verifyCredential(inputedOtp,pass,cnfPass))
                    return;


                registerUser(pass);
            }
        });

    }



    private boolean verifyCredential(String inputedOtp, String pass, String conPass) {

        if(inputedOtp.equals("") || pass.equals("") || conPass.equals("") ){
            Toast.makeText(OtpPage.this,"All fields are required",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!pass.equals(conPass)){
            Toast.makeText(OtpPage.this,"Confirm Password is not matching",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(inputedOtp.length()<6){
            Toast.makeText(OtpPage.this,"Enter valid otp",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!verifyOtp(inputedOtp)){
            Toast.makeText(OtpPage.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private boolean verifyOtp(String inputedOtp) {
        return true;
    }


    private void registerUser(String pass) {

        String name  = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");

        Log.e(TAG, name+" "+email+" "+phone + " "+pass);

        startActivity(new Intent(OtpPage.this, AppHomePage.class));
        finish();

    }
}
