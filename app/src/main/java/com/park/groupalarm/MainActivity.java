package com.park.groupalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{

    private AlarmManager aManager;
    private DatePicker dPicker;
    private TimePicker tPicker;
    private GregorianCalendar gCalendar;
    private TextView tView;
    int a;
    //private NotificationManager nManager;

    private void setAlarm() {
        aManager.set(AlarmManager.RTC_WAKEUP, gCalendar.getTimeInMillis(), pendingIntent());
        Toast.makeText(getApplicationContext(), "alarm set", Toast.LENGTH_SHORT).show();
    }

    private void resetAlarm() {
        aManager.cancel(pendingIntent());
        Toast.makeText(getApplicationContext(), "alarm reset", Toast.LENGTH_SHORT).show();
    }

    private PendingIntent pendingIntent() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
    }

    @Override
    public void onDateChanged (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gCalendar.set(year, monthOfYear, dayOfMonth, tPicker.getHour(), tPicker.getMinute());
        } else {
            gCalendar.set(year, monthOfYear, dayOfMonth, tPicker.getCurrentHour(), tPicker.getCurrentMinute());
        }
    }

    @Override
    public void onTimeChanged (TimePicker view, int hour, int minute) {
        gCalendar.set(dPicker.getYear(), dPicker.getMonth(), dPicker.getDayOfMonth(),
                hour, minute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        aManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        gCalendar = new GregorianCalendar();
        tView = (TextView)findViewById(R.id.textView);

        setContentView(R.layout.activity_main);

        Button btn1 = (Button)findViewById(R.id.set);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                setAlarm();
            }
        });

        btn1 = (Button)findViewById(R.id.reset);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                resetAlarm();
            }
        });

        dPicker = (DatePicker)findViewById(R.id.date_picker);
        dPicker.init(gCalendar.get(Calendar.YEAR), gCalendar.get(Calendar.MONTH),
                gCalendar.get(Calendar.DAY_OF_MONTH), this);
        tPicker = (TimePicker)findViewById(R.id.time_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tPicker.setHour(gCalendar.get(Calendar.HOUR_OF_DAY));
            tPicker.setMinute(gCalendar.get(Calendar.MINUTE));
        } else {
            tPicker.setCurrentHour(gCalendar.get(Calendar.HOUR_OF_DAY));
            tPicker.setCurrentMinute(gCalendar.get(Calendar.MINUTE));
        }
        tPicker.setOnTimeChangedListener(this);
    }
}
