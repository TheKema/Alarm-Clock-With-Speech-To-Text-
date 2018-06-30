package com.example.ainullov.kamil.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Wake up!", Toast.LENGTH_LONG).show();

        Intent commonIntent = new Intent(context, CommonAlarmClock.class);
        commonIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(commonIntent);
    }
}

