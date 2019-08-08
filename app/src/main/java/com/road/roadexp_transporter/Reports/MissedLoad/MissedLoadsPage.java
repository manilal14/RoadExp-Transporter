package com.road.roadexp_transporter.Reports.MissedLoad;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.road.roadexp_transporter.CommonForAll.MySingleton;
import com.road.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.road.roadexp_transporter.R;

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
import static com.road.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class MissedLoadsPage extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar mProgressBar;
    private List<MissedLoad> mMissedLoadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_missed_load_home_page);
        mProgressBar = findViewById(R.id.progress_bar);
        mMissedLoadList  = new ArrayList<>();
        
        fetchMissedLoads();

        clickListener();
    }

    private void fetchMissedLoads() {

        Log.e(TAG,"called : fetchMissedLoads");
        mProgressBar.setVisibility(View.VISIBLE);

        TextView tv_total_loads  = findViewById(R.id.total_loads);
        TextView tv_accept_loads = findViewById(R.id.accepted_loads);
        final TextView tv_missed_loads = findViewById(R.id.missed_loads);


        String URL = BASE_URL + "showmissedload";

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
                                Toast.makeText(MissedLoadsPage.this,"code is not 1", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray result = jsonObject.getJSONArray("result");
                            tv_missed_loads.setText(result.length()+"");

                            for(int i=0;i<result.length();i++){
                                JSONObject obj = result.getJSONObject(i);

                                String loadId           = obj.getString("load_id");
                                String date             = obj.getString("date_1").substring(0,10);
                                String did              = obj.getString("Did");
                                String intermediateLoc  = obj.getString("intermediate_loc");
                                String pickupLocation   = obj.getString("pickup_location");
                                String lastPoint        = obj.getString("last_point");
                                String weight           = obj.getString("weight");
                                String driverName       = obj.getString("d_name");

                                mMissedLoadList.add(new MissedLoad(loadId,date,did,intermediateLoc,pickupLocation, lastPoint,
                                        weight,driverName));
                            }

                            RecyclerView recyclerView = findViewById(R.id.recycler_view_missed_load);
                            MissedAdapter adapter = new MissedAdapter(MissedLoadsPage.this, mMissedLoadList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MissedLoadsPage.this));
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
                Toast.makeText(MissedLoadsPage.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String transId = new LoginSessionManager(MissedLoadsPage.this).getTransporterDetailsFromPref().get(TRANS_ID);
                Log.e(TAG, "trandId="+transId);
                params.put("trans",transId);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MissedLoadsPage.this).addToRequestQueue(stringRequest);

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
