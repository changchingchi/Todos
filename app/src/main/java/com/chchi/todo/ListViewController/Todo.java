package com.chchi.todo.ListViewController;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.HashMap;

/**
 * Created by chchi on 10/26/16.
 */

public class Todo implements Parcelable{


    private String id;
    public String priority;
    public String title;
    public String date;
    public String time;
    public String description;
    public View view;
    public int position;
    public Todo(){

    }
    public Todo(HashMap <String,String> map){
        //further validation needed.
        this.title = map.get("title");
        this.description = map.get("description");
        this.priority = map.get("priority");
        this.date = map.get("date");
        this.time = map.get("time");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(priority);
    }
    public static final Parcelable.Creator<Todo> CREATOR
            = new Parcelable.Creator<Todo>() {
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    private Todo(Parcel in) {
        //Follow FIFO
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        priority = in.readString();
    }
}
