package com.fintech.match.pay.service.message;

import android.service.notification.StatusBarNotification;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public abstract class KAbstractMessageManager {

    protected abstract void doPost(StatusBarNotification statusBarNotification);

    protected abstract void doRemove(String packageName, int id, String tag);

    boolean isMonitor(String packageName) {
        return "com.tencent.mm".equals(packageName) || "com.eg.android.AlipayGphone".equalsIgnoreCase(packageName);
    }

    public final void onNotificationPosted(final StatusBarNotification statusBarNotification) {
        Disposable disposable = io.reactivex.Observable.just(statusBarNotification)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<StatusBarNotification>() {
                    @Override
                    public void accept(StatusBarNotification statusBarNotification) throws Exception {
                        doPost(statusBarNotification);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public final void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        doRemove(statusBarNotification.getPackageName(), statusBarNotification.getId(), statusBarNotification.getTag());
    }

    public final void onNotificationRemoved(String packageName, int id, String tag) {
        if ((packageName != null) && (isMonitor(packageName))) {
            doRemove(packageName, id, tag);
        }
    }
}
