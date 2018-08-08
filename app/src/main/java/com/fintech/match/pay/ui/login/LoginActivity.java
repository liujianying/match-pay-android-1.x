package com.fintech.match.pay.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.Constants;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ErroCommonConsumer;
import com.fintech.match.pay.common.Configuration;
import com.fintech.match.pay.common.Constant;
import com.fintech.match.pay.common.route.Router;
import com.fintech.match.pay.common.util.ToastUtils;
import com.fintech.match.pay.common.util.Utils;
import com.fintech.match.pay.data.db.DB;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.config.ConfigActivity;
import com.fintech.match.pay.ui.init.InitActivity;
import com.fintech.match.pay.ui.me.MeActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.fintech.match.pay.api.Constants.KEY_ADDRESS;
import static com.fintech.match.pay.api.Constants.KEY_LOGIN_TOKEN;
import static com.fintech.match.pay.api.Constants.KEY_MCH_ID;
import static com.fintech.match.pay.api.Constants.KEY_USER_NAME;

/**
 * 登录
 */
public class LoginActivity extends BaseActivityOld implements
        EasyPermissions.PermissionCallbacks {

    private int type;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean showHomeAsUp() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Configuration.noAddress()){
            Router.get().build(ConfigActivity.class).go(this);
            finish();
        }else{
            Constants.baseUrl = "https://" + Configuration.getUserInfoByKey(Constants.KEY_ADDRESS) + "/";
        }

        type = getIntent().getIntExtra("type",0);

        setTitle("离线付");

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String userName;
    private String password;

    @Override
    protected void onResume() {
        super.onResume();
        etUsername.setText(Configuration.getUserInfoByKey(KEY_USER_NAME));

        requestAllPermission();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ALL_PERMISSION) {

        }
    }

    @AfterPermissionGranted(Constant.ALL_PERMISSION)
    public void requestAllPermission() {
        if (!EasyPermissions.hasPermissions(this, Constant.PERMISSIONS_GROUP)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_message),
                    Constant.ALL_PERMISSION, Constant.PERMISSIONS_GROUP);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Utils.goPermissionSetting(this);
        }
    }

    @Override
    protected void createData() {

    }

    @Override
    protected void resumeData() {

    }

    // ------------------------------------------------------------------------------------

    @OnClick({R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                showPregress("登录中...");
                boolean validate = validate();
                if (!validate) {
                    dismissPregress();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void login(Map<String, String> request) {

        Observable<ResultEntity<Map<String, String>>> login = service().login(new SignRequestBody(request));
        api(login).subscribe(result -> loginSuccess(result.getResult()), new ErroCommonConsumer(LoginActivity.this));
    }

    private void loginSuccess(Map<String, String> map) {

        //{"mchId":"422800012","mchName":"兰州拉面","loginToken":"b84c9492d745077f58a7aca04f4c91ef","id":40,"userName":"13888888888@010"}
        String userNameOld = Configuration.getUserInfoByKey(KEY_USER_NAME);
        if (userNameOld!=null && !userNameOld.equals(map.get(KEY_USER_NAME))){//清除本地数据库
            Single.just(1)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            DB.deleteTable(LoginActivity.this);
                        }
                    });
        }
        Configuration.putUserInfo(KEY_USER_NAME, map.get(KEY_USER_NAME));
        Configuration.putUserInfo(KEY_MCH_ID, map.get(KEY_MCH_ID));
        Configuration.putUserInfo(KEY_LOGIN_TOKEN, map.get(KEY_LOGIN_TOKEN));

        switch (type){
            case 1:
                Router.get().build(MeActivity.class).go(this);
                break;
            case 2:
                Router.get().build(InitActivity.class).go(this);
                break;
        }
        finish();
    }

    private boolean validate() {

        userName = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(getApplicationContext(), "请正确输入您的账号");
            return false;
        }

        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(getApplicationContext(), "请正确输入您的密码");
            return false;
        }


        Map<String, String> request = new HashMap<>();
        request.put("userName", userName);
        request.put("password", password);

        login(request);

        return true;
    }

}
