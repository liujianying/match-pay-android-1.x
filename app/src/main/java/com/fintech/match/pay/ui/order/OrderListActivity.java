package com.fintech.match.pay.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.fintech.match.pay.R;
import com.fintech.match.pay.ui.BaseActivityOld;
import com.fintech.match.pay.ui.order.adapter.OrderListPagerAdapter;

import butterknife.BindView;

public class OrderListActivity extends BaseActivityOld {

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
        return "订单";
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_list;
    }

    // ---------------------------------------------------------------------

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager pager;

    OrderListPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mAdapter = new OrderListPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setupWithViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void createData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void reload() {
        mAdapter.notifyDataSetChanged();
    }
}
