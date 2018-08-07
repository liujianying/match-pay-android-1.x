package com.fintech.match.pay.service.notification;

import android.annotation.TargetApi;
import android.service.notification.StatusBarNotification;

import com.fintech.match.pay.service.message.NAbstractMessage;
import com.fintech.match.pay.service.message.NAliPayMessage;
import com.fintech.match.pay.service.message.NWeChatMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class NotificationMessageLibInterface {

    @TargetApi(18)
    public static NAbstractMessage buildMessage(StatusBarNotification statusBarNotification) {
        if ((statusBarNotification == null) || (statusBarNotification.getNotification() == null)) {
            return null;
        }

        try {
            return createMessage(statusBarNotification.getPackageName())
                    .buildMessage(statusBarNotification);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static NAbstractMessage createMessage(String packageName) {
        try {
            Class<? extends NAbstractMessage> observer = ObserverFactory.getObserver(packageName);
            return observer.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private static final class ObserverFactory {

        static final Map<String, Class<? extends NAbstractMessage>> sPackageMap;

        static {
            HashMap localHashMap = new HashMap();
            localHashMap.put("com.tencent.mm", NWeChatMessage.class);
            localHashMap.put("com.eg.android.AlipayGphone", NAliPayMessage.class);
            sPackageMap = Collections.unmodifiableMap(localHashMap);
        }

        static Class<? extends NAbstractMessage> getObserver(String messageKey) {
            if (sPackageMap.containsKey(messageKey)) {
                return sPackageMap.get(messageKey);
            }
            return null;
        }
    }
}

