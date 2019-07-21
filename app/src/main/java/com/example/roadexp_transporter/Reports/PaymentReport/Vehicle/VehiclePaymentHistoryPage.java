package com.example.roadexp_transporter.Reports.PaymentReport.Vehicle;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.PaymentReport.Driver.DriverPaymentHistoryAdapter;
import com.example.roadexp_transporter.Reports.PaymentReport.PaymentHistoryModel;
import com.example.roadexp_transporter.Review.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;

public class VehiclePaymentHistoryPage extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private List<PaymentHistoryModel> mPaymentHistoryList;
    private Driver mDriver;
    private Vehicle mVehicle;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_payment_history_page);
        mProgressBar = findViewById(R.id.progress_bar);

        mPaymentHistoryList = new ArrayList<>();

        Log.e(TAG,"called for vehicle");
        mVehicle = (Vehicle) getIntent().getSerializableExtra("vehicle_detail");
        fetchPaymentHistoryForVehicle();

        clickListener();
    }

    private void fetchPaymentHistoryForDriver() {

        Log.e(TAG,"called : fetchPaymentHistory");
        mProgressBar.setVisibility(View.VISIBLE);

        String URL = BASE_URL + "payInfo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG,response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if(code!=1){
                                findViewById(R.id.no_payment_history).setVisibility(View.VISIBLE);
                                return;
                            }

                            JSONArray remainingArray = jsonObject.getJSONArray("remaining");
                            JSONArray infoArray      = jsonObject.getJSONArray("info");

                            int size = Math.min(remainingArray.length(), infoArray.length());

                            for(int i=0;i<size;i++){

                                JSONObject rem  = remainingArray.getJSONObject(i);
                                JSONObject info = infoArray.getJSONObject(i);

                                int tripId            = Integer.parseInt(rem.getString("trip_id"));
                                String remaining      = rem.getString("remaining");

                                String totalFare      = info.getString("total");
                                String driverCut      = info.getString("main");
                                String startDate      = info.getString("start_time").substring(0,11);
                                String pickupLocation = info.getString("pickup_location");
                                String interMob       = info.getString("inter_mob");
                                String lastPoint      = info.getString("last_point");

                                int comma = 0;
                                for(int j=0;j<interMob.length();j++){
                                    if(interMob.charAt(j) == ',')
                                        comma++;
                                }
                                int stops;
                                if(comma==0)
                                    stops =0;
                                else
                                    stops = comma+1;

                                mPaymentHistoryList.add(new PaymentHistoryModel(tripId,startDate,stops,pickupLocation,lastPoint,
                                        totalFare, driverCut,remaining, mDriver.getName()));
                            }

                            if(mPaymentHistoryList.size() == 0)
                                findViewById(R.id.no_payment_history).setVisibility(View.VISIBLE);


                            RecyclerView recyclerView = findViewById(R.id.recycler_view_payment_history);
                            DriverPaymentHistoryAdapter adapter = new DriverPaymentHistoryAdapter(VehiclePaymentHistoryPage.this,mPaymentHistoryList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(VehiclePaymentHistoryPage.this));
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG,error.toString());
                Toast.makeText(VehiclePaymentHistoryPage.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Did",mDriver.getId());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(VehiclePaymentHistoryPage.this).addToRequestQueue(stringRequest);
    }

    private void fetchPaymentHistoryForVehicle() {

        Log.e(TAG,"called : fetchPaymentHistory");
        mProgressBar.setVisibility(View.VISIBLE);

        String URL = BASE_URL + "payInfoVehicle";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG,response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if(code!=1){
                                findViewById(R.id.no_payment_history).setVisibility(View.VISIBLE);
                                return;
                            }

                            JSONArray result = jsonObject.getJSONArray("result");

                            for(int i=0;i<result.length();i++){

                                JSONObject res  = result.getJSONObject(i);

                                int tripId            = 0;
                                String remaining      = "0";

                                String totalFare      = res.getString("amount");
                                String driverCut      = "0";
                                String startDate      = res.getString("start_time").substring(0,11);
                                String pickupLocation = res.getString("pickup_location");
                                String interMob       = res.getString("inter_mob");
                                String lastPoint      = res.getString("last_point");

                                int comma = 0;
                                for(int j=0;j<interMob.length();j++){
                                    if(interMob.charAt(j) == ',')
                                        comma++;
                                }
                                int stops;
                                if(comma==0)
                                    stops =0;
                                else
                                    stops = comma+1;

                                //Driver will be null here
                                mPaymentHistoryList.add(new PaymentHistoryModel(tripId,startDate,stops,pickupLocation,lastPoint,
                                        totalFare, driverCut,remaining,"N/A"));


                            }

                            if(mPaymentHistoryList.size() == 0)
                                findViewById(R.id.no_payment_history).setVisibility(View.VISIBLE);


                            RecyclerView recyclerView = findViewById(R.id.recycler_view_payment_history);
                            VehiclePaymentHistoryAdapter adapter = new VehiclePaymentHistoryAdapter(VehiclePaymentHistoryPage.this,mPaymentHistoryList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(VehiclePaymentHistoryPage.this));
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG,error.toString());
                Toast.makeText(VehiclePaymentHistoryPage.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vid", String.valueOf(mVehicle.getVehicleId()));
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(VehiclePaymentHistoryPage.this).addToRequestQueue(stringRequest);
    }


    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
