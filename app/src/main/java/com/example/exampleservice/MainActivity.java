package com.example.exampleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;

import com.example.exampleservice.service.BoundService;
import com.example.exampleservice.service.ForegroundService;
import com.example.exampleservice.service.MyBackgroundService;


public class MainActivity extends AppCompatActivity {
    private static final int JOB_ID = 123;
    private EditText edtTextContent;
    private Button btn;
    private Button btnCancel;
    private Button btnStartBackgroundService;
    private Button btnStopBackgroundService;
    private Button btnStartBoundService;
    private Button btnStopBoundService;
    private BoundService boundService;
    private boolean isServiceConnected;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyBinder binder = (BoundService.MyBinder) iBinder;
            boundService = binder.getBoundService();
            boundService.startMusic();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTextContent = (EditText) findViewById(R.id.edt_textContent);
        btn = (Button) findViewById(R.id.btn);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnStartBackgroundService = (Button) findViewById(R.id.btn_start_background_service);
        btnStopBackgroundService = (Button) findViewById(R.id.btn_stop_background_service);
        btnStartBoundService = (Button) findViewById(R.id.btn_start_bound_service);
        btnStopBoundService = (Button) findViewById(R.id.btn_stop_bound_service);

        btn = findViewById(R.id.btn);
        btnCancel = findViewById(R.id.btn_cancel);
        btn.findViewById(R.id.btn);
//        ** foregroundService
        btn.setOnClickListener(v -> {

            Intent intent = new Intent(this, ForegroundService.class);
            intent.putExtra("data_text", edtTextContent.getText().toString().trim());
            startService(intent);
            // ** job scheduler
//            ComponentName componentName = new ComponentName(this, JobSchedulerService.class);
//            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                    .setPersisted(true)
//                    .setPeriodic(15 * 60 * 1000)
//                    .build();
//            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(jobInfo);
        });
        btnCancel.setOnClickListener(v -> {
            // ** job scheduler
//            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.cancel(JOB_ID);
            stopService(new Intent(this, ForegroundService.class));
        });
        // ** background Service
        btnStartBackgroundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyBackgroundService.class);
            startService(intent);
        });
        btnStopBackgroundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyBackgroundService.class);
            stopService(intent);
        });
        // ** bound service
        btnStartBoundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoundService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });
        btnStopBoundService.setOnClickListener(v -> {
            if (isServiceConnected) {
                unbindService(serviceConnection);
                isServiceConnected = false;
            }
        });
    }
}