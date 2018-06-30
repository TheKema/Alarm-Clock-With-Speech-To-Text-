package com.example.ainullov.kamil.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class CreateAlarmClock extends AppCompatActivity implements View.OnClickListener {
    TimePicker alarmTimePicker;
    long time;
    Calendar calendar;
    Button btnOk;
    Button btnCancel;
    CheckBox cbCommon;
    CheckBox cbTask;
    CheckBox cbTaskSpeech;
    int cbCheckResult = 1;

    String[] musicName = {"Good Morning (MIUI)", "Nature, water and birds", "Simple melody", "Electronic melody", "Melody of a mechanical alarm clock", "Operation Y", ""};
    int numberOfMusicName = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm_clock);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        cbCommon = (CheckBox) findViewById(R.id.cbCommon);
        cbCommon.setClickable(true);
        cbCommon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbTask.setChecked(false);
                    cbTaskSpeech.setChecked(false);
                }
            }
        });

        cbTask = (CheckBox) findViewById(R.id.cbTask);
        cbTask.setClickable(true);
        cbTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbCommon.setChecked(false);
                    cbTaskSpeech.setChecked(false);
                }
            }
        });

        cbTaskSpeech = (CheckBox) findViewById(R.id.cbTaskSpeech);
        cbTaskSpeech.setClickable(true);
        cbTaskSpeech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbTask.setChecked(false);
                    cbCommon.setChecked(false);
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, musicName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(6);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0)
                    numberOfMusicName = 0;
                if (position == 1)
                    numberOfMusicName = 1;
                if (position == 2)
                    numberOfMusicName = 2;
                if (position == 3)
                    numberOfMusicName = 3;
                if (position == 4)
                    numberOfMusicName = 4;
                if (position == 5)
                    numberOfMusicName = 5;
                if (position == 6) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                if (System.currentTimeMillis() > time) {
                    if (calendar.AM_PM == 0)
                        time = time + (1000 * 60 * 60 * 12);
                    else
                        time = time + (1000 * 60 * 60 * 24);
                }
                Intent intent = new Intent();
                intent.putExtra("time", time);
                if (cbCommon.isChecked())
                    cbCheckResult = 1;
                if (cbTask.isChecked())
                    cbCheckResult = 2;
                if (cbTaskSpeech.isChecked())
                    cbCheckResult = 3;
                intent.putExtra("cbCheckResult", cbCheckResult);
//                intent.putExtra("switchResult", switchResult);
                intent.putExtra("numberOfMusicName", numberOfMusicName);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }


}
