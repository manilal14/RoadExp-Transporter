package com.example.roadexp_transporter.Reports.TravelReport;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Review.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class TRFragVehicle extends Fragment {

    private View mRootView;
    private List<Vehicle> mVehicleList;


    public TRFragVehicle() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.trfrag_vehicle, container, false);
        mVehicleList = new ArrayList<>();

        fetchVehicleList();


        return mRootView;
    }

    private void fetchVehicleList() {

//        mVehicleList.add(new Vehicle(1,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Kedar Yadav"));
//
//
//        mVehicleList.add(new Vehicle(4,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Viru Paji"));
//
//        mVehicleList.add(new Vehicle(5,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Yuvraj Singh"));
//
//        mVehicleList.add(new Vehicle(6,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Mahi Bhai"));
//
//        mVehicleList.add(new Vehicle(8,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Saurav Dada"));
//
//        mVehicleList.add(new Vehicle(1,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Kedar Yadav"));
//
//
//        mVehicleList.add(new Vehicle(4,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Viru Paji"));
//
//        mVehicleList.add(new Vehicle(5,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Yuvraj Singh"));
//
//        mVehicleList.add(new Vehicle(6,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Mahi Bhai"));
//
//        mVehicleList.add(new Vehicle(8,1,"Tata Ace","BRGE1234",45624,
//                "pic-inc_paper","vechicleInvoice","numberPlate",
//                1,"Saurav Dada"));

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_travel_report_vehicle);
        TravelReportVehicleAdapter adapter = new TravelReportVehicleAdapter(getActivity(),mVehicleList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }

}
