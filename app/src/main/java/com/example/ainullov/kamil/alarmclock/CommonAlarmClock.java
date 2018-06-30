package com.example.ainullov.kamil.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommonAlarmClock extends AppCompatActivity {
    Intent serviceIntent;
    int forStopAlarm = 1;
    String currentTimeString;
    TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_alarm_clock);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        currentTimeString = new SimpleDateFormat("HH:mm").format(new Date());
        textViewTime.setText(currentTimeString);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(10);
                        currentTimeString = new SimpleDateFormat("HH:mm").format(new Date());
                        runOnUiThread(setCurrentTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();


    serviceIntent = new Intent(this, RingtonePlayingService.class);
        forStopAlarm = 1;
        serviceIntent.putExtra("extra", forStopAlarm);
        startService(serviceIntent);
    }

    Runnable setCurrentTime = new Runnable() {
        public void run() {
            textViewTime.setText(currentTimeString);
        }
    };

    public void onCommonCheck(View view) {
        forStopAlarm = 2;
        serviceIntent.putExtra("extra", forStopAlarm);
        startService(serviceIntent);

        Intent intentForNewAlarm;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;
        intentForNewAlarm = new Intent(this, AlarmReceiver.class);
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intentForNewAlarm, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY , pendingIntent);
        finish();
    }
}
