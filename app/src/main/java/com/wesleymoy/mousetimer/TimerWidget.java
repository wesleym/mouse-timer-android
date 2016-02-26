package com.wesleymoy.mousetimer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Implementation of App Widget functionality.
 */
public class TimerWidget extends AppWidgetProvider {

    public static final PeriodFormatter FORMATTER = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendDays()
            .appendSuffix("d")
            .appendSeparator(" ")
            .appendHours()
            .appendSuffix("h")
            .toFormatter();
    private static final DateTime DESTINATION;
    private static final String TIME_ZONE_ID = "America/Los_Angeles";
    private static final String TAG = "TimerWidget";

    static {
        if (DateTimeZone.getAvailableIDs().contains(TIME_ZONE_ID)) {
            DESTINATION = new DateTime(2016, 3, 22, 16, 45, DateTimeZone.forID(TIME_ZONE_ID));
        } else {
            Log.e(TAG, "Time zone " + TIME_ZONE_ID + " not found; using system default instead");
            DESTINATION = new DateTime(2016, 3, 22, 16, 45);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        DateTime now = DateTime.now();
        Period period = new Period(now, DESTINATION, PeriodType.dayTime());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
        views.setTextViewText(R.id.appwidget_text, period.toString(FORMATTER));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

