package com.fintech.match.pay.ui.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fintech.match.pay.R;
import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.ErroCommonConsumer;
import com.fintech.match.pay.common.Constant;
import com.fintech.match.pay.common.model.PageList;
import com.fintech.match.pay.common.model.PageMsg;
import com.fintech.match.pay.common.util.DialogUtils;
import com.fintech.match.pay.common.util.ToastUtils;
import com.fintech.match.pay.ui.BaseFragment;
import com.fintech.match.pay.ui.order.adapter.OrderListRecyclerAdapter;
import com.fintech.match.pay.ui.order.model.OrderList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public class OrderListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    public OrderListFragment() {
        // Required empty public constructor
    }

    public static OrderListFragment newInstance(int mType) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt("tradeStatus", mType);
        fragment.setArguments(args);
        return fragment;
    }

    private Integer tradeStatus = null;

    private OrderListRecyclerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradeStatus = getArguments().getInt("tradeStatus", 0);
        if (tradeStatus.equals(0)) {//null表示全部
            tradeStatus = null;
        }
        mAdapter = new OrderListRecyclerAdapter(OrderListFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classic_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeToLoadLayout.post(() -> swipeToLoadLayout.setRefreshing(true));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    private void stop() {
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    private int pageNow = 1;
    private PageMsg pageMsg;

    @Override
    public void onRefresh() {
        pageNow = 1;
        loadList(pageNow);
        swipeToLoadLayout.setLoadMoreEnabled(true);
    }


    @Override
    public void onLoadMore() {
        if (pageNow <= pageMsg.getPages()) {//have more
            pageNow += 1;
            loadList(pageNow);
        } else {
            stop();
            swipeToLoadLayout.setLoadMoreEnabled(false);
            ToastUtils.show("没有更多");
        }
    }

    @SuppressLint("CheckResult")
    public void loadList(final int pageNow) {
        SignRequestBody body = new SignRequestBody();
        body.put("tradeStatus", tradeStatus);
        body.put("pageSize", Constant.pageSize);
        body.put("pageNow", pageNow);
        Observable<ResultEntity<PageList<OrderList>>> orders = service().orders(body.sign());
        api(orders)
                .doFinally(() -> stop())
                .subscribe(pageResult -> {
                    PageList<OrderList> result = pageResult.getResult();
                    onPageResult(result);
                }, new ErroCommonConsumer(getActivity()));
    }

    private void onPageResult(PageList<OrderList> result) {
        this.pageMsg = result.getPageMsg();
        if (pageMsg.getPageNum() == 1) {
            this.mAdapter.setList(result.getList());
        } else {
            this.mAdapter.append(result.getList());
        }
    }

    //0--------------------------------------------------------------------------------------------

    /**
     * 重发通知
     */
    @SuppressLint("CheckResult")
    public void sendOrderNotify(OrderList orderList) {
        SignRequestBody signRequestBody = new SignRequestBody();
        signRequestBody.put("tradeNo", orderList.getTradeNo());
        Observable<ResultEntity<String>> notify = service().reNotify(signRequestBody.sign());

        api(notify).subscribe(
                (Consumer<ResultEntity>) booleanResultEntity -> ToastUtils.show("通知下发成功"),
                throwable -> DialogUtils.show(getActivity(), throwable.getMessage()));
    }

    /**
     * 我要补单
     */
    public void replenishOrder(final OrderList orderList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确认订单已经支付成功?");
        builder.setPositiveButton(
                "确定",
                (dialog, which) -> postReplenishOrder(orderList)).setNegativeButton("取消",
                (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("CheckResult")
    public void postReplenishOrder(OrderList orderList) {
        SignRequestBody signRequestBody = new SignRequestBody();
        signRequestBody.put("tradeNo", orderList.getTradeNo());
        Observable<ResultEntity<String>> reNotify = service().sendOrderNotify(signRequestBody.sign());
        api(reNotify).subscribe(
                (Consumer<ResultEntity>) booleanResultEntity -> {
                    ToastUtils.show("补单成功");
                    onRefresh();
                },
                throwable -> DialogUtils.show(getActivity(), throwable.getMessage())
        );
    }
}
