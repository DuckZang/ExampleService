package com.example.exampleservice.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.exampleservice.MainActivity;
import com.example.exampleservice.MyApplication;
import com.example.exampleservice.R;

public class ForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("zzzzz", "Create: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("zzzzz", "Start: ");
        String textContent = intent.getStringExtra("data_text");
        showNotification(textContent);
        return START_NOT_STICKY;
    }

    private void showNotification(String textContent) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,123,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Foreground service")
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        Log.d("zzzzz", "Destroy: ");
        super.onDestroy();
    }
}
