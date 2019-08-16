package com.road.roadexp_transporter.LoginSingUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class LoginSessionManager {

    private Context mCtx;
    private SharedPreferences pref;
    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor editor;

    private final String TAG = "LoginSessionManager";

    private final String PREF_NAME   = "LoginPreference";
    private final String IS_LOGIN    = "IsLoggedIn";
    private static final String ON_BOARDING_SHOWN    = "onBoardingShown";

    public static final String TRANS_ID         = "transId";
    public static final String TRANS_NAME       = "trans_name";
    public static final String TRANS_PASSWORD   = "transPass";
    public static final String TRANS_PHONE      = "trans_phone";

    public static final String TRANS_CITY       = "trans_city";
    public static final String TRANS_STATE      = "trans_state";
    public static final String TRANS_BANK_ACC   = "trans_bank_ac";


    public LoginSessionManager(Context context){
        mCtx = context;
        pref = mCtx.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String password, String phone, String city, String state, String bankAcc) {

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(TRANS_ID,id);
        editor.putString(TRANS_NAME,name);
        editor.putString(TRANS_PASSWORD,password);
        editor.putString(TRANS_PHONE,phone);

        editor.putString(TRANS_CITY,city);
        editor.putString(TRANS_STATE,state);
        editor.putString(TRANS_BANK_ACC,bankAcc);

        editor.commit();

        Log.e(TAG, "login session is created");
    }


    public void logout(){

        editor.clear();
        setOnBoardingShown();
        editor.commit();

        Intent i = new Intent(mCtx, LoginPage.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(mCtx,"Logged Out",Toast.LENGTH_SHORT).show();
        mCtx.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public HashMap<String,String> getTransporterDetailsFromPref(){

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(TRANS_ID, pref.getString(TRANS_ID, ""));
        user.put(TRANS_NAME, pref.getString(TRANS_NAME, ""));
        user.put(TRANS_PASSWORD, pref.getString(TRANS_PASSWORD, ""));
        user.put(TRANS_PHONE, pref.getString(TRANS_PHONE, ""));

        user.put(TRANS_CITY, pref.getString(TRANS_CITY, ""));
        user.put(TRANS_STATE, pref.getString(TRANS_STATE, ""));
        user.put(TRANS_BANK_ACC, pref.getString(TRANS_BANK_ACC, ""));


        return user;
    }

    public void setOnBoardingShown(){

        editor.putBoolean(ON_BOARDING_SHOWN, true);
        editor.commit();

    }
    public boolean onBoardingShown(){
        return pref.getBoolean(ON_BOARDING_SHOWN, false);
    }

}
