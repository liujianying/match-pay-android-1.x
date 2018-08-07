package com.fintech.match.pay.service.receiver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.fintech.match.pay.App;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.ApiProducerModule;
import com.fintech.match.pay.api.ApiService;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ResultCodeConsumer;
import com.fintech.match.pay.api.rx.excep.UnLoginException;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.service.NotificationListener;
import com.fintech.match.pay.ui.login.LoginActivity;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 闹钟: 心跳
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "AlarmReceiver -- heart beats ");
        dispose();
        if (Configuration.isLogin()) {//登录的时候开启心跳
            try {
                heartBeat(context);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    // ------------------------------heartbeat----------------------------------

    private static volatile Disposable disposable;

    public static void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @SuppressLint("CheckResult")
    public void heartBeat(Context context) {
        disposable = Flowable.interval(0, 10, TimeUnit.SECONDS)
                .take(6)
                .doOnNext(aLong -> activeNotificationService(context))
                .filter(aLong -> {
//                    Log.d(TAG, (Configuration.isLogin() && isNotificationListenerAlive(context)) + "---");
                    return Configuration.isLogin() && isNotificationListenerAlive(context);
                })
                .flatMap(new Function<Long, Publisher<ResultEntity<Boolean>>>() {
                    @Override
                    public Publisher<ResultEntity<Boolean>> apply(Long aLong) throws Exception {
                        return ApiProducerModule.create(ApiService.class).heartbeat(new SignRequestBody().sign());
                    }
                })
                .doOnNext(new ResultCodeConsumer<>())
                .subscribe(new Consumer<ResultEntity<Boolean>>() {
                    @Override
                    public void accept(ResultEntity<Boolean> resultEntity) throws Exception {
                        Log.d(TAG, resultEntity.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof UnLoginException) {
                            goLoginActivity();
                        }
                        throwable.printStackTrace();
                    }
                });
    }

    private void goLoginActivity() {
        Configuration.clearUserInfo();
        Router.get().build(LoginActivity.class).go(App.getAppContext());
    }

    // ------------------------------重启监听服务----------------------------------
    public void activeNotificationService(Context context) {
        if (!isNotificationListenerAlive(context)) {
            toggleNotificationListenerService(context);
        }
    }

    private boolean isNotificationListenerAlive(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void toggleNotificationListenerService(Context context) {
        Log.d(TAG, "toggleNotificationListenerService");
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, com.fintech.match.pay.service.NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, com.fintech.match.pay.service.NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    //----------------------------------------------------------

}