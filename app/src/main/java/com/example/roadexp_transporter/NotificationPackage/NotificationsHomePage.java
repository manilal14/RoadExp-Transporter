package com.example.roadexp_transporter.NotificationPackage;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.roadexp_transporter.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsHomePage extends AppCompatActivity {

    private List<Notification> mNotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mNotificationList = new ArrayList<>();

        fetchNotification();
        
        clickListener();
        
    }

    private void fetchNotification() {

        mNotificationList.add(new Notification(1,"Harmanpreet Kaur"));
        mNotificationList.add(new Notification(2,"Smiriti Mandhava"));
        mNotificationList.add(new Notification(3,"Mandira Bedi"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notification);

        NotificationAdapter adapter = new NotificationAdapter(NotificationsHomePage.this,mNotificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsHomePage.this));
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
