package com.example.roadexp_transporter.HomePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.example.roadexp_transporter.AddNewDriver.AddDriverPage;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.DriverPackage.Unverified.UnverifiedDriver;
import com.example.roadexp_transporter.FCM.SharedPrefFcm;
import com.example.roadexp_transporter.Initializer;
import com.example.roadexp_transporter.LoginSingUp.LoginPage;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.NetworkState.ConnectivityReceiver;
import com.example.roadexp_transporter.MissedNotification.MissedNotificationPage;
import com.example.roadexp_transporter.Reports.MissedLoad.MissedLoadsPage;
import com.example.roadexp_transporter.Reports.PaymentReport.PaymentReportPage;
import com.example.roadexp_transporter.Reports.TravelReport.TravelReportHomePage;
import com.example.roadexp_transporter.VehiclePackage.UnverifiedVehicle.UnverifiedVehiclePage;
import com.example.roadexp_transporter.VehiclePackage.VehicleStatusHomePage;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.ReviewBottomSheet;
import com.example.roadexp_transporter.NavigationDrawer.ExpandableListAdapter;
import com.example.roadexp_transporter.NavigationDrawer.MenuModel;
import com.example.roadexp_transporter.DriverPackage.DriverHomepage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.getUnixInSec;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_NAME;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_PHONE;

public class AppHomePage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private String TAG = this.getClass().getSimpleName();

    private List<Notification> mNotificationList;

    //Navigation Menu
    private ExpandableListView mExpandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    private ExpandableListAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;

    private LoginSessionManager mSession;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        Log.e(TAG, "called : onCreate");

       // checkConnection();

        mSession = new LoginSessionManager(AppHomePage.this);
        mSwipeRefreshLayout = findViewById(R.id.swipe_to_refresh);

        if(!mSession.isLoggedIn()){
            startActivity(new Intent(AppHomePage.this, LoginPage.class));
            finish();
            return;
        }

        settingNavigation();
        mNotificationList = new ArrayList<>();

        String token = SharedPrefFcm.getmInstance(AppHomePage.this).getToken();
        if(token!=null){
            Log.e(TAG,"Fcm token from sharedPref: "+token);
            storeTokenToDb(token);
        }

        fetchNotification();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotification();
            }
        });

    }

    private void storeTokenToDb(final String refreshedToken) {

        Log.e(TAG,"called : storeTokenToDb");
        String URL_TOKEN = BASE_URL + "fcm_trans";

        final String phone = mSession.getTransporterDetailsFromPref().get(TRANS_PHONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.toString());


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mob",phone);
                params.put("fcm",refreshedToken);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS*1000,NO_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void fetchNotification() {

        Log.e(TAG, "called :fetchNotification ");
        String STATE_URL = BASE_URL + "sosnotification";
        mSwipeRefreshLayout.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mSwipeRefreshLayout.setRefreshing(false);

                if(mNotificationList.size()!=0)
                    mNotificationList.clear();

                Log.e(TAG, response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");

                    if(code!=1){
                        Toast.makeText(AppHomePage.this,"Something went wrong",Toast.LENGTH_SHORT).show();
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

                        if(currentTime<expireTime)
                            mNotificationList.add(new Notification(loadId, vehicleType, pickupLocation, startMob, lastPoint, endMob, city,
                                state, dimention, intermediate_loc , inter_mob,
                                clientId, clientName,loadWeight, loadType, startOn, expireOn, amount,capacity));
                    }

                    Log.e(TAG,mNotificationList.size()+"size");


                    TextView errorView = findViewById(R.id.error_message);
                    if(mNotificationList.size() == 0){
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText("No loads to show");
                    }
                    else {
                        errorView.setVisibility(View.GONE);
                    }

                    RecyclerView recyclerView   = findViewById(R.id.recycler_view_notification);
                    NotificationAdapter adapter = new NotificationAdapter(AppHomePage.this,mNotificationList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(AppHomePage.this));
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    Toast.makeText(AppHomePage.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(AppHomePage.this, error.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(AppHomePage.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //No nees of menu as of now

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_settings : break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Initializer.getInstance() == null){
            Log.e(TAG,"MyApplication getInstance is null");
            return;
        }

        Initializer.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }



    /*---------------------------------------------Navigation Drawer------------------------------------------------------*/

    private void settingNavigation() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExpandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView      =  navigationView.getHeaderView(0);

        ImageView iv_profile =  headerView.findViewById(R.id.profile_image);
        TextView tv_name     =  headerView.findViewById(R.id.header_name);
        TextView tv_mobile   =  headerView.findViewById(R.id.header_phone);

        //Edit profile is commented in header

//        headerView.findViewById(R.id.header_edit_profile).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AppHomePage.this,"Edit Profile", Toast.LENGTH_SHORT).show();
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        String name = mSession.getTransporterDetailsFromPref().get(TRANS_NAME);
        String phone = mSession.getTransporterDetailsFromPref().get(TRANS_PHONE);

        tv_name.setText(name);
        tv_mobile.setText(phone);
    }
    private void prepareMenuData(){

        MenuModel menuModel = new MenuModel(0,"Vehicles",true,true,0);
        headerList.add(menuModel);

        List<MenuModel> childModelsList;

        childModelsList = new ArrayList<>();
        childModelsList.add(new MenuModel(01,"Vehicles Status", false, false, 0));
        childModelsList.add(new MenuModel(03,"Unverified Vehicle", false, false, 0));

        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }



        menuModel = new MenuModel(1,"Reports",true,true,0);
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();

        MenuModel childModel = new MenuModel(11,"Missed Loads", false, false, 0);
        childModelsList.add(childModel);

        childModel = new MenuModel(21,"Travel Reports", false, false, 0);
        childModelsList.add(childModel);

        childModel = new MenuModel(31,"Payment Reports", false, false, 0);
        childModelsList.add(childModel);

        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(2,"Driver",true,true,0);
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModelsList.add(new MenuModel(21,"Add Driver", false, false, 0));
        childModelsList.add(new MenuModel(22,"View All", false, false, 0));
        childModelsList.add(new MenuModel(23,"Unverified Driver", false, false, 0));

        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }


//        menuModel = new MenuModel(3,"Payment Records",false,true,0);
//        headerList.add(menuModel);
//        if (!menuModel.isHasChildren()) {
//            childList.put(menuModel, null);
//        }

        menuModel = new MenuModel(3,"Missed Notification",false,true,0);
        headerList.add(menuModel);
        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(4,"Review us",false,true,0);
        headerList.add(menuModel);
        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(5,"Log Out",false,true,0);
        headerList.add(menuModel);
        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        mExpandableListView.setAdapter(expandableListAdapter);

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                MenuModel menuModel = headerList.get(groupPosition);
                int id_int = (int) id;

                if (menuModel.isGroup()) {

                    if (!menuModel.isHasChildren()) {
                        switch (id_int){
                            case 3: startActivity(new Intent(AppHomePage.this, MissedNotificationPage.class));
                                break;
                            case 4: ReviewBottomSheet reviewBottomSheet = new ReviewBottomSheet();
                                reviewBottomSheet.show(getSupportFragmentManager(), reviewBottomSheet.getTag());
                                break;
                            case 5: mSession.logout();
                                finish();
                                break;
                        }
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {

                    //MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                    switch (groupPosition){
                        case 0:
                            switch (childPosition){
                                case 0:startActivity(new Intent(AppHomePage.this, VehicleStatusHomePage.class)); break;
                                case 1:startActivity(new Intent(AppHomePage.this, UnverifiedVehiclePage.class));break;


                            }
                            break;
                        case 1:
                            switch (childPosition){
                                case 0:startActivity(new Intent(AppHomePage.this, MissedLoadsPage.class));break;
                                case 1:startActivity(new Intent(AppHomePage.this, TravelReportHomePage.class));break;
                                case 2:startActivity(new Intent(AppHomePage.this, PaymentReportPage.class));break;


                            }
                            break;

                        case 2:
                            switch (childPosition){
                                case 0: startActivity(new Intent(AppHomePage.this, AddDriverPage.class));break;
                                case 1: startActivity(new Intent(AppHomePage.this, DriverHomepage.class));break;
                                case 2: startActivity(new Intent(AppHomePage.this, UnverifiedDriver.class));break;

                            }

                            break;
                    }
                    onBackPressed();
                }

                return false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("asd","network state changed");
        showSnack(isConnected);
    }


//    public AlertDialog.Builder buildDialog(Context c){
//
//        AlertDialog.Builder builder =  new AlertDialog.Builder(c);
//        builder.setTitle("No Internet");
//        builder.setMessage("You must be connected to internet to access this App");
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//
//        return builder;
//    }


}
