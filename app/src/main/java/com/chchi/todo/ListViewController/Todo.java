package com.chchi.todo.ListViewController;

import java.util.HashMap;

/**
 * Created by chchi on 10/26/16.
 */

public class Todo {
    public String title;
    public String description;
    public String priority;
    public String date;
    public String time;


    public Todo(HashMap <String,String> map){
        //further validation needed.
        this.title = map.get("title");
        this.description = map.get("description");
        this.priority = map.get("priority");
        this.date = map.get("date");
        this.time = map.get("time");
    }
}
