package com.chchi.todo.ListViewController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chchi.todo.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.chchi.todo.R.layout.todo;

/**
 * Created by chchi on 10/26/16.
 */

public class TodoAdaptor extends BaseAdapter {

    ArrayList<Todo> arrayList;
    Context context;
    public TodoAdaptor(Context context){
        this.context = context;
        arrayList = new ArrayList<>();
        HashMap<String, String> todoItem = new HashMap<>();
        todoItem.put("title","Call Nina");
        todoItem.put("description","call her that we need to do weekly grocery!call her that we need to do weekly grocery!");
        todoItem.put("time","12:23");
        todoItem.put("date","12/29/1987");
        todoItem.put("priority","high");
        HashMap<String, String> todoItem2 = new HashMap<>();
        todoItem2.put("title","Call Nina");
        todoItem2.put("description","call her that we need to do weekly grocery!call her that we need to do weekly grocery!");
        todoItem2.put("time","12:23");
        todoItem2.put("date","12/29/1987");
        todoItem2.put("priority","low");
        arrayList.add(new Todo(todoItem));
        arrayList.add(new Todo(todoItem2));


    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TodoViewHolder viewHolder ;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(todo,parent,false);
            // we let inflator init only when its 1st time.
            viewHolder = new TodoViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (TodoViewHolder) view.getTag();
        }

        Todo todo = arrayList.get(position);
        viewHolder.title.setText(todo.title);
        viewHolder.description.setText(todo.description);
        viewHolder.time.setText(todo.time);
        viewHolder.date.setText(todo.date);
        switch (todo.priority){
            case "high":
                viewHolder.priority.setImageResource(R.drawable.priority_high);
                break;
            case "low":
                viewHolder.priority.setImageResource(R.drawable.priority_low);
                break;
            default:
                viewHolder.priority.setImageResource(R.drawable.priority_normal);
        }
        return view;
    }
}
