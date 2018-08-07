package com.fintech.match.pay.api.rx;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class RetryWithDelay implements
        Function<Observable<? extends Throwable>, Observable<?>> {


    @SuppressLint("CheckResult")
    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable
                .zipWith(Observable.range(1, 6), (BiFunction<Throwable, Integer, Integer>) (throwable, integer) -> integer)
                .flatMap((Function<Integer, ObservableSource<?>>) retryCount -> {
                    Log.d("RetryWithDelay", " retry by " + retryCount + " times : " + DateFormat.getTimeInstance().format(System.currentTimeMillis()));
                    return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS);
                });
    }

}


