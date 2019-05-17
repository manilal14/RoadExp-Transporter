package com.example.roadexp_transporter.CommonForAll;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommanVariablesAndFunctuions {

    //values
    public static final int RETRY_SECONDS = 5*1000;
    public static final int NO_OF_RETRY = 0;



    public static final String BASE_URL     = "http://roadexp.codebuckets.in/admin_api/";
    public static final String OTP_AUTH_KEY = "172372AtvxExayuF3v5b5de27c";


    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String TIME_FORMAT  = "hh:mm a";

    // 06:45 PM
    public static String getFormattedTime(String TAG, String s) {

        String time = "NA";
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);

        try {
            Long unix   = Long.valueOf(s);
            date        = new Date(unix*1000L);
            time        = sdf.format(date);

        }catch (Exception e){
            Log.e(TAG,"Exception cought while converting time : "+e.toString());

        }
        return time;

    }

    public static long getUnixInSec(String TAG, String s) {
        
        Long unix = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        try {
            Date dt = sdf.parse(s);
            unix = dt.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return unix/1000L;
    }

    //Output - Thr 2 April
    //Input  - 14/02/1996 5:57 PM
    public static String getFormattedDate(String TAG, String s) {

        String result = "NA";
        Date date;

        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        DateFormat targetFormat = new SimpleDateFormat("EEE d MMM");

        try {
            date = originalFormat.parse(s);
            result = targetFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}













