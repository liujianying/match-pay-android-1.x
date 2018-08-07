package com.fintech.match.pay.api.rx;

import android.app.Activity;

import com.fintech.match.pay.api.resp.ErroBody;
import com.fintech.match.pay.api.rx.excep.UnLoginException;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.common.util.DialogUtils;
import com.fintech.match.pay.common.widget.loading.LoadingUtils;
import com.fintech.match.pay.ui.login.LoginActivity;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;


public class ErroCommonConsumer implements Consumer<Throwable> {

    private WeakReference<Activity> reference;

    public ErroCommonConsumer(Activity activity) {
        reference = new WeakReference<>(activity);
    }

    @Override
    public void accept(@NonNull Throwable throwable) {
        LoadingUtils.dismiss();//如果有,dismiss

        throwable.printStackTrace();

        if (throwable instanceof HttpException) {
            accept(new ErroBody("response erro", throwable.toString()));
        } else if (throwable instanceof SocketTimeoutException) {
            accept(new ErroBody("net erro", "连接失败,请检查您的网络环境或稍后重试!"));
        } else if (throwable instanceof ConnectException) {
            accept(new ErroBody("net erro", "连接失败,请检查您的网络环境或稍后重试!"));
        } else if (throwable instanceof UnLoginException) {
            goLoginActivity();
        } else {
            accept(new ErroBody("erro", throwable.getMessage()));
        }
    }

    private void goLoginActivity() {
        if (reference != null) {
            Activity activity = reference.get();
            if (activity == null)
                return;

            Configuration.clearUserInfo();

            Router.get().build(LoginActivity.class).go(activity);
            activity.finish();
        }
        return;
    }

    public void accept(@NonNull ErroBody body) {
        if (reference != null) {
            Activity activity = reference.get();
            if (activity == null)
                return;
            DialogUtils.show(activity, body.getMsg());
        }
    }


}
