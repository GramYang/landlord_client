package com.gram.landlord_client.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gram.landlord_client.R;

public class NotificationUtil {

    public static void jumpActivity(Context context, Class<?> cls, String action, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, cls);
        PendingIntent pIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context, "switch_activity")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("landlord")
                .setContentText(action)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(id, notification);
    }
}
