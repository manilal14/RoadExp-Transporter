package com.example.roadexp_transporter.VehicleDetailPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.Objects;

public class FragMapDriver extends Fragment {

    private String TAG = "FragTravelDetails";
    private View mRoot;

    public FragMapDriver() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.frag_map_driver, container, false);
        fetchDriver();

        clickListener();
        return mRoot;
    }

    private void fetchDriver() {

        Spinner spiSelectDriver = mRoot.findViewById(R.id.spinner_select_driver);

        ArrayList<String> driverList = new ArrayList<>();

        driverList.add("");
        driverList.add("Kedar Jadav");
        driverList.add("Virat Kohli");
        driverList.add("MS Dhoni");
        driverList.add("Yuvraj Singh");
        driverList.add("Saurav Ganguly");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, driverList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiSelectDriver.setAdapter(adapter);

    }

    private void clickListener() {

        mRoot.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Backbutton is clicked");
                onDestroyView();

            }
        });

        mRoot.findViewById(R.id.whole_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Do nothing");
            }
        });




    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "OnDestroView2");
        super.onDestroyView();
        assert getFragmentManager() != null;
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

}
