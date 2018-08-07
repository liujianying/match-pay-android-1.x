package com.fintech.match.pay.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.fintech.match.pay.service.message.KMessageManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@TargetApi(18)
public final class NotificationListener extends NotificationListenerService {

    private static final Set<String> PACKAGES_LOWER_CASE;
    private static final Lock sLock = new ReentrantLock();

    static {
        HashSet localHashSet = new HashSet();
        localHashSet.add("com.tencent.mm");
        localHashSet.add("com.eg.android.AlipayGphone".toLowerCase());
        PACKAGES_LOWER_CASE = Collections.unmodifiableSet(localHashSet);
    }

    public final void onCreate() {

        Log.d("NotificationListener", "onCreate");

        super.onCreate();
    }

    public IBinder onBind(Intent intent) {

        Log.d("NotificationListener", "onBind");

        return super.onBind(intent);
    }

    @Override
    public final void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (statusBarNotification == null || statusBarNotification.getNotification() == null) {
            return;
        }

        String str = statusBarNotification.getPackageName();
        if (TextUtils.isEmpty(str)) {
            return;
        }

        System.out.println("--------:" + statusBarNotification.getNotification().tickerText);
        System.out.println();

        if (PACKAGES_LOWER_CASE.contains(str.toLowerCase())) {//监控 微信 and 支付宝  Notification
            sLock.lock();
            try {
                onPostedAsync(statusBarNotification);
                return;
            } finally {
                sLock.unlock();
            }
        }
    }

    protected void onPostedAsync(StatusBarNotification statusBarNotification) {
        KMessageManager.getInstance().onNotificationPosted(statusBarNotification);
    }


    @Override
    public final void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        KMessageManager.getInstance().onNotificationRemoved(statusBarNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d("NotificationListener", "onStartCommand");

        return Service.START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        Log.d("NotificationListener", "onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("NotificationListener", "onDestroy");

    }

}


