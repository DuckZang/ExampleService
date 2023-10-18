package com.example.exampleservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.exampleservice.service.ForegroundService;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("actionMusic",0);
        Intent intentService = new Intent(context, ForegroundService.class);
        intentService.putExtra("actionMusicService",actionMusic);
        context.startService(intentService);
    }
}
