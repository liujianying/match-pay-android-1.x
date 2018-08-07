package com.fintech.match.pay.service.notify;

import android.util.Log;

import com.fintech.match.pay.App;
import com.fintech.match.pay.api.ApiProducerModule;
import com.fintech.match.pay.api.ApiService;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ResultCodeConsumer;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.data.source.TradeLog;
import com.fintech.match.pay.data.source.local.MatchPayDatabase;
import com.fintech.match.pay.data.source.local.TradeLogDao;
import com.fintech.match.pay.service.message.IMessage;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

//必须 在子线程中 运行
public class RxNotify {

    public void sendMessage(IMessage message) {

        TradeLog log = message.createLog();
        postRemote(log);//写远程

    }

    private void postRemote(TradeLog log) {

        if (Configuration.noLogin()) {//未登录
            return;
        }

        MessageRequestBody request = log.createSignRequestBody();
        ApiProducerModule
                .create(ApiService.class)
                .notifyLog(request)
                .doOnNext(new ResultCodeConsumer<>())
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {

                        return throwableObservable
                                .zipWith(Observable.range(1, 6),
                                        new BiFunction<Throwable, Integer, Integer>() {
                                            @Override
                                            public Integer apply(Throwable throwable, Integer integer) throws Exception {
                                                Log.d("notify retry", "delay retry by " + integer + " times " + DateFormat.getTimeInstance().format(System.currentTimeMillis()));

                                                if (integer == 6) {
                                                    log.setPost(false);
                                                    log.setErroMsg(throwable.getMessage() + " | times " + integer);
                                                    insertLocal(log);
                                                }

                                                return integer;
                                            }
                                        })
                                .flatMap(new Function<Integer, ObservableSource<?>>() {

                                    @Override
                                    public ObservableSource<?> apply(Integer integer) throws Exception {
                                        return Observable.timer((long) Math.pow(2, integer), TimeUnit.SECONDS);
                                    }

                                });
                    }
                })
                .blockingForEach(new Consumer<ResultEntity>() {
                    @Override
                    public void accept(ResultEntity resultEntity) {
                        Log.d("notify success", log.getContent() + " success");
                    }
                });
    }

    // ---------------- post local -----------------------

    private void updateLocal(TradeLog log) {//更新状态
        MatchPayDatabase database = MatchPayDatabase.getInstance(App.getAppContext());
        TradeLogDao dao = database.logDao();
        try {
            dao.update(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertLocal(TradeLog log) {
        MatchPayDatabase database = MatchPayDatabase.getInstance(App.getAppContext());
        TradeLogDao dao = database.logDao();
        try {
            dao.insertAll(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
