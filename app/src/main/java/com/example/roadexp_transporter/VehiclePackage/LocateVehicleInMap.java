package com.example.roadexp_transporter.VehiclePackage;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.roadexp_transporter.MapsActivity;
import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.GPS_REQUEST;

public class LocateVehicleInMap extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private GoogleMap mMap;
    private Marker mCurrLocationMarker;

    private int ZOOM_VALUE = 17;

    private Vehicle mVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_vehicle_in_map);

        mVehicle = (Vehicle) getIntent().getSerializableExtra("vehicle_detail");

        initMap();
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            LocateVehicleInMap.this, R.raw.style_json2));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }


        getRealTimeLocation();



    }






    private void getRealTimeLocation() {

        Log.e(TAG, "called : getRealTimeLocation");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("driver/" + mVehicle.getDriverId());


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                Log.e(TAG,"onChildAdded");
                if (dataSnapshot.getKey() != null && dataSnapshot.getKey().equals("location")) {
                    showOnMap(dataSnapshot.getValue()+"");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.e(TAG,"onChildChanged");
                if (dataSnapshot.getKey() != null && dataSnapshot.getKey().equals("location")) {
                    showOnMap(dataSnapshot.getValue()+"");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showOnMap(String location) {

        Log.e(TAG,"called : shoeOnMap");
        Log.e(TAG, "location="+location);

        String s[] = location.split(",");

        LatLng latLng = new LatLng(Double.parseDouble(s[0]),Double.parseDouble(s[1]));



//        if(mCurrLocationMarker != null)
//            mCurrLocationMarker.remove();



//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                .target(mMap.getCameraPosition().target)
//                .zoom(15)
//                .bearing(30)
//                .tilt(45)
//                .build()));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZOOM_VALUE));
        changePositionSmoothly(latLng);


    }

    void changePositionSmoothly(LatLng newLatLng) {

        Drawable circleDrawable = ContextCompat.getDrawable(LocateVehicleInMap.this,R.drawable.truck_to_animate);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        mMap.addMarker(new MarkerOptions()
                .position(newLatLng).title("Truck").icon(markerIcon));

        if (mCurrLocationMarker == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng,ZOOM_VALUE));
            return;
        }

        mMap.clear();

        ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
        final float[] previousStep = {0f};
        final double deltaLatitude = newLatLng.latitude - mCurrLocationMarker.getPosition().latitude;
        final double deltaLongitude = newLatLng.longitude - mCurrLocationMarker.getPosition().latitude;
        animation.setDuration(1500);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation1) {
                float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
                previousStep[0] = (Float) animation1.getAnimatedValue();
                mCurrLocationMarker.setPosition(new LatLng(mCurrLocationMarker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, mCurrLocationMarker.getPosition().latitude + deltaStep * deltaLongitude * 1 / 100));
            }
        });
        animation.start();

    }




    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
