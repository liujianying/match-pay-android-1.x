package com.fintech.match.pay.service;


import org.junit.Test;
import org.reactivestreams.Publisher;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class RxNotifyTest {

    @Test
    public void test() {


        Flowable.create((FlowableOnSubscribe<String>) emitter -> {
            emitter.onError(new RuntimeException("onErro"));
        }, BackpressureStrategy.BUFFER)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("doOnNext s: " + s);
                    }
                })
//                .retry(3, new Predicate<Throwable>() {
//                    @Override
//                    public boolean test(Throwable throwable) throws Exception {
//                        return false;
//                    }
//                })
//                .retryWhen(throwableFlowable -> throwableFlowable.zipWith(Flowable.range(1, 5), (throwable, integer) -> integer)
//                        .flatMap((Function<Integer, Publisher<?>>) integer -> Flowable.timer((long) Math.pow(2, integer), TimeUnit.SECONDS)))
                .retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {

                        Flowable<Object> map = throwableFlowable.zipWith(Flowable.range(1, 5), (throwable, integer) -> integer)
                                .flatMap(new Function<Integer, Publisher<?>>() {
                                    @Override
                                    public Publisher<?> apply(Integer integer) throws Exception {
                                        System.out.println("delay retry by " + integer + " second(s)");
                                        return Flowable.timer(integer, TimeUnit.SECONDS);
                                    }
                                });

                        return map;
                    }
                })
                .blockingForEach(System.out::println);
//                .forEach(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("s:"+s);
//                    }
//                });

    }

    @Test
    public void test2() {
        String str = "蓝翔小挖付款3330.02元通过扫码向你付款330.01元";
        String pattern = "付款([0-9]+.[0-9][0-9])元$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
            String group = m.group(1);
            float v = Float.parseFloat(group);
            System.out.println(v);
        }
    }

}
