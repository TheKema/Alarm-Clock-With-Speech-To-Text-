package com.example.ainullov.kamil.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class RingtonePlayingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer mMediaPlayer;
    int startStop;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        startStop = intent.getExtras().getInt("extra", startStop);
        if (startStop == 1) {
            if (MainActivity.numberOfMusicName == 0)
                mMediaPlayer = MediaPlayer.create(this, R.raw.good_morning_miui_);
            if (MainActivity.numberOfMusicName == 1)
                mMediaPlayer = MediaPlayer.create(this, R.raw.morning_water_birds);
            if (MainActivity.numberOfMusicName == 2)
                mMediaPlayer = MediaPlayer.create(this, R.raw.simple_melody);
            if (MainActivity.numberOfMusicName == 3)
                mMediaPlayer = MediaPlayer.create(this, R.raw.electronic_melody);
            if (MainActivity.numberOfMusicName == 4)
                mMediaPlayer = MediaPlayer.create(this, R.raw.melody_of_a_mechanical_alarm_clock);
            if (MainActivity.numberOfMusicName == 5)
                mMediaPlayer = MediaPlayer.create(this, R.raw.operation_y);

            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);

        }
        if (startStop == 2) {
            mMediaPlayer.stop();
            stopSelf();
        }
        return START_NOT_STICKY;
    }
}