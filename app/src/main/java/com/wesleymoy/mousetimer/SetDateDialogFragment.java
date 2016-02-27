package com.wesleymoy.mousetimer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import org.joda.time.DateTime;

public class SetDateDialogFragment extends AppCompatDialogFragment {
    public static final String DESTINATION_DATE = "destinationDate";
    private static final String TAG = "SetDateDialogFragment";
    private DatePickerDialog.OnDateSetListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof DatePickerDialog.OnDateSetListener)) {
            return;
        }
        listener = (DatePickerDialog.OnDateSetListener) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        DateTime destinationDate = (DateTime) arguments.getSerializable(DESTINATION_DATE);
        if (destinationDate == null) {
            Log.e(TAG, "Destination time argument not provided; defaulting to current time");
            destinationDate = DateTime.now();
        }
        return new DatePickerDialog(getContext(), listener, destinationDate.getYear(), destinationDate.getMonthOfYear() - 1, destinationDate.getDayOfMonth());
    }
}
