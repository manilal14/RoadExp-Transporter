package com.example.roadexp_transporter.LoginSingUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadexp_transporter.R;

public class SignUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_page);

        clickListener();
    }

    private void clickListener() {

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final TextView tv_name, tv_email, tv_phone;

        tv_name  = findViewById(R.id.name);
        tv_email = findViewById(R.id.email);
        tv_phone = findViewById(R.id.phone);

        findViewById(R.id.singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name  = tv_name.getText().toString();
                String email = tv_email.getText().toString();
                String phone = tv_phone.getText().toString();

                if(!verifyCredentials(name, email,phone))
                    return;

                sendOtp(name,email,phone);

            }
        });


    }


    private boolean verifyCredentials(String name, String email, String phone) {

        if(name.equals("") || email.equals("") || phone.equals("") ){
            Toast.makeText(SignUpPage.this,"All field are required",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(phone.length()<10){
            Toast.makeText(SignUpPage.this,"Invalid phone",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    private void sendOtp(String name, String email, String phone) {

        //After otp is send successfully

        Intent i = new Intent(new Intent(SignUpPage.this, OtpPage.class));

        i.putExtra("name",name);
        i.putExtra("email",email);
        i.putExtra("phone",phone);

        startActivity(i);
        finish();
    }


}
