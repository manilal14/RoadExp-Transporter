package com.example.roadexp_transporter.Reports.PaymentReport;


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


public class PRFragVechicle extends Fragment {

    private View mRootView;
    private List<Vehicle> mVehicleList;

    public PRFragVechicle() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.prfrag_vechicle, container, false);

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

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_payment_report_vehicle);
        PaymentReportVehicleAdapter adapter = new PaymentReportVehicleAdapter(getActivity(),mVehicleList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }
}
