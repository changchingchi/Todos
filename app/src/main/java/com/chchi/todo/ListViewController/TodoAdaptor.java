package com.chchi.todo.ListViewController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chchi.todo.R;

import java.util.ArrayList;

/**
 * Created by chchi on 10/26/16.
 */

public class TodoAdaptor extends BaseAdapter {

    ArrayList<Todo> arrayList;
    Context context;
    public TodoAdaptor(Context context){
        this.context = context;
        arrayList = new ArrayList<Todo>();
        arrayList.add(new Todo("title1","subtitle1"));
        arrayList.add(new Todo("title2","subtitle2"));
        arrayList.add(new Todo("title3","subtitle3"));
        arrayList.add(new Todo("title4","subtitle4"));

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
        TodoViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.todo,parent,false);
            // we let inflator init only when its 1st time.
            viewHolder = new TodoViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (TodoViewHolder) view.getTag();
        }

        Todo todo = arrayList.get(position);
        viewHolder.title.setText(todo.title);
        viewHolder.subTitle.setText(todo.subTitle);
        return view;
    }
}
