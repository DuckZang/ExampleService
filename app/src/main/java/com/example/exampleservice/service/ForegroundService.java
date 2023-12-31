package com.example.exampleservice.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.exampleservice.MainActivity;
import com.example.exampleservice.MyApplication;
import com.example.exampleservice.MyReceiver;
import com.example.exampleservice.R;
import com.example.exampleservice.model.Song;

public class ForegroundService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_PLAY = 2;
    private static final int ACTION_CLOSE = 3;
    private Song mSong;

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
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("objectSong");
            if (song != null) {
                mSong = song;
                startMusic(song);
                showNotification(song);
            }
        }
        int actionMusic = intent.getIntExtra("actionMusicService", 0);
        handleAction(actionMusic);
        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, song.getResource());
            mediaPlayer.start();
        }

    }

    private void handleAction(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_PLAY:
                resumeMusic();
                break;
            case ACTION_CLOSE:
                stopSelf();
                break;
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            showNotification(mSong);
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            showNotification(mSong);
        }
    }

    private void showNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getTitle());
        remoteViews.setTextViewText(R.id.tv_singer_song, song.getSingle());
        remoteViews.setImageViewResource(R.id.btn_action, R.drawable.icon_pause);
        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.btn_action, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.btn_action, R.drawable.icon_pause);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.btn_action, getPendingIntent(this, ACTION_PLAY));
            remoteViews.setImageViewResource(R.id.btn_action, R.drawable.icon_play);
        }
        remoteViews.setOnClickPendingIntent(R.id.btn_close, getPendingIntent(this, ACTION_CLOSE));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(context, MyReceiver.class);
        intent.putExtra("actionMusic", action);
        return PendingIntent.getBroadcast(context, action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        Log.d("zzzzz", "Destroy: ");
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
