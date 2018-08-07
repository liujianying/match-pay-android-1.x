package com.fintech.match.pay.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fintech.match.pay.R;
import com.fintech.match.pay.api.ApiProducerModule;
import com.fintech.match.pay.api.ApiService;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ResultCodeConsumer;
import com.fintech.match.pay.common.route.RequestCode;
import com.fintech.match.pay.common.util.ToastUtils;
import com.fintech.match.pay.common.widget.loading.LoadingUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivityOld extends AppCompatActivity {

    protected Toolbar toolbar;

    protected TextView tv_title;

    public String getCenterTitle() {
        return "";
    }

    protected boolean useToolbar() {
        return false;
    }

    protected boolean showHomeAsUp() {
        return true;
    }

    @LayoutRes
    public abstract int getContentView();

    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(getContentView());

        try {
            bind = ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        initToolbar();

        initView();

        this.compositeDisposable = new CompositeDisposable();
        createData();
    }

    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadingUtils.dismiss();//如果有,dismiss
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        compositeDisposable.clear();
        ToastUtils.cancel();
    }

    protected void initToolbar() {
        if (useToolbar()) {
            toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setNavigationIcon(R.mipmap.icon_back);
                toolbar.setTitle("");
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp());
                toolbar.getBackground().setAlpha(255);
                TextView tv_title = findViewById(R.id.tv_title);
                if (tv_title != null) {
                    tv_title.setText(getCenterTitle());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.RE_LOGIN:
                    reload();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case RequestCode.RE_LOGIN:
                    finish();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void reload() {
        createData();
        resumeData();
    }

    //一次加载的数据
    protected abstract void createData();

    //需要刷新的数据
    protected abstract void resumeData();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // api ------------------------
    public CompositeDisposable compositeDisposable;

    protected ApiService service;

    public <T> Observable<ResultEntity<T>> api(Observable<ResultEntity<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                .doOnNext(new ResultCodeConsumer<>());//在OnNext之前
    }

    public ApiService service() {
        if (service == null) {
            service = ApiProducerModule.create(ApiService.class);
        }
        return service;
    }

    // api ------------------------
    public void showPregress(String message) {
        LoadingUtils.showProgress(this, message);
    }


    public void showPregress() {
        LoadingUtils.showProgress(this);
    }

    public void dismissPregress() {
        LoadingUtils.dismiss();
    }

}
