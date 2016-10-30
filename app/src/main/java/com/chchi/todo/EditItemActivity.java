package com.chchi.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chchi.todo.FragmentController.EditDateFragment;
import com.chchi.todo.FragmentController.EditPriorityFragment;
import com.chchi.todo.FragmentController.EditTimeFragment;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    TextView datePicker;
    TextView timePicker;
    TextView priorityPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datePicker = (TextView) findViewById(R.id.textViewDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        timePicker = (TextView) findViewById(R.id.textViewTime);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        priorityPicker = (TextView) findViewById(R.id.textViewPriority);
        priorityPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriorityPickerDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edititem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.save){
        //handle all data and save it into SQLite
        }
        return super.onOptionsItemSelected(item);
    }

    // attach to an onclick handler to show the date picker
    private void showDatePickerDialog() {
        EditDateFragment newFragment = new EditDateFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        EditTimeFragment newFragment = new EditTimeFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    private void showPriorityPickerDialog(){
        EditPriorityFragment newFragment = new EditPriorityFragment();
        newFragment.show(getFragmentManager(),"priorityPicker");
    }

    // handle the date selected by implement onDateSetLister.
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        datePicker.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timePicker.setText(hourOfDay+":"+minute);
    }
}
