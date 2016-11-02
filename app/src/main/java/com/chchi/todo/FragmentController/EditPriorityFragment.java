package com.chchi.todo.FragmentController;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.chchi.todo.R;

/**
 * Created by chchi on 10/29/16.
 */

public class EditPriorityFragment extends DialogFragment {
    Button saveButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_priority, container, false);

        String [] values =
                {"High","Normal","Low",};
        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        saveButton = (Button) v.findViewById(R.id.prioritySave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("asdasdasdsad",spinner.getSelectedItem()+"");
            }
        });
        return v;

    }
}
