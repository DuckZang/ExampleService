package com.example.exampleservice.service;

import static com.example.exampleservice.R.raw.music;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.util.Log;

public class JobSchedulerService extends JobService {
    private boolean isJobCancel;
    private MediaPlayer mediaPlayer;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("zzzzzzzz", "onStartJob: ");
        doWorkOnBackground(jobParameters);
        return true;
    }

    private void doWorkOnBackground(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 19; i++) {
                    if (isJobCancel) {
                        return;
                    }
                    Log.d("zzzz", "run: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d("zzzzzzzz", "onFinishJob: ");
                jobFinished(jobParameters, false);
            }
        }).start();
//        if (!isJobCancel) {
//            if (mediaPlayer == null) {
//                mediaPlayer = MediaPlayer.create(this, music);
//            }
//            mediaPlayer.start();
//        }

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("zzzzzzzz", "onStopJob: ");
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            isJobCancel = true;
//        }
        isJobCancel = true;
//        mediaPlayer.stop();
        return true;
    }
}
