package com.road.roadexp_transporter.MissedNotification;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.HomePage.Notification;
import com.road.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.road.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.getUnixInSec;

public class MissedNotificationPage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private List<Notification> mMissedNotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_notification_missed_page);

        mSwipeRefreshLayout      = findViewById(R.id.swipe_to_refresh);
        mMissedNotificationList  = new ArrayList<>();

        fetchMissedNotification();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMissedNotification();
            }
        });



        clickListener();
    }

    private void fetchMissedNotification() {

        Log.e(TAG, "called :fetchNotification ");
        String STATE_URL = BASE_URL + "sosnotification";
        mSwipeRefreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mSwipeRefreshLayout.setRefreshing(false);

                if(mMissedNotificationList.size()!=0)
                    mMissedNotificationList.clear();

                Log.e(TAG, response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(MissedNotificationPage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray jsonResult = jsonResponse.getJSONArray("result");

                    for(int i=0;i<jsonResult.length();i++){

                        JSONObject res = jsonResult.getJSONObject(i);

                        //show only if status and flag is 0
                        int status  = res.getInt("status");
                        int flag    = res.getInt("flag");

                        if(status != 0 || flag !=0)
                            continue;

                        int loadId = res.getInt("load_id");

                        String clientId        = res.getString("fromc");
                        String loadType         = res.getString("type");
                        String amount           = res.getString("amount");
                        String vehicleType      = res.getString("vehicle_type");

                        String pickupLocation   = res.getString("pickup_location");
                        String lastPoint        = res.getString("last_point");
                        String city             = res.getString("city");
                        String startMob         = res.getString("start_mob");
                        String endMob           = res.getString("end_mob");
                        String intermediate_loc = res.getString("intermediate_loc");

                        //Log.e(TAG, "1 "+intermediate_loc);

                        intermediate_loc        = intermediate_loc.replaceAll("[^a-zA-Z0-9,]", "");

                        //Log.e(TAG, "2 "+intermediate_loc);

                        String inter_mob        = res.getString("inter_mob");
                        inter_mob               = inter_mob.replaceAll("[^0-9,]", "");


                        String startOn          = res.getString("timef");
                        String expireOn         = res.getString("timet");
                        String loadWeight       = res.getString("weight");

                        String dimention        = res.getString("dimension");
                        String capacity         = res.getString("capacity");
                        String state            = res.getString("state");
                        String clientName       = res.getString("client");

                        long expireTime = getUnixInSec(TAG,expireOn);
                        long currentTime =  System.currentTimeMillis()/1000L;

                        if(currentTime>expireTime)
                            mMissedNotificationList.add(new Notification(loadId, vehicleType, pickupLocation, startMob, lastPoint, endMob, city,
                                    state, dimention, intermediate_loc , inter_mob,
                                    clientId, clientName,loadWeight, loadType, startOn, expireOn, amount,capacity));

                    }



                    Log.e(TAG,mMissedNotificationList.size()+"size");


                    TextView errorView = findViewById(R.id.empty_recycler_view);
                    if(mMissedNotificationList.size() == 0){
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText("No Misssed Notifications");
                    }
                    else {
                        errorView.setVisibility(View.GONE);
                    }

                    RecyclerView recyclerView   = findViewById(R.id.recycler_view_notification);
                    MissedNotificationAdapter adapter = new MissedNotificationAdapter(MissedNotificationPage.this,mMissedNotificationList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(MissedNotificationPage.this));
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    Toast.makeText(MissedNotificationPage.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MissedNotificationPage.this, error.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MissedNotificationPage.this).addToRequestQueue(stringRequest);

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
