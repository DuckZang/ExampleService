package com.example.exampleservice.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.exampleservice.R;

public class MyBackgroundService extends Service {
    private MediaPlayer mediaPlayer;
    public MyBackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMusic();
        return START_NOT_STICKY;
    }
    private void startMusic(){
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
        }
        mediaPlayer.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}