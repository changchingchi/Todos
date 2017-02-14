package com.chchi.todo.FragmentController;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chchi.todo.AlarmController.AlarmService;
import com.chchi.todo.FireBaseUtils.Firebase;
import com.chchi.todo.ListViewController.Todo;
import com.chchi.todo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by chchi on 11/2/16.
 */

public class EditItemFragment extends DialogFragment implements EditPriorityFragment.EditPriorityListener{
    private static final String USER_CHILD = "users" ;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 29;
    //ButterKnife Binding
    @Bind(R.id.taskET)
    EditText mTaskEditText;
    @Bind(R.id.textViewDate)
    TextView mDateTextView;
    @Bind(R.id.textViewTime)
    TextView mTimeTextView;
    @Bind(R.id.textViewPriority)
    TextView mPriorityTextView;
    @Bind(R.id.EditTextDescription)
    EditText mDescriptionEditText;
    @Bind(R.id.switchAlarm)
    Switch mAlarmSwitch;

    Todo ClickedTodo;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    public DatabaseReference mFirebaseDatabaseReference;
    private boolean isEditedItem;
    private String ClickedTodoKey=null;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    public static EditItemFragment newInstance() {
        return new EditItemFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edit_item, container);
        setHasOptionsMenu(true);
        ButterKnife.bind(this,rootView);

        if(getArguments()!=null){
            //when users clicked the item, we need to populate the item back to fragment.
            isEditedItem = true;
            ClickedTodo = getArguments().getParcelable("todoItem");
            ClickedTodoKey = getArguments().getString("todoKey");
            if(ClickedTodo.getTitle()!=null || !ClickedTodo.getTitle().isEmpty()) {
                mTaskEditText.setText(ClickedTodo.getTitle());
            }
            if(ClickedTodo.getDescription()!=null || !ClickedTodo.getDescription().isEmpty()) {
                mDescriptionEditText.setText(ClickedTodo.getDescription());
            }
            if(ClickedTodo.getTime()!=null || !ClickedTodo.getTime().isEmpty()) {
                mTimeTextView.setText(ClickedTodo.getTime());
            }
            if(ClickedTodo.getDate()!=null || !ClickedTodo.getDate().isEmpty()) {
                mDateTextView.setText(ClickedTodo.getDate());
            }
            if(ClickedTodo.getPriority()!=null || !ClickedTodo.getPriority().isEmpty()) {
                mPriorityTextView.setText(ClickedTodo.getPriority());
            }
            Boolean isAlarm = ClickedTodo.getAlarm() ? (Boolean.TRUE) : (Boolean.FALSE);
            mAlarmSwitch.setChecked(isAlarm);
        }

        mFirebaseDatabaseReference = Firebase.getDatabase().getReference();
        mFirebaseAuth = Firebase.getFirebaseAuth();
        mFirebaseUser= mFirebaseAuth.getCurrentUser();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        mPriorityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriorityPickerDialog();
            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edititem,menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save:
                PostToFireBase();
                return true;
            case R.id.photoCamera:
                AttachPhotoToNote();
            default:
                break;
        }

        return false;
    }

    private void AttachPhotoToNote() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            //REQUEST CAMERA PERMISSION
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void PostToFireBase() {
        if(isValidInput()){
            HashMap<String,String> map = new HashMap<>();
            map.put("title",mTaskEditText.getText().toString());
            map.put("description",mDescriptionEditText.getText().toString());
            map.put("date",mDateTextView.getText().toString());
            map.put("time",mTimeTextView.getText().toString());
            map.put("priority",mPriorityTextView.getText().toString());

            //flags to control alarm
            map.put("isAlarm",mAlarmSwitch.isChecked()+"");
            map.put("finish",Boolean.FALSE.toString());

            Todo todo = new Todo(map);

            if(isEditedItem){
                //user wants to update item.
                mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid()).child(ClickedTodoKey).setValue(todo);
                //reset flag for new edit item.
                isEditedItem = false;
            }else{
                //user wants to add new item.
//                mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid())
//                        .push().setValue(todo);

                ClickedTodoKey = mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid())
                        .push().getKey();

                mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid())
                        .child(ClickedTodoKey).setValue(todo);
            }
            if(mAlarmSwitch.isChecked()){
                setAlarm(todo,stringToDate(mDateTextView.getText().toString()+mTimeTextView.getText().toString()).getTime());
            }else if(!mAlarmSwitch.isChecked()){
                //in case we need to reset
                todo.setAlarm(Boolean.FALSE);
                todo.setFinish(Boolean.FALSE);
            }
            this.dismiss();
        }else{
            Toast.makeText(getActivity(),"invalid inputs", Toast.LENGTH_LONG).show();
        }
    }

    private void setAlarm(Todo todo, long timeInMillis) {
            Intent i = new Intent(getActivity(), AlarmService.class);
            Bundle alarmBundle = new Bundle();
            alarmBundle.putParcelable("alarmTodo",todo);
            //pass the firebase key
            alarmBundle.putString("clickedTodoKey",ClickedTodoKey);
            i.putExtra(AlarmService.TODOBUNDLE,alarmBundle);
            AlarmManager am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(getContext(),399, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        Log.d("test", "setAlarm "+399+" time: "+timeInMillis+" PI "+pi.toString());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
         MenuItem item= menu.findItem(R.id.save);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    // attach to an onclick handler to show the date picker
    private void showDatePickerDialog() {
        EditDateFragment newFragment = new EditDateFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.setCallBack(onDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        EditTimeFragment newFragment = new EditTimeFragment();
        newFragment.setCallBack(onTime);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showPriorityPickerDialog() {
        EditPriorityFragment newFragment = new EditPriorityFragment();
        // SETS the target fragment for use later when sending results
        newFragment.setTargetFragment(this, 300);
        newFragment.show(getFragmentManager(), "priorityPicker");
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String min;
            min = (minute<10) ? "0"+minute : minute+"";
            mTimeTextView.setText(hourOfDay + ":" + min);

        }
    };
    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month, day;
            month = (monthOfYear<10) ? "0"+(monthOfYear+1) : (monthOfYear+1)+"";
            day = (dayOfMonth<10) ? "0"+(dayOfMonth) : (dayOfMonth)+"";
            mDateTextView.setText(year + "/" + (month) + "/" + day);

        }
    };

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public boolean isValidInput() {
//        return !(mTaskEditText.getText().toString().isEmpty()||
//                mDateTextView.getText().toString().isEmpty()||
//                mTimeTextView.getText().toString().isEmpty());
        return !(mTaskEditText.getText().toString().isEmpty());
    }

    @Override
    public void onFinishEditPriority(String priority) {
//        Toast.makeText(getActivity(), "Hi, "+priority, Toast.LENGTH_SHORT).show();
        mPriorityTextView.setText(priority);
    }

    private Date stringToDate(String s){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/ddHH:mm");
        try {
             date = format.parse(s);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
        }
        return date;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
            mDescriptionEditText.setCompoundDrawablesWithIntrinsicBounds(null,null,null,new BitmapDrawable(getResources(),imageBitmap));
        }    }
}