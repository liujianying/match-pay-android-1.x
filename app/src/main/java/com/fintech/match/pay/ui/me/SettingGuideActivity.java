package com.fintech.match.pay.ui.me;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fintech.match.pay.R;
import com.fintech.match.pay.ui.BaseActivityOld;

public class SettingGuideActivity extends BaseActivityOld {

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
        return "设置教程";
    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting_guide;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void createData() {

    }

    @Override
    protected void resumeData() {

    }

}
