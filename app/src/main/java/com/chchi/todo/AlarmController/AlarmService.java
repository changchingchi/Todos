package com.chchi.todo.AlarmController;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.chchi.todo.R;

/**
 * Created by chchi on 2/8/17.
 */

public class AlarmService extends IntentService {
    public static final String TODOTEXT = "AlarmService";
    private String mTodoTitle;
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
        mTodoTitle = intent.getStringExtra(TODOTEXT);

        Log.d("AlarmService", "onHandleIntent called");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        Intent i = new Intent(this, ReminderActivity.class);
//        i.putExtra(TodoNotificationService.TODOUUID, mTodoUUID);
//        Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
//        deleteIntent.putExtra(TODOUUID, mTodoUUID);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Finish "+mTodoTitle+" ?")
                .setSmallIcon(R.drawable.notificationicon)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
//                .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
//                .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
//
        manager.notify(100, notification);
    }
}
