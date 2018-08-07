package com.fintech.match.pay.ui.me;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.Constants;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ErroCommonConsumer;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.data.model.UserInfoDetail;
import com.fintech.match.pay.main.MainActivity;
import com.fintech.match.pay.service.TradeChannel;
import com.fintech.match.pay.service.compact.JobServiceCompact;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.login.LoginActivity;
import com.fintech.match.pay.ui.me.model.GatheringAcount;
import com.fintech.match.pay.ui.order.OrderListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;

import static android.support.v4.app.NotificationCompat.FLAG_ONGOING_EVENT;
import static com.fintech.match.pay.api.Constants.KEY_USER_NAME;

public class MeActivity extends BaseActivityOld {

    private static final String TAG = BaseActivityOld.class.getSimpleName();

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
//    @BindView(R.id.tv_user_money)
//    TextView tvUserMoney;
//    @BindView(R.id.tv_user_appid)
//    TextView tvUserAppid;
//    @BindView(R.id.tv_user_key)
//    TextView tvUserKey;

    @BindView(R.id.tv_setting_item11)
    TextView tvSettingItem11;
    @BindView(R.id.tv_setting_item12)
    TextView tvSettingItem12;
    @BindView(R.id.tv_setting_item13)
    TextView tvSettingItem13;

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean showHomeAsUp() {
        return false;
    }

    @Override
    public String getCenterTitle() {
        return "个人中心";
    }

    @Override
    public int getContentView() {
        return R.layout.activity_me;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Configuration.noLogin()) {
//            Router.get().build(LoginActivity.class).go(this);
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
            finish();
        }else{
            Constants.baseUrl = "https://" + Configuration.getUserInfoByKey(Constants.KEY_ADDRESS) + "/";
        }

    }

    @Override
    protected void createData() {

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

    @Override
    protected void onStart() {
        super.onStart();
        setNotification();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // 添加常驻通知
    private void setNotification() {
        try {
            createNotificationChannel();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Intent intent = new Intent(this, MeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("温馨提示")
                    .setContentText("请保持本通知常驻通知栏")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            Notification notification = mBuilder.build();
            notification.flags |= FLAG_ONGOING_EVENT;
            notificationManager.notify(notificationId, notification);
        } catch (Exception e) {
        }
    }

    private static final int notificationId = 1001;
    private static final String CHANNEL_ID = "offline_pay";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNotificationServiceEnable()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
//        AlarmCompact.runAlarm(getApplicationContext());
        JobServiceCompact.startJob(getApplicationContext(),1000);
    }

    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }

    @SuppressLint("CheckResult")
    protected void resumeData() {
        Observable<ResultEntity<UserInfoDetail>> userInfo = service().userInfo(new SignRequestBody().sign());
        api(userInfo).subscribe(result -> initUI(result.getResult()), new ErroCommonConsumer(MeActivity.this));

        init();
    }

    private void initUI(UserInfoDetail result) {
        if (result == null) {
            return;
        }
        tvUserName.setText(Configuration.getUserInfoByKey(KEY_USER_NAME));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // not do anything
    }

    @OnClick({R.id.ll_go_order_list, R.id.ll_go_self_info, R.id.ll_go_setting, R.id.ll_go_setting_guide
            , R.id.iv_me_header})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_go_order_list:
                goOrderList();
                break;
            case R.id.ll_go_self_info:
                goSelfInfo();
                break;
            case R.id.ll_go_setting:
                goSetting();
                break;
            case R.id.ll_go_setting_guide:
                goSettingGuide();
                break;
        }
    }

    @OnLongClick({R.id.iv_me_header})
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_me_header:
                goMainActivity();
                return true;
        }
        return false;
    }

    private void goMainActivity() {
        Router.get().build(MainActivity.class).go(MeActivity.this);
    }

    private void goOrderList() {
        Router.get().build(OrderListActivity.class).go(MeActivity.this);
    }

    private void goSelfInfo() {
        Router.get().build(SelfInfoActivity.class).go(MeActivity.this);
    }

    private void goSettingGuide() {
        Router.get().build(SettingGuideActivity.class).go(MeActivity.this);
    }

    private void goSetting() {
        Router.get().build(SettingActivity.class).go(MeActivity.this);
    }

}
