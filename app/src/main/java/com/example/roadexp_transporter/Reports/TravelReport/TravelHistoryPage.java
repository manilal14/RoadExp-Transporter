package com.example.roadexp_transporter.Reports.TravelReport;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.roadexp_transporter.DriverPackage.Driver;
import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class TravelHistoryPage extends AppCompatActivity {

    private List<TravelHistoryModel> mTravelHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_travel_history);


        // Need to get vehicle id through intent

        mTravelHistoryList = new ArrayList<>();
        fetchHistory();


        clickListener();

    }

    private void fetchHistory() {

        mTravelHistoryList.add(new TravelHistoryModel(1));
        mTravelHistoryList.add(new TravelHistoryModel(2));
        mTravelHistoryList.add(new TravelHistoryModel(3));
        mTravelHistoryList.add(new TravelHistoryModel(4));
        mTravelHistoryList.add(new TravelHistoryModel(5));


        RecyclerView recyclerView = findViewById(R.id.recycler_view_travel_history);

        TravelHistoryAdapter adapter = new TravelHistoryAdapter(TravelHistoryPage.this,mTravelHistoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(TravelHistoryPage.this));
        recyclerView.setAdapter(adapter);

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
