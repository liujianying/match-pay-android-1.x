package com.fintech.match.pay.api.rx;

import com.fintech.match.pay.api.resp.ResultEntity;
import com.fintech.match.pay.api.rx.excep.ConsumerException;
import com.fintech.match.pay.api.rx.excep.UnLoginException;
import com.fintech.match.pay.common.widget.loading.LoadingUtils;

import io.reactivex.functions.Consumer;


public class ResultCodeConsumer<T> implements Consumer<T> {

    public ResultCodeConsumer() {
    }

    @Override
    public void accept(T t) throws Exception {
        LoadingUtils.dismiss();//如果有,dissmiss

        if (t instanceof ResultEntity) {
            ResultEntity entity = (ResultEntity) t;
            if ("10000".equals(entity.getCode())) {
                return;
            } else if ("20001".equals(entity.getCode())) {
                throw new UnLoginException(entity.getMsg());
            } else if ("20002".equals(entity.getCode())) {
                throw new UnLoginException(entity.getMsg());
            } else {
                throw new ConsumerException(entity.getSubMsg());
            }
        }
    }

}
