package com.chchi.todo.AlarmController;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chchi.todo.ListViewController.Todo;
import com.chchi.todo.MainActivity;
import com.chchi.todo.R;

/**
 * Created by chchi on 2/8/17.
 */

public class AlarmService extends IntentService{
    public static final String TODOTEXT = "AlarmService";
    public static final String TODOUUID = "AlarmSeriveUUID";
    public static final String TODOBUNDLE = "AlarmBundle";
    private String mTodoTitle;
    private String mTodoUUID;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmService(String name) {
        super(name);
    }
    public AlarmService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        intent.getBundleExtra(AlarmService.TODOBUNDLE).getParcelable("alarmTodo")
        Bundle mBundle = intent.getBundleExtra(AlarmService.TODOBUNDLE);
        Todo alarmTodo = mBundle.getParcelable("alarmTodo");
        mTodoTitle = alarmTodo.getTitle();
        Log.d("AlarmService", "onHandleIntent called");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(AlarmService.TODOBUNDLE, mBundle);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action actionDidit = new NotificationCompat.Action(0,"DID IT",pi);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Finish "+mTodoTitle+" ?")
                .setSmallIcon(R.drawable.notepad)
                .setDefaults(Notification.DEFAULT_SOUND)
                .addAction(actionDidit)
                .setAutoCancel(true)
                .build();
        manager.notify(100, notification);
    }
}
