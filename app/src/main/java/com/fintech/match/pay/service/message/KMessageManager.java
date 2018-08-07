package com.fintech.match.pay.service.message;

import android.annotation.TargetApi;
import android.service.notification.StatusBarNotification;

import com.fintech.match.pay.service.notification.NotificationMessageLibInterface;
import com.fintech.match.pay.service.notify.RxNotify;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class KMessageManager extends KAbstractMessageManager {

    public static KMessageManager getInstance() {
        return SingletonHolder.sInstance;
    }

    //TODO 处理 Notification 消息
    private void handleNotificationMessage(NAbstractMessage message) {
        new RxNotify().sendMessage(message);
    }

    @TargetApi(18)
    protected final void doPost(StatusBarNotification statusBarNotification) {
        NAbstractMessage message = NotificationMessageLibInterface.buildMessage(statusBarNotification);
        if ((statusBarNotification != null) && (message != null)) {
            if (message.getTitle().contains("微信") || message.getTitle().contains("支付宝")) {
                handleNotificationMessage(message);
            }
        }
    }

    @TargetApi(18)
    protected final void doRemove(String packageName, int id, String tag) {
        //not do anything
    }

    private static final class SingletonHolder {
        private static final KMessageManager sInstance = new KMessageManager();
    }

}


