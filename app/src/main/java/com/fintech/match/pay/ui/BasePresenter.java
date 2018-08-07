package com.fintech.match.pay.ui;

import com.fintech.match.pay.api.ApiProducerModule;
import com.fintech.match.pay.api.ApiService;
import com.fintech.match.pay.api.resp.ResultEntity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter {

    protected CompositeDisposable compositeDisposable;

    ApiService service;

    public <T> Observable<ResultEntity<T>> api(Observable<ResultEntity<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        compositeDisposable.add(disposable);
                    }
                });
    }

    public ApiService service() {
        if (service == null) {
            service = ApiProducerModule.create(ApiService.class);
        }
        return service;
    }

}
