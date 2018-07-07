package com.example.ainullov.kamil.alarmclock.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.ainullov.kamil.alarmclock.AlarmClockActivity.SpeechToText;

public class AlarmSpeechToTextReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Wake up!", Toast.LENGTH_LONG).show();

        Intent speechToTextIntent = new Intent(context, SpeechToText.class);
        speechToTextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(speechToTextIntent);
    }
}

