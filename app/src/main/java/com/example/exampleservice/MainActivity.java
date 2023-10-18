package com.example.exampleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exampleservice.model.Song;
import com.example.exampleservice.service.BoundService;
import com.example.exampleservice.service.ForegroundService;
import com.example.exampleservice.service.MusicService;
import com.example.exampleservice.service.MyBackgroundService;


public class MainActivity extends AppCompatActivity {
    private static final int JOB_ID = 123;
    private Button btn;
    private Button btnCancel;
    private Button btnStartBackgroundService;
    private Button btnStopBackgroundService;
    private Button btnStartBoundService, btnPlayMusic;
    private Button btnStopBoundService, btnStopMusic;
    private RelativeLayout bottomNavMusic;
    private TextView titleSong, singerSong;
    private ImageView btnActionMusic;
    private BoundService boundService;
    private MusicService musicService;
    private boolean isServiceConnected;
    private boolean isConnected;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyBinder binder = (BoundService.MyBinder) iBinder;
            boundService = binder.getBoundService();
            boundService.startMusic();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) iBinder;
            musicService = musicBinder.getMusicService();
//
            isServiceConnected = true;

            handleBottomNavMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };

    private void handleBottomNavMusic() {
        bottomNavMusic.setVisibility(View.VISIBLE);
        titleSong.setText(musicService.getmSong().getTitle());
        singerSong.setText(musicService.getmSong().getSingle());
        setStatusAction();
    }

    public void setStatusAction() {
        if (musicService == null) {
            return;
        }
        if (musicService.isPlaying()) {
            btnActionMusic.setImageResource(R.drawable.icon_pause);
        } else {
            btnActionMusic.setImageResource(R.drawable.icon_play);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnStartBackgroundService = (Button) findViewById(R.id.btn_start_background_service);
        btnStopBackgroundService = (Button) findViewById(R.id.btn_stop_background_service);
        btnStartBoundService = (Button) findViewById(R.id.btn_start_bound_service);
        btnStopBoundService = (Button) findViewById(R.id.btn_stop_bound_service);
        btnActionMusic = findViewById(R.id.btn_action_music);
        titleSong = findViewById(R.id.title_song);
        singerSong = findViewById(R.id.singer_song);
        btnPlayMusic = findViewById(R.id.btn_play_music);
        btnStopMusic = findViewById(R.id.btn_stop_music);
        bottomNavMusic = findViewById(R.id.bottom_nav_music);

        btnActionMusic.setOnClickListener(v -> {
            if (musicService.isPlaying()) {
                musicService.pauseMusic();
                setStatusAction();
            } else {
                musicService.resumeMusic();
                setStatusAction();
            }

        });

        btn = findViewById(R.id.btn);
        btnCancel = findViewById(R.id.btn_cancel);
        btn.findViewById(R.id.btn);
//        ** foregroundService
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForegroundService.class);
            Song song = new Song("3107", "Duong", R.raw.music);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objectSong", song);
            intent.putExtras(bundle);
            startService(intent);

        });
        btnCancel.setOnClickListener(v -> {
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
//         ** bound service
        btnStartBoundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoundService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
        });
        btnStopBoundService.setOnClickListener(v -> {
            if (isConnected) {
                unbindService(connection);
                isConnected = false;
            }
        });
        // ** music service
        btnPlayMusic.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicService.class);
            Song song = new Song("3107", "Duong", R.raw.music);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objectSong", song);
            intent.putExtras(bundle);
            startService(intent);

            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });
        btnStopMusic.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicService.class);
            stopService(intent);
            if (isServiceConnected) {
                unbindService(serviceConnection);
                isServiceConnected = false;
            }
            bottomNavMusic.setVisibility(View.GONE);
        });
    }
}

// ** start job scheduler
//            ComponentName componentName = new ComponentName(this, JobSchedulerService.class);
//            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                    .setPersisted(true)
//                    .setPeriodic(15 * 60 * 1000)
//                    .build();
//            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(jobInfo);
// ** stop job scheduler
//            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.cancel(JOB_ID);