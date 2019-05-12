package com.example.roadexp_transporter.Reports.PaymentReport;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.roadexp_transporter.R;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryAdapter;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryModel;
import com.example.roadexp_transporter.Reports.TravelReport.TravelHistoryPage;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryPage extends AppCompatActivity {

    private List<PaymentHistoryModel> mPaymentHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_payment_history_page);

        mPaymentHistoryList = new ArrayList<>();
        fetchPaymentHistory();


        clickListener();
    }

    private void fetchPaymentHistory() {

        mPaymentHistoryList.add(new PaymentHistoryModel(1));
        mPaymentHistoryList.add(new PaymentHistoryModel(2));
        mPaymentHistoryList.add(new PaymentHistoryModel(3));
        mPaymentHistoryList.add(new PaymentHistoryModel(4));
        mPaymentHistoryList.add(new PaymentHistoryModel(5));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_payment_history);
        PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(PaymentHistoryPage.this,mPaymentHistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentHistoryPage.this));
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
