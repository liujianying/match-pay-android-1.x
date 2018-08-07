package com.fintech.match.pay.ui.order.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fintech.match.pay.ui.order.OrderListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息中心,pager页适配器
 */
public class OrderListPagerAdapter extends FragmentPagerAdapter {

    /**
     * 订单状态 支付中 10 通知中 20 成功 30 关闭 40
     */
    public static final String[] CONTENT = new String[]{"全部", "支付中", "通知中", "成功", "关闭"};
    public static final int[] types = new int[]{0, 10, 20, 30, 40};
    public List<OrderListFragment> fragmentList = new ArrayList<>();

    public OrderListPagerAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < CONTENT.length; i++) {
            OrderListFragment fragment = OrderListFragment.newInstance(types[i]);
            fragmentList.add(fragment);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }


    @Override
    public void notifyDataSetChanged() {
        for (OrderListFragment fragment : fragmentList) {
            fragment.onRefresh();
        }
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


}