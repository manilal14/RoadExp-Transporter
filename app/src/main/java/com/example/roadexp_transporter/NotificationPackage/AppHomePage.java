package com.example.roadexp_transporter.NotificationPackage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.example.roadexp_transporter.AddDriverPackage.AddDriverPage;
import com.example.roadexp_transporter.Reports.MissedLoad.MissedLoadsPage;
import com.example.roadexp_transporter.Reports.PaymentReport.PaymentReportPage;
import com.example.roadexp_transporter.Reports.TravelReport.TravelReportPage;
import com.example.roadexp_transporter.VehicleStatusHomePage;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.ReviewBottomSheet;
import com.example.roadexp_transporter.NavigationDrawer.ExpandableListAdapter;
import com.example.roadexp_transporter.NavigationDrawer.MenuModel;
import com.example.roadexp_transporter.DriverPackage.DriverHomepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppHomePage extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private List<Notification> mNotificationList;


    //Navigation Menu
    private ExpandableListView mExpandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

   private ExpandableListAdapter expandableListAdapter;

    private int lastExpandedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);

        settingNavigation();

        mNotificationList = new ArrayList<>();
        fetchNotification();

    }


    private void fetchNotification() {

        mNotificationList.add(new Notification(1,"Harmanpreet Kaur"));
        mNotificationList.add(new Notification(2,"Smiriti Mandhava"));
        mNotificationList.add(new Notification(3,"Mandira Bedi"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notification);

        NotificationAdapter adapter = new NotificationAdapter(AppHomePage.this,mNotificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AppHomePage.this));
        recyclerView.setAdapter(adapter);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings : break;

        }



        return super.onOptionsItemSelected(item);
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

        headerView.findViewById(R.id.header_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppHomePage.this,"Edit Profile", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        tv_name.setText("Manilal Kasera");
        tv_mobile.setText("8634567890");



    }
    private void prepareMenuData(){

        MenuModel menuModel = new MenuModel(0,"Vehicle Status",false,true,0);
        headerList.add(menuModel);
        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        List<MenuModel> childModelsList;


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

        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel(3,"Payment Records",false,true,0);
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
                            case 0: startActivity(new Intent(AppHomePage.this, VehicleStatusHomePage.class)); break;
                            case 3: break;
                            case 4:
                                ReviewBottomSheet reviewBottomSheet = new ReviewBottomSheet();
                                reviewBottomSheet.show(getSupportFragmentManager(), reviewBottomSheet.getTag());
                                break;
                            case 5: break;
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
                        case 1:
                            switch (childPosition){
                                case 0:startActivity(new Intent(AppHomePage.this, MissedLoadsPage.class));break;
                                case 1:startActivity(new Intent(AppHomePage.this, TravelReportPage.class));break;
                                case 2:startActivity(new Intent(AppHomePage.this, PaymentReportPage.class));break;


                            }
                            break;

                        case 2:
                            switch (childPosition){
                                case 0: startActivity(new Intent(AppHomePage.this, AddDriverPage.class));break;
                                case 1: startActivity(new Intent(AppHomePage.this, DriverHomepage.class));break;

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
}
