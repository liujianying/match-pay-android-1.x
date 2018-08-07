package com.fintech.match.pay.service.compact;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fintech.match.pay.service.receiver.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;

public class AlarmCompact {

    //启动心跳
    public static void runAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("match.pay.android.beats");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 60000, pendingIntent);
    }

    //取消心跳
    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("match.pay.android.beats");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        AlarmReceiver.dispose();
    }

}
