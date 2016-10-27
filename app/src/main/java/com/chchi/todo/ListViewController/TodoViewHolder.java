package com.chchi.todo.ListViewController;

import android.view.View;
import android.widget.TextView;

import com.chchi.todo.R;

/**
 * Created by chchi on 10/26/16.
 */

public class TodoViewHolder {
    public TextView title;
    public TextView subTitle;

    TodoViewHolder(View view){
        this.title = (TextView) view.findViewById(R.id.title);
        this.subTitle = (TextView) view.findViewById(R.id.subtitle);

    }
}
