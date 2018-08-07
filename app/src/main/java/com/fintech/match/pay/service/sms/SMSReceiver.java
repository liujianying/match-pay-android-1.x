package com.fintech.match.pay.service.sms;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.fintech.match.pay.common.util.ToastUtils;
import com.fintech.match.pay.service.notify.RxNotify;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SMSReceiver extends BroadcastReceiver {

    public static final String TAG = "SMSReceiver";

    /**
     * Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] objs = (Object[]) bundle.get("pdus");
            SmsMessage[] smsMessages = new SmsMessage[objs.length];
            String address = "";
            String content = "";
            long time = System.currentTimeMillis();
            for (int i = 0; i < objs.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
                address = smsMessages[i].getOriginatingAddress();
                content += smsMessages[i].getMessageBody(); //短信的内容
                time = smsMessages[i].getTimestampMillis();
            }

            Log.d(TAG, "号码：" + address + " 内容：" + content + " 时间：" + time);

            ToastUtils.show(address + ":" + content);

            if (address.startsWith("9") && address.length() == 5) {
                NSmsMessage nSmsMessage = new NSmsMessage(address, content, time);
                postRemote(nSmsMessage);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void postRemote(NSmsMessage message) {
        Observable.just(message)
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        nSmsMessage -> new RxNotify().sendMessage(nSmsMessage),
                        throwable -> Log.d(TAG, throwable.getMessage())
                );
    }

}