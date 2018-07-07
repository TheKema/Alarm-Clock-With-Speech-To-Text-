package com.example.ainullov.kamil.alarmclock.AlarmClockActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ainullov.kamil.alarmclock.R;
import com.example.ainullov.kamil.alarmclock.RingtonePlayingService;

import java.util.Random;

public class TaskAlarmClock extends AppCompatActivity {
    TextView textViewTask;
    TextView textViewLeft;
    TextView textViewIfWrongAnswerTask;
    EditText editTextAnswer;
    Button btnCheck;
    int editTextRes;
    int countRightAnswers = 3;

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
        setContentView(R.layout.activity_task_alarm_clock);
        textViewTask = (TextView) findViewById(R.id.textViewTask);
        textViewLeft = (TextView) findViewById(R.id.textViewLeft);
        textViewIfWrongAnswerTask = (TextView) findViewById(R.id.textViewIfWrongAnswerTask);
        editTextAnswer = (EditText) findViewById(R.id.editTextAnswer);
        btnCheck = (Button) findViewById(R.id.btnCheck);


        r = new Random();
        random_number1 = r.nextInt(100);
        random_number2 = r.nextInt(100);
        random_result = random_number1 + random_number2;
        textViewTask.setText(random_number1 + " + " + random_number2 + " = ");
        strRandom_result = Integer.toString(random_result);

        serviceIntent = new Intent(this, RingtonePlayingService.class);
        forStopAlarm = 1;
        serviceIntent.putExtra("extra", forStopAlarm);
        startService(serviceIntent);
    }

    public void onCheckTask(View view) {
        String strCheckIfEmpty = editTextAnswer.getText().toString();
        if(!strCheckIfEmpty.equals("")) {
            editTextRes = Integer.valueOf(strCheckIfEmpty);
            if (editTextRes == random_result) {
                countRightAnswers--;
                textViewLeft.setText("You need to answer correctly: " + countRightAnswers + " times");
                textViewIfWrongAnswerTask.setText("Right!");
                random_number1 = r.nextInt(100);
                random_number2 = r.nextInt(100);
                random_result = random_number1 + random_number2;
                editTextAnswer.setText("");
                if (countRightAnswers != 0)
                    textViewTask.setText(random_number1 + " + " + random_number2 + " = ");
                if (countRightAnswers == 0) { // Отключаем
                    forStopAlarm = 2;
                    serviceIntent.putExtra("extra", forStopAlarm);
                    startService(serviceIntent);

                    Intent intentForNewAlarm;
                    PendingIntent pendingIntent;
                    AlarmManager alarmManager;

                    intentForNewAlarm = new Intent(this, TaskAlarmClock.class);
                    alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(this, 0, intentForNewAlarm, 0);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY , pendingIntent);

                    finish();
                }
            } else {
                textViewIfWrongAnswerTask.setText("Wrong. Try Again!");
            }
        } else {
            textViewIfWrongAnswerTask.setText("Enter answer");
        }
    }
}
