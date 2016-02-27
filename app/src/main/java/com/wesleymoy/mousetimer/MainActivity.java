package com.wesleymoy.mousetimer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public static final PeriodFormatter PRECISE_FORMATTER = new PeriodFormatterBuilder()
            .appendDays()
            .appendSeparator(" ")
            .printZeroAlways()
            .appendHours()
            .appendSeparator(":")
            .minimumPrintedDigits(2)
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();
    private DateTime destination;
    private static final String TIME_ZONE_ID = "America/Los_Angeles";
    private static final String TAG = "TimerWidget";
    private TextView timer;
    private TextView destination2;

    public MainActivity() {
        if (DateTimeZone.getAvailableIDs().contains(TIME_ZONE_ID)) {
            destination = new DateTime(2016, 3, 22, 16, 45, DateTimeZone.forID(TIME_ZONE_ID));
        } else {
            Log.e(TAG, "Time zone " + TIME_ZONE_ID + " not found; using system default instead");
            destination = new DateTime(2016, 3, 22, 16, 45);
        }
    }

    private final Timer timer1 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView) findViewById(R.id.timer);
        DateTime now = DateTime.now();
        Period period = new Period(now, destination, PeriodType.dayTime());

        timer.setText(period.toString(PRECISE_FORMATTER));

        destination2 = (TextView) findViewById(R.id.destination);
        updateDestinationDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DateTime now = DateTime.now();
                        Period period = new Period(now, destination, PeriodType.dayTime());
                        timer.setText(period.toString(PRECISE_FORMATTER));
                    }
                });
            }
        }, 0L, 500L);
    }

    @Override
    protected void onPause() {
        timer1.cancel();
        super.onPause();
    }

    public void onSetDate(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment date = supportFragmentManager.findFragmentByTag("date");
        if (date != null) {
            return;
        }
        SetDateDialogFragment setDateDialogFragment = new SetDateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(SetDateDialogFragment.DESTINATION_DATE, destination);
        setDateDialogFragment.setArguments(args);
        setDateDialogFragment.show(supportFragmentManager, "date");
    }

    public void onSetTime(View view) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment time = supportFragmentManager.findFragmentByTag("time");
        if (time != null) {
            return;
        }
        SetTimeDialogFragment setTimeDialogFragment = new SetTimeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(SetTimeDialogFragment.DESTINATION_TIME, destination);
        setTimeDialogFragment.setArguments(args);
        setTimeDialogFragment.show(getSupportFragmentManager(), "time");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (DateTimeZone.getAvailableIDs().contains(TIME_ZONE_ID)) {
            destination = new DateTime(year, monthOfYear + 1, dayOfMonth, destination.getHourOfDay(), destination.getMinuteOfHour(), DateTimeZone.forID(TIME_ZONE_ID));
        } else {
            Log.e(TAG, "Time zone " + TIME_ZONE_ID + " not found; using system default instead");
            destination = new DateTime(year, monthOfYear + 1, dayOfMonth, destination.getHourOfDay(), destination.getMinuteOfHour());
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (DateTimeZone.getAvailableIDs().contains(TIME_ZONE_ID)) {
            destination = new DateTime(destination.getYear(), destination.getMonthOfYear(), destination.getDayOfMonth(), hourOfDay, minute, DateTimeZone.forID(TIME_ZONE_ID));
        } else {
            Log.e(TAG, "Time zone " + TIME_ZONE_ID + " not found; using system default instead");
            destination = new DateTime(destination.getYear(), destination.getMonthOfYear(), destination.getDayOfMonth(), hourOfDay, minute);
        }
    }

    private void updateDestinationDisplay() {
        CharSequence destie;
        if (destination == null) {
            destie = getString(R.string.destination_not_set);
        } else {
            destie = destination.toString(DateTimeFormat.fullDateTime());
        }
        destination2.setText(destie);
    }
}
