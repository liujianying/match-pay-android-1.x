package com.fintech.match.pay.ui.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fintech.match.pay.R;
import com.fintech.match.pay.common.util.DensityUtils;
import com.fintech.match.pay.common.util.NumberFormatUtils;
import com.fintech.match.pay.common.widget.helper.SpannableUtils;
import com.fintech.match.pay.common.widget.holder.EmptyViewHolder;
import com.fintech.match.pay.service.TradeChannel;
import com.fintech.match.pay.ui.order.OrderListFragment;
import com.fintech.match.pay.ui.order.model.OrderList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0x0;
    private static final int VIEW_TYPE_ITEM = 0x1;

    private final List<OrderList> mOrderLists;

    protected Context getContext() {
        return fragment.getContext().getApplicationContext();
    }

    private OrderListFragment fragment;

    public OrderListRecyclerAdapter(OrderListFragment fragment) {
        this.fragment = fragment;
        mOrderLists = new ArrayList<>();
    }

    public void setList(List<OrderList> orderLists) {
        this.mOrderLists.clear();
        if (orderLists == null) return;
        append(orderLists);
    }

    public void append(List<OrderList> orderLists) {
        this.mOrderLists.addAll(orderLists);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty_item_view, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rv_order, parent, false);
            return new OrderListHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OrderListHolder) {

            OrderList orderList = mOrderLists.get(position);

            OrderListHolder oHolder = (OrderListHolder) holder;

            oHolder.tvRvOrderCode.setText("订单编号:".concat(orderList.getTradeNo()));

            String totalAmountDesc;
            if (orderList.getTotalAmount() == 0d) {
                totalAmountDesc = "动态金额";
            } else {
                totalAmountDesc = NumberFormatUtils.format2dot(orderList.getTotalAmount()).concat(" 元");
            }
            oHolder.tvRvOrderTotalAmount.setText(buildText("定价: ", totalAmountDesc, "#333333"));
            oHolder.tvRvOrderFee.setText(buildText("手续费: ",
                    NumberFormatUtils.format5dot(orderList.getSeviceFee()).concat(" 元"), "#333333"));

            oHolder.tvRvOrderRealAmount.setText(buildText("实收: ",
                    NumberFormatUtils.format2dot(orderList.getRealAmount()).concat("元"), "#FF9600"));
            oHolder.tvRvOrderPayWay.setText(buildText("支付方式: ", TradeChannel.getDesc(orderList.getTradeChannel()), "#7B9FFE"));

            String realAmountDesc = NumberFormatUtils.format2dot(orderList.getRealAmount()).concat("元");

            oHolder.tvRvOrderRealAmount.setText(buildText("实收: ",
                    realAmountDesc, "#FF9600"));
            oHolder.tvRvOrderPayWay.setText(buildText("通道: ", TradeChannel.getDesc(orderList.getTradeChannel()), "#7B9FFE"));

            oHolder.tvRvOrderDisAmount.setText(buildText("折扣: ",
                    NumberFormatUtils.format2dot(orderList.getDiscAmount()).concat("元"), "#FF9600"));
            oHolder.tvRvOrderAccount.setText(buildText("账号: ", orderList.getQrAccount(), "#7B9FFE"));

            oHolder.tvRvOrderCreateTime.setText("下单时间: " + orderList.getCreateTime());

            // --------------------------------------------------------
            disposeTradeStatus(orderList, oHolder);

        }
    }

    //订单状态 支付中 10 通知中 20 成功 30 关闭 40
    private void disposeTradeStatus(final OrderList orderList, OrderListHolder oHolder) {
        if ("10".equals(orderList.getTradeStatus())) {//支付中
            oHolder.btnDo.setVisibility(View.GONE);

            oHolder.tvRvTradeStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.grandis));
            oHolder.tvRvTradeStatus.setText("支付中");
        } else if ("20".equals(orderList.getTradeStatus())) {//通知中
            oHolder.btnDo.setVisibility(View.VISIBLE);
            oHolder.btnDo.setText("重发通知");
            oHolder.btnDo.setOnClickListener(v -> {
                fragment.sendOrderNotify(orderList);
            });

            oHolder.tvRvTradeStatus.setTextColor(Color.BLUE);
            oHolder.tvRvTradeStatus.setText("通知中");
        } else if ("30".equals(orderList.getTradeStatus())) {//成功
            oHolder.btnDo.setVisibility(View.GONE);

            oHolder.tvRvTradeStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.forest_green));
            oHolder.tvRvTradeStatus.setText("成功");
        } else if ("40".equals(orderList.getTradeStatus())) {//关闭
            oHolder.btnDo.setVisibility(View.VISIBLE);
            oHolder.btnDo.setText("我要补单");
            oHolder.btnDo.setOnClickListener(v -> {//TODO 补单
                fragment.replenishOrder(orderList);
            });

            oHolder.tvRvTradeStatus.setTextColor(Color.RED);
            oHolder.tvRvTradeStatus.setText("关闭");
        } else {
            oHolder.btnDo.setVisibility(View.VISIBLE);
            oHolder.btnDo.setText("未知状态");
        }
    }

    public Spannable buildText(String pre, String end, String endColor) {
        return SpannableUtils.buildMultiColorText(
                pre,
                DensityUtils.dp2px(getContext(), 16),
                Color.parseColor("#333333"),
                end,
                DensityUtils.dp2px(getContext(), 14),
                Color.parseColor(endColor)
        );
    }

    @Override
    public int getItemCount() {
        if (mOrderLists.size() == 0) {
            return 1;
        }
        return mOrderLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mOrderLists.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return VIEW_TYPE_ITEM;
    }

    static class OrderListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_rv_order_code)
        TextView tvRvOrderCode;
        @BindView(R.id.tv_rv_trade_status)
        TextView tvRvTradeStatus;
        @BindView(R.id.tv_rv_order_total_amount)
        TextView tvRvOrderTotalAmount;
        @BindView(R.id.tv_rv_order_real_amount)
        TextView tvRvOrderRealAmount;
        @BindView(R.id.tv_rv_order_fee)
        TextView tvRvOrderFee;
        @BindView(R.id.tv_rv_order_pay_way)
        TextView tvRvOrderPayWay;
        @BindView(R.id.tv_rv_order_dis_amount)
        TextView tvRvOrderDisAmount;
        @BindView(R.id.tv_rv_order_account)
        TextView tvRvOrderAccount;

        @BindView(R.id.tv_rv_order_create_time)
        TextView tvRvOrderCreateTime;

        @BindView(R.id.btn_do)
        Button btnDo;

        OrderListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int i = R.layout.item_rv_order;
        }
    }


}
