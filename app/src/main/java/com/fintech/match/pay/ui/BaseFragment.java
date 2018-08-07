package com.fintech.match.pay.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fintech.match.pay.api.ApiProducerModule;
import com.fintech.match.pay.api.ApiService;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ResultCodeConsumer;
import com.fintech.match.pay.common.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseFragment extends Fragment {

    Unbinder bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            bind = ButterKnife.bind(this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        compositeDisposable.clear();
        ToastUtils.cancel();
    }


    // api ------------------------
    protected CompositeDisposable compositeDisposable;

    ApiService service;

    public <T> Observable<ResultEntity<T>> api(Observable<ResultEntity<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }
                })
                .doOnNext(new ResultCodeConsumer<ResultEntity<T>>());
    }

    public ApiService service() {
        if (service == null) {
            service = ApiProducerModule.create(ApiService.class);
        }
        return service;
    }
    // api ------------------------
}
