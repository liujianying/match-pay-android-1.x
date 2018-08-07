package com.fintech.match.pay.api;


import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.model.RequestBody;
import com.fintech.match.pay.api.param.LoginRequest;
import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.common.model.PageList;
import com.fintech.match.pay.data.model.UserInfoDetail;
import com.fintech.match.pay.service.message.IMessage;
import com.fintech.match.pay.service.message.NAbstractMessage;
import com.fintech.match.pay.service.message.NAliPayMessage;
import com.fintech.match.pay.service.notify.MessageRequestBody;
import com.fintech.match.pay.ui.me.model.GatheringAcount;
import com.fintech.match.pay.ui.order.model.OrderList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @POST("/api/terminal/v1/merchant/login")
    Observable<ResultEntity<Map<String, String>>> login(
            @Body SignRequestBody body
    );

    @POST("/api/terminal/v1/merchant/logout")
    Observable<ResultEntity<Boolean>> logout(
            @Body SignRequestBody body
    );

    @POST("/api/terminal/v1/merchant/info")
    Observable<ResultEntity<UserInfoDetail>> userInfo(
            @Body SignRequestBody body
    );

    @POST("/api/terminal/v1/merchant/orders")
    Observable<ResultEntity<PageList<OrderList>>> orders(
            @Body SignRequestBody body
    );

    @POST("/api/terminal/v1/trade/notify")
    Observable<ResultEntity> notifyLog(
            @Body SignRequestBody body
    );

    //补单
    @POST("/api/terminal/v1/trade/replenish")
    Observable<ResultEntity<String>> sendOrderNotify(
            @Body SignRequestBody body
    );

    @POST("/api/terminal/v1/trade/reNotify")
    Observable<ResultEntity<String>> reNotify(
            @Body SignRequestBody body
    );

    // alipays or wechats
    @POST("/api/terminal/v1/gathering/setting/init")
    Observable<ResultEntity<List<GatheringAcount>>> gatheringSettingInit(
            @Body SignRequestBody body
    );

    // alipays or wechats
    @POST("/api/terminal/v1/heartbeat")
    Flowable<ResultEntity<Boolean>> heartbeat(
            @Body SignRequestBody body
    );

}