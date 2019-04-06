package com.example.roadexp_transporter;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Vehicle> mVehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mVehicleList = new ArrayList<>();
        fetchVehicleList();

    }

    private void fetchVehicleList() {

        mVehicleList.add(new Vehicle(1,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Kedar Yadav"));

        mVehicleList.add(new Vehicle(2,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Kedar Yadav"));

        mVehicleList.add(new Vehicle(3,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Kedar Yadav"));

        mVehicleList.add(new Vehicle(4,1,"Tata Ace","BRGE1234",45624,
                "pic-inc_paper","vechicleInvoice","numberPlate",
                1,"Kedar Yadav"));


        RecyclerView recyclerView = findViewById(R.id.recycler_view_homepage);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new VehicleAdapter(MainActivity.this,mVehicleList));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_home) {
//        } else if (id == R.id.nav_reports) {
//
//        } else if (id == R.id.nav_driver) {
//
//        } else if (id == R.id.nav_pay_records) {
//
//        } else if (id == R.id.nav_reports) {
//
//        } else if (id == R.id.nav_logout) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
