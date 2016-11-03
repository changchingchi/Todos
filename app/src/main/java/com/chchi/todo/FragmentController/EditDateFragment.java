package com.chchi.todo.FragmentController;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

/**
 * Created by chchi on 10/29/16.
 */

public class EditDateFragment extends android.support.v4.app.DialogFragment{

    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    public EditDateFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }
}