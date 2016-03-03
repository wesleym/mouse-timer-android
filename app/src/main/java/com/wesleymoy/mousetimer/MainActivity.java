package com.wesleymoy.mousetimer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

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
    private static final DateTime DESTINATION;
    private static final String TIME_ZONE_ID = "America/Los_Angeles";
    private static final String TAG = "TimerWidget";
    private static final DateTimeZone TIME_ZONE = DateTimeZone.forID(TIME_ZONE_ID);

    static {
        if (DateTimeZone.getAvailableIDs().contains(TIME_ZONE_ID)) {
            DESTINATION = new DateTime(2016, 3, 22, 16, 45, TIME_ZONE);
        } else {
            Log.e(TAG, "Time zone " + TIME_ZONE_ID + " not found; using system default instead");
            DESTINATION = new DateTime(2016, 3, 22, 16, 45);
        }
    }

    private Timer timer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView timer = (TextView) findViewById(R.id.timer);
        DateTime now = DateTime.now();
        Duration duration = new Duration(now, DESTINATION);

        Period period = duration.toPeriod(PeriodType.dayTime());
        int hours = period.getHours();
        int days = hours / 24;
        hours %= 24;
        period = period.withHours(hours).withDays(days);

        timer.setText(period.toString(PRECISE_FORMATTER));
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView timer = (TextView) findViewById(R.id.timer);
                        DateTime now = DateTime.now();
                        Duration duration = new Duration(now, DESTINATION);

                        Period period = duration.toPeriod(PeriodType.dayTime());
                        int hours = period.getHours();
                        int days = hours / 24;
                        hours %= 24;
                        period = period.withHours(hours).withDays(days);

                        timer.setText(period.toString(PRECISE_FORMATTER));
                    }
                });
            }
        }, 0L, 500L);
    }

    @Override
    protected void onPause() {
        timer1.cancel();
        timer1 = null;
        super.onPause();
    }
}
