package com.road.roadexp_transporter.VehiclePackage;


import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.road.roadexp_transporter.R;
import com.road.roadexp_transporter.Review.Vehicle;
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
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class LocateVehicleInMap extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private GoogleMap mMap;
    private Marker mCurrLocationMarker;

    private int ZOOM_VALUE = 18;

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
        Log.e(TAG,"onMapReady");

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            LocateVehicleInMap.this, R.raw.style_json));
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

        Log.e(TAG,"called : showOnMap");
        Log.e(TAG, "location="+location);

        String s[] = location.split(",");

        LatLng latLng = new LatLng(Double.parseDouble(s[0]),Double.parseDouble(s[1]));

        if(mCurrLocationMarker == null){
            Log.e(TAG,"marker is null");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("MyLocation");

            markerOptions.icon(getMarkerIconFromDrawable(getDrawable(R.drawable.map_truck)));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE));
        }

        else {
            Log.e(TAG,"maker is not null");
            animateCar(mCurrLocationMarker.getPosition(), latLng);
        }




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


    private void animateCar(final LatLng firstPoint, final LatLng secondPoint){

        Log.e(TAG,"animateCar");

        Handler handler = new Handler();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(8000);
        valueAnimator.setInterpolator(new LinearInterpolator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        Log.e(TAG, "animation");
                        float v = valueAnimator.getAnimatedFraction();
                        double lng = v * secondPoint.longitude + (1 - v) * firstPoint.longitude;
                        double lat = v * secondPoint.latitude + (1 - v) * firstPoint.latitude;

                        LatLng newPos = new LatLng(lat, lng);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(newPos);
                        markerOptions.title("MyLocation");

                        markerOptions.icon(getMarkerIconFromDrawable(getDrawable(R.drawable.map_truck)));
                        mMap.clear();

                        mCurrLocationMarker.setAnchor(0.5f, 0.5f);
                        mCurrLocationMarker.setRotation(getBearing(firstPoint.latitude, firstPoint.longitude,
                                newPos.latitude, newPos.longitude));

                        mCurrLocationMarker = mMap.addMarker(markerOptions);


                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(new CameraPosition.Builder()
                                        .target(secondPoint)
                                        .zoom(mMap.getCameraPosition().zoom)
                                        .build()));
                    }
                });

            }
        },200);


        valueAnimator.start();




    }

    public float getBearing(double startLatitude,double startLongitude, double endLatitude, double endLongitude){
        double Phi1 = Math.toRadians(startLatitude);
        double Phi2 = Math.toRadians(endLatitude);
        double DeltaLambda = Math.toRadians(endLongitude - startLongitude);

        double Theta = atan2((sin(DeltaLambda)*cos(Phi2)) , (cos(Phi1)*sin(Phi2) - sin(Phi1)*cos(Phi2)*cos(DeltaLambda)));
        return (float)Math.toDegrees(Theta);
    }



}


