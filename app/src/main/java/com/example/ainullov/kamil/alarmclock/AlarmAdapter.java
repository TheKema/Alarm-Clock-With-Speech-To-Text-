package com.example.ainullov.kamil.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

class AlarmAdapter extends ArrayAdapter<Alarm> {
    private Context mContext;
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter adapter = this;
    Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Alarm alarm;
    long timeInAdapter;
    int cbResInAdapter;
    boolean onOrOffResInAdapter;


    AlarmAdapter(Context context, int resource, ArrayList<Alarm> alarms) {
        super(context, resource, alarms);
        mContext = context;
        this.alarmList = alarms;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        alarm = alarmList.get(position);
        viewHolder.timeView.setText(alarm.getName());

        cbResInAdapter = Integer.valueOf(formatValue(alarm.getCheckBoxRes()));
        switch (cbResInAdapter) {
            case 1:
                viewHolder.textView.setText(R.string.common);
                break;
            case 2:
                viewHolder.textView.setText(R.string.task);
                break;
            case 3:
                viewHolder.textView.setText(R.string.voice_response);
                break;
        }

        onOrOffResInAdapter = alarmList.get(position).isOnOrOff();
        if (onOrOffResInAdapter) {
            viewHolder.switchButton.setChecked(true);

            timeInAdapter = alarm.getTimeInMillis();
            if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 1)
                intent = new Intent(mContext, AlarmReceiver.class);
            if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 2)
                intent = new Intent(mContext, AlarmTaskReceiver.class);
            if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 3)
                intent = new Intent(mContext, AlarmSpeechToTextReceiver.class);

            if (timeInAdapter <= System.currentTimeMillis()) {
                timeInAdapter = timeInAdapter + AlarmManager.INTERVAL_DAY;
            }
            alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInAdapter, pendingIntent);

            alarm.setOnOrOff(true);
        } else {
            viewHolder.switchButton.setChecked(false);
        }

        viewHolder.constraintLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Delete");
                Edit.setOnMenuItemClickListener(onEditMenu);
            }

            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case 1:
                            //Проверка на включенный свитч, иначе вылетает
                            if (viewHolder.switchButton.isChecked())
                                alarmManager.cancel(pendingIntent);

                            //Работы с переменными для ограничения
                            int cbDeleteLimitPerem = Integer.valueOf(formatValue(alarm.getCheckBoxRes()));
                            if (cbDeleteLimitPerem == 1) MainActivity.cbLimitCommon--;
                            if (cbDeleteLimitPerem == 2) MainActivity.cbLimitWithTask--;
                            if (cbDeleteLimitPerem == 3) MainActivity.cbLimitSpeechToText--;
                            alarmList.remove(position);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                    return true;
                }
            };
        });

        viewHolder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmList.get(position).setOnOrOff(true);

                    timeInAdapter = alarm.getTimeInMillis();
                    if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 1)
                        intent = new Intent(mContext, AlarmReceiver.class);
                    if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 2)
                        intent = new Intent(mContext, AlarmTaskReceiver.class);
                    if (Integer.valueOf(formatValue(alarm.getCheckBoxRes())) == 3)
                        intent = new Intent(mContext, AlarmSpeechToTextReceiver.class);

                    alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmList.get(position).getTimeInMillis(), pendingIntent);
                    alarm.setOnOrOff(true);

                } else {
                    alarmManager.cancel(pendingIntent);
                    alarmList.get(position).setOnOrOff(false);
                }
            }
        });
        return convertView;
    }

    private String formatValue(int count) {
        return String.valueOf(count);
    }

    private class ViewHolder {
        final Switch switchButton;
        final ConstraintLayout constraintLayout;
        final TextView timeView, textView;

        ViewHolder(View view) {
            switchButton = (Switch) view.findViewById(R.id.switchButton);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
            timeView = (TextView) view.findViewById(R.id.timeView);
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }
}
