package com.road.roadexp_transporter.CommonForAll;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommanVariablesAndFunctuions {

    //values
    public static final int RETRY_SECONDS = 8*1000;
    public static final int NO_OF_RETRY = 0;

    //public static final String BASE_URL     = "http://roadexp.codebuckets.in/admin_api/";

    public static final String BASE_URL          = "https://rxp.api.codebuckets.in/admin_api/";
    private static final String BASE_IMGAES_PATH = "https://rxp.api.codebuckets.in/images/";

    public static final String BASE_PROFILE_PIC  = BASE_IMGAES_PATH +"profile/";
    public static final String BASE_AADHAR       = BASE_IMGAES_PATH +"aadhar_front_pic/";
    public static final String BASE_DL_FRONT     = BASE_IMGAES_PATH +"dl_pic_front/";
    public static final String BASE_DL_BACK      = BASE_IMGAES_PATH +"dl_pic_back/";
    public static final String BASE_VEHCHICLE_PIC= BASE_IMGAES_PATH +"vehicle/";
    public static final String BASE_RC_FRONT     = BASE_IMGAES_PATH +"rc_front/";
    public static final String BASE_RC_BACK      = BASE_IMGAES_PATH +"rc_back/";

    public static final String BASE_TRANS_PROFILE_PIC     = "https://admin.roadexp.codebuckets.in/images/prof_trans/";


    public static final String OTP_AUTH_KEY = "172372AtvxExayuF3v5b5de27c";

    public static final int GPS_REQUEST = 100;


    public static final String MY_DATE_FORMAT = "dd/MM/yyyy";

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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void callIntent(Context context, String phoneNumber){
        String phone = phoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);

    }


}













