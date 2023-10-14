package com.example.exampleservice.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.exampleservice.R;


public class BoundService extends Service {
    private MyBinder mMyBinder = new MyBinder();
    private MediaPlayer mediaPlayer;

    public class MyBinder extends Binder {
        public BoundService getBoundService() {
            return BoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("zzzzzzzz", "onCreate: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("zzzzzzzz", "onBind: ");
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("zzzzzzzz", "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("zzzzzzzz", "onDestroy: ");
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void startMusic(){
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
        }
        mediaPlayer.start();
    }
}
