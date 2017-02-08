package com.chchi.todo.FragmentController;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.chchi.todo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chchi on 10/29/16.
 */

public class EditPriorityFragment extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.radioButtonHigh)
    RadioButton mRadioButtonHigh;
    @Bind(R.id.radioButtonMedium)
    RadioButton mRadioButtonMedium;
    @Bind(R.id.radioButtonLow)
    RadioButton mRadioButtonLow;
    @Bind(R.id.prioritySave)
    Button saveButton;
    String priority = "LOW";

    public interface EditPriorityListener{
        void onFinishEditPriority(String priority);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_priority, container, false);
        ButterKnife.bind(this,v);
//
//        String [] values =
//                {"High","Normal","Low",};
//        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
////        spinner.setAdapter(adapter);
        mRadioButtonLow.setOnClickListener(this);
        mRadioButtonMedium.setOnClickListener(this);
        mRadioButtonHigh.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        return v;

    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioButtonHigh:
                priority = "HIGH";
                break;
            case R.id.radioButtonMedium:
                priority = "MEDIUM";
                break;
            case R.id.prioritySave:
                sendBackResult(priority);
                break;
        }
    }

    public void sendBackResult(String result){
        EditPriorityListener listener = (EditPriorityListener) getTargetFragment();
        listener.onFinishEditPriority(result);
        dismiss();
    }
}
