package com.chchi.todo.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by chchi on 10/29/16.
 */

public class EditTimeFragment extends android.support.v4.app.DialogFragment{

    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    public EditTimeFragment(){};
    public void setCallBack(TimePickerDialog.OnTimeSetListener onTimeSetListener){
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Activity needs to implement this interface
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute,DateFormat.is24HourFormat(getActivity()));
    }
}