package com.fintech.match.pay.ui.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.fintech.match.pay.App;
import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ErroCommonConsumer;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.common.util.Utils;
import com.fintech.match.pay.service.NotificationListener;
import com.fintech.match.pay.service.compact.JobServiceCompact;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class SelfInfoActivity extends BaseActivityOld {

    @BindView(R.id.btv_change_account)
    TextView btvChangeAccount;
    @BindView(R.id.btv_go_setting)
    TextView btv_go_setting;

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public String getCenterTitle() {
        return "个人资料";
    }

    @Override
    public int getContentView() {
        return R.layout.activity_self_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人资料");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void createData() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void resumeData() {
        btv_go_setting.setText("通知服务 (" +
                (Utils.isServiceAlive(getApplicationContext(), NotificationListener.class) ? "可用" : "不可用") +
                ")");
    }

    @SuppressLint("CheckResult")
    public void logoutAndgoLogin() {
//        AlarmCompact.cancelAlarm(getApplicationContext());
        JobServiceCompact.cancelAllJobs(App.getAppContext());

        Observable<ResultEntity<Boolean>> userInfo = service().logout(new SignRequestBody().sign());
        api(userInfo).subscribe(booleanResultEntity -> goLoginActivity(), new ErroCommonConsumer(SelfInfoActivity.this));
    }

    private void goLoginActivity() {
        Configuration.clearUserInfo();
        Router.get().build(LoginActivity.class).go(SelfInfoActivity.this);
        finish();
    }

    @OnClick({R.id.btv_change_account, R.id.btv_go_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btv_change_account:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("点击确认将退出监控当前商户的交易数据.确定切换?");
                builder.setPositiveButton("确定", (dialog, which) -> logoutAndgoLogin());
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                builder.show();
                break;
            case R.id.btv_go_setting:
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivityForResult(intent, 0);
                break;
        }
    }

    //再次启动服务
    private void onReStartService() {
        Intent intent = new Intent(getApplicationContext(), NotificationListener.class);
        stopService(intent);
        startService(intent);
    }

}
