package com.fintech.match.pay.ui.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ErroCommonConsumer;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.common.util.Utils;
import com.fintech.match.pay.service.NotificationListener;
import com.fintech.match.pay.service.TradeChannel;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.login.LoginActivity;
import com.fintech.match.pay.ui.me.model.GatheringAcount;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

@Deprecated
public class SettingActivity extends BaseActivityOld {

    @BindView(R.id.tv_setting_title1)
    TextView tvSettingTitle1;
    @BindView(R.id.tv_setting_item11)
    TextView tvSettingItem11;
    @BindView(R.id.tv_setting_item12)
    TextView tvSettingItem12;
    @BindView(R.id.tv_setting_item13)
    TextView tvSettingItem13;

    @BindView(R.id.btv_change_account)
    TextView btvChangeAccount;
    @BindView(R.id.btv_go_setting)
    TextView btv_go_setting;

    @Override
    public String getCenterTitle() {
        return "设置";
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void createData() {
        init();
    }

    @Override
    protected void resumeData() {
        btv_go_setting.setText("通知服务 (" +
                (Utils.isServiceAlive(getApplicationContext(), NotificationListener.class) ? "可用" : "不可用") +
                ")");
    }

    @SuppressLint("CheckResult")
    public void init() {
        Observable<ResultEntity<List<GatheringAcount>>> init = service().gatheringSettingInit(new SignRequestBody().sign());
        api(init).subscribe(result -> {

            List<GatheringAcount> list = result.getResult();
            if (list == null)
                return;
            for (GatheringAcount acount : list) {
                if (TradeChannel.alipay == acount.getType()) {
                    if (TextUtils.isEmpty(acount.getAccount())) {
                        tvSettingItem11.setText("未设置");
                    } else {
                        tvSettingItem11.setText(acount.getAccount());
                    }
                } else if (TradeChannel.wechat == acount.getType()) {
                    if (TextUtils.isEmpty(acount.getAccount())) {
                        tvSettingItem12.setText("未设置");
                    } else {
                        tvSettingItem12.setText(acount.getAccount());
                    }
                } else if (TradeChannel.bank == acount.getType()) {
                    if (TextUtils.isEmpty(acount.getAccount())) {
                        tvSettingItem13.setText("未设置");
                    } else {
                        tvSettingItem13.setText(acount.getAccount());
                    }
                }
            }

        }, new ErroCommonConsumer(this));
    }

    @OnClick({R.id.btv_change_account, R.id.btv_go_setting, R.id.ll_setting_item11, R.id.ll_setting_item12, R.id.btv_go_permission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_item11:
                break;
            case R.id.ll_setting_item12:
                break;
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
            case R.id.btv_go_permission:
                goPermissionSetting();
                break;
        }
    }

    private void goPermissionSetting() {
        startActivity(
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", getPackageName(), null)));
    }

    // --- setting ----
    @SuppressLint("CheckResult")
    public void logoutAndgoLogin() {
        Observable<ResultEntity<Boolean>> userInfo = service().logout(new SignRequestBody().sign());
        api(userInfo).subscribe(booleanResultEntity -> goLoginActivity(), new ErroCommonConsumer(SettingActivity.this));
    }

    @Deprecated
    private void goLoginActivity() {
        Configuration.clearUserInfo();
        Router.get().build(LoginActivity.class).go(SettingActivity.this);
        finish();
    }

    //再次启动服务
    private void onReStartService() {
        Intent intent = new Intent(getApplicationContext(), NotificationListener.class);
        stopService(intent);
        startService(intent);
    }

}
