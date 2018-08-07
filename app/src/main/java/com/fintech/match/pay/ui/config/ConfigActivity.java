package com.fintech.match.pay.ui.config;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.Constants;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.me.MeActivity;
import com.fintech.match.pay.ui.select.SelectActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ConfigActivity extends BaseActivityOld {
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.etAddress)
    EditText etAddress;

    @Override
    public int getContentView() {
        return R.layout.activity_config;
    }


    @Override
    protected void createData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @OnClick({R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                Constants.baseUrl = "https://" + etAddress.getText().toString() + "/";

                Map<String, String> request = new HashMap<>();
                request.put("userName", "13397610459@001");
                request.put("password", "123456");
                check(request);
                break;
            default:
                break;
        }
    }

    private void check(Map<String, String> request) {

        Observable<ResultEntity<Map<String, String>>> check = service().login(new SignRequestBody(request));
        api(check).subscribe(new Observer<ResultEntity<Map<String, String>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                showPregress("登录中...");
            }

            @Override
            public void onNext(ResultEntity<Map<String, String>> mapResultEntity) {
                Configuration.putUserInfo(Constants.KEY_ADDRESS,etAddress.getText().toString());
                Router.get().build(SelectActivity.class).go(ConfigActivity.this);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                dismissPregress();
                e.printStackTrace();
                if (e.getMessage().contains("用户不存在")){
                    Configuration.putUserInfo(Constants.KEY_ADDRESS,etAddress.getText().toString());
                    Router.get().build(SelectActivity.class).go(ConfigActivity.this);
                    finish();
                }
            }

            @Override
            public void onComplete() {
                dismissPregress();
            }
        });
    }
}
