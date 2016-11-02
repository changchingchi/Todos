package com.chchi.todo.ListViewController;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chchi.todo.R;

/**
 * Created by chchi on 10/26/16.
 */

public class TodoViewHolder {
    public TextView title;
    public TextView description;
    public ImageView priority;
    public TextView time;
    public TextView date;
    TodoViewHolder(View view){
        this.title = (TextView) view.findViewById(R.id.todo_title);
        this.description = (TextView) view.findViewById(R.id.todo_subtitle);
        this.priority = (ImageView) view.findViewById(R.id.todo_Image_priority);
        this.time = (TextView) view.findViewById(R.id.todo_time);
        this.date = (TextView) view.findViewById(R.id.todo_date);

    }
}
