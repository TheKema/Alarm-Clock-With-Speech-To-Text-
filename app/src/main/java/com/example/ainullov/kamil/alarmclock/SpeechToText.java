package com.example.ainullov.kamil.alarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SpeechToText extends Activity {
    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtOutput;
    private TextView textViewIfWrongAnswer;
    private TextView task;
    Button check;
    private ImageButton btnMicrophone;
    String text = "";
    String strRandom_result = "";
    int random_number1;
    int random_number2;
    int random_result;
    Random r;
    Intent serviceIntent;
    int forStopAlarm = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        txtOutput = (TextView) findViewById(R.id.txt_output);
        textViewIfWrongAnswer = (TextView) findViewById(R.id.textViewIfWrongAnswer);
        task = (TextView) findViewById(R.id.task);
        check = (Button) findViewById(R.id.check);
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        r = new Random();
        random_number1 = r.nextInt(100);
        random_number2 = r.nextInt(100);
        random_result = random_number1 + random_number2;
        task.setText(random_number1 + " + " + random_number2 + " = ");
        strRandom_result = Integer.toString(random_result);

        serviceIntent = new Intent(this, RingtonePlayingService.class);
        forStopAlarm = 1;
        serviceIntent.putExtra("extra", forStopAlarm);
        startService(serviceIntent);

    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text = result.get(0);
                    txtOutput.setText(text);
                }
                break;
            }
        }
    }

    public void onCheck(View view) {
        if (text.equals(strRandom_result)) {
            forStopAlarm = 2;
            serviceIntent.putExtra("extra", forStopAlarm);
            startService(serviceIntent);

            Intent intentForNewAlarm;
            PendingIntent pendingIntent;
            AlarmManager alarmManager;

            intentForNewAlarm = new Intent(this, SpeechToText.class);
            alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intentForNewAlarm, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY , pendingIntent);

            finish();
        } else {
            textViewIfWrongAnswer.setText("Wrong. Try Again!");
        }
    }
}