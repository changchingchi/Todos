package com.chchi.todo.ListViewController;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chchi.todo.R;

/**
 * Created by chchi on 10/26/16.
 */

public class TodoViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView description;
    public ImageView priority;
    public TextView time;
    public TextView date;
    public View mView;
    public TodoViewHolder(View view){
        super(view);
        this.mView = view;
        this.title = (TextView) itemView.findViewById(R.id.todo_title);
        this.description = (TextView) itemView.findViewById(R.id.todo_subtitle);
        this.priority = (ImageView) itemView.findViewById(R.id.todo_Image_priority);
        this.time = (TextView) itemView.findViewById(R.id.todo_time);
        this.date = (TextView) itemView.findViewById(R.id.todo_date);
    }
}
