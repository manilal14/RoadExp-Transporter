package com.road.roadexp_transporter.Reports.TravelReport.Vehicle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Reports.TravelReport.TravelHistoryAdapter;
import com.road.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;
import com.road.roadexp_transporter.Review.Vehicle;

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

public class TravelHistoryVehicle extends AppCompatActivity {

    private List<TravelHistoryModel> mTravelHistoryList;
    private Vehicle mVehicle;
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history_vehicle);

        mVehicle = (Vehicle) getIntent().getSerializableExtra("vehicle_detail");
        mProgressBar = findViewById(R.id.progress_bar);

        mTravelHistoryList = new ArrayList<>();
        fetchVehicleTravelHistory();

        clickListener();
    }

    private void clickListener() {
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void fetchVehicleTravelHistory() {

        Log.e(TAG,"called :  fetchVehicleTravelHistory");
        mProgressBar.setVisibility(View.VISIBLE);

        String URL = BASE_URL + "getHistoryTrip_ve";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG,response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if(code!=2){
                                Toast.makeText(TravelHistoryVehicle.this,"code is not 2", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray result = jsonObject.getJSONArray("result");

                            for(int i=0;i<result.length();i++){

                                JSONObject obj = result.getJSONObject(i);

                                int loadId            = obj.getInt("load_id");
                                String date           = obj.getString("time").substring(0,10);
                                String load_type      = obj.getString("type");
                                String amount         = obj.getString("amount");
                                String vehicleType    = obj.getString("vehicle_type");
                                String pickupLocation = obj.getString("pickup_location");
                                String city           = obj.getString("city");
                                String lastPoint      = obj.getString("last_point");
                                String destCity       = obj.getString("dest_city");
                                String weight         = obj.getString("weight");
                                String intermediateLoc = obj.getString("intermediate_loc");
                                String driverName      = mVehicle.getMappedDriver();

                                mTravelHistoryList.add(new TravelHistoryModel(loadId,date,load_type,amount, vehicleType, pickupLocation,
                                        city, lastPoint, destCity, weight, intermediateLoc, driverName));
                            }

                            if(mTravelHistoryList.size() == 0)
                                findViewById(R.id.no_history).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.no_history).setVisibility(View.GONE);

                            RecyclerView recyclerView = findViewById(R.id.recycler_view_travel_history);
                            TravelHistoryAdapter adapter = new TravelHistoryAdapter(TravelHistoryVehicle.this,mTravelHistoryList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(TravelHistoryVehicle.this));
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                            Toast.makeText(TravelHistoryVehicle.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG,error.toString());
                Toast.makeText(TravelHistoryVehicle.this,error.toString(), Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(TravelHistoryVehicle.this).addToRequestQueue(stringRequest);

    }
}
