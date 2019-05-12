package com.example.roadexp_transporter.AddDriverPackage;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.roadexp_transporter.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ADFrag1 extends Fragment {

    private String TAG = "ADFrag1";
    private View mRoot;

    private AddDriverPage mActivity;

    public ADFrag1() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AddDriverPage) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.adfrag1, container, false);
        mActivity.translateBoy(0);

        fetchState();
        fetchCity();




        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.fard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ADFrag2(),"ad_frag_2");
            }
        });

        mRoot.findViewById(R.id.birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDOBInEditText();
            }
        });

    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void fetchState(){

        Spinner spiState = mRoot.findViewById(R.id.spinner_state);

        ArrayList<String> cityList = new ArrayList<>();


        cityList.add("Choose State");
        cityList.add("Kerala");
        cityList.add("Bihar");
        cityList.add("Jharkhnad");
        cityList.add("Tamil Nadu");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, cityList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiState.setAdapter(adapter);

    }

    private void fetchCity(){

        Spinner spiCity = mRoot.findViewById(R.id.spinner_city);

        ArrayList<String> cityList = new ArrayList<>();


        cityList.add("Choose City");
        cityList.add("Kochi");
        cityList.add("Ernakulam");


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_custom, cityList);

        adapter.setDropDownViewResource(R.layout.spinner_layout_custom);

        spiCity.setAdapter(adapter);

    }

    public void setDOBInEditText() {

        final Calendar calendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        new DatePickerDialog(getActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        Calendar calendar =  Calendar.getInstance();
        String date = sdf.format(calendar.getTime());

        EditText et_dob = mRoot.findViewById(R.id.birthday);
        et_dob.setText(date);
    }

}
