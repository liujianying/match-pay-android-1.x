package com.fintech.match.pay.service.job;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.fintech.match.pay.service.compact.JobServiceCompact;
import com.fintech.match.pay.ui.login.LoginActivity;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {

        Log.i(TAG, "---------- create -----------");

        super.onCreate();
    }


    @Override
    public boolean onStartJob(final JobParameters params) {
        doJob();
        return false;
    }


    private void doJob() {
        dispose();
        if (Configuration.isLogin()) {//登录的时候开启心跳
            try {
                heartBeat(getApplicationContext());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void reJob() {
        JobServiceCompact.startJob(getApplicationContext(), 60 * 1000);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        reJob();
        Log.i(TAG, "---------- destroy -----------");

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
        if (Configuration.noLogin()) {
            return;
        }
        disposable = Flowable.interval(0, 10, TimeUnit.SECONDS)
                .take(6)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Long>() {
                    private boolean isToggle = false;

                    @Override
                    public boolean test(Long aLong) throws Exception {
                        boolean alive = isNotificationListenerAlive(context);
                        if (!alive && !isToggle) {
                            isToggle = true;
                            toggleNotificationListenerService(context);
                        }
                        return alive;
                    }
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
