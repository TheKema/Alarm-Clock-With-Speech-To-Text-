package com.example.ainullov.kamil.alarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textViewTime;
    Button btnAdd;
    String currentTimeString;
    ArrayList<Alarm> alarms = new ArrayList<>();
    Intent intentAddAlarmClock;
    ListView productList;
    AlarmAdapter adapter;
    long time;
    int cbCheckResultRes;

    // Выбор музыки
    public static int numberOfMusicName = 0;
    SharedPreferences shref;
    final String key = "Key";
    Gson gson = new Gson();
    boolean onOffRes = false;

//    //Переменные для ограничения
//   public static int cbLimitCommon = 0;
//   public static int cbLimitWithTask = 0;
//   public static int cbLimitSpeechToText = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


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
        productList = (ListView) findViewById(R.id.timeList);
        load();
        adapter = new AlarmAdapter(this, R.layout.list_item, alarms);
        productList.setAdapter(adapter);
    }

    Runnable setCurrentTime = new Runnable() {
        public void run() {
            textViewTime.setText(currentTimeString);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if (alarms.size() == 5) {
                    Toast.makeText(this, "You can't create more than 5 alarm clocks", Toast.LENGTH_SHORT).show();
                } else {
                    intentAddAlarmClock = new Intent(this, CreateAlarmClock.class);
                    startActivityForResult(intentAddAlarmClock, 1);
                }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        numberOfMusicName = data.getIntExtra("numberOfMusicName", numberOfMusicName);

        time = data.getLongExtra("time", time);
        cbCheckResultRes = data.getIntExtra("cbCheckResult", cbCheckResultRes);

//        //Ограничение
//        if (cbCheckResultRes == 1) {
//
//            if (cbLimitCommon > 0)
//                Toast.makeText(this, "You use a limited version, so you can't create more than one alarm of each type", Toast.LENGTH_LONG).show();
//            else {
//                cbLimitCommon++;
//                alarms.add(new Alarm(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME), time, cbCheckResultRes, onOffRes));
//            }}
//        if (cbCheckResultRes == 2) {
//            if (cbLimitWithTask > 0)
//                Toast.makeText(this, "You use a limited version, so you can't create more than one alarm of each type", Toast.LENGTH_LONG).show();
//            else{
//                cbLimitWithTask++;
//                alarms.add(new Alarm(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME), time, cbCheckResultRes, onOffRes));
//        }}
//        if (cbCheckResultRes == 3) {
//            if (cbLimitSpeechToText > 0)
//                Toast.makeText(this, "You use a limited version, so you can't create more than one alarm of each type", Toast.LENGTH_LONG).show();
//            else{
//                cbLimitSpeechToText++;
//                alarms.add(new Alarm(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME), time, cbCheckResultRes, onOffRes));
//        }}
                alarms.add(new Alarm(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_TIME), time, cbCheckResultRes, onOffRes));
        adapter.notifyDataSetChanged();
    }


    public void load() {
        shref = getPreferences(MODE_PRIVATE);
        //Если впервые запускаем
        boolean hasVisited = shref.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = shref.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        } else {
            numberOfMusicName = shref.getInt("numberOfMusicName", numberOfMusicName);
            //Переменные для ограничения
//            cbLimitCommon = shref.getInt("cbLimitCommon", cbLimitCommon);
//            cbLimitWithTask = shref.getInt("cbLimitWithTask", cbLimitWithTask);
//            cbLimitSpeechToText = shref.getInt("cbLimitSpeechToText", cbLimitSpeechToText);
            String response = shref.getString(key, "");
            // Боги, я нашел это https://stackoverflow.com/questions/14981233/android-arraylist-of-custom-objects-save-to-sharedpreferences-serializable/40237149#40237149
            alarms = gson.fromJson(response,
                    new TypeToken<List<Alarm>>() {
                    }.getType());
        }
    }

    public void save() {
        shref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor;
        String json = gson.toJson(alarms);
        editor = shref.edit();
        editor.remove(key).commit();
        editor.putString(key, json);
        //Ограничение
        editor.putInt("numberOfMusicName", numberOfMusicName);
//        editor.putInt("cbLimitCommon", cbLimitCommon);
//        editor.putInt("cbLimitWithTask", cbLimitWithTask);
//        editor.putInt("cbLimitSpeechToText", cbLimitSpeechToText);
        editor.commit();
    }
}

