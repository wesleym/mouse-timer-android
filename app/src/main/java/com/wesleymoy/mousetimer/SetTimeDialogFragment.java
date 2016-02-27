package com.wesleymoy.mousetimer;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import org.joda.time.DateTime;

public class SetTimeDialogFragment extends AppCompatDialogFragment {
    public static final String DESTINATION_TIME = "destinationTime";
    private static final String TAG = "SetTimeDialogFragment";
    private TimePickerDialog.OnTimeSetListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof TimePickerDialog.OnTimeSetListener)) {
            return;
        }
        listener = (TimePickerDialog.OnTimeSetListener) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        DateTime destinationTime = (DateTime) arguments.getSerializable(DESTINATION_TIME);
        if (destinationTime == null) {
            Log.e(TAG, "Destination time argument not provided; defaulting to current time");
            destinationTime = DateTime.now();
        }
        return new TimePickerDialog(getContext(), listener, destinationTime.getHourOfDay(), destinationTime.getMinuteOfHour(), false);
    }
}
