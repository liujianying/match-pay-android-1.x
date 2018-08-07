package com.fintech.match.pay.service.message;

import com.fintech.match.pay.data.source.TradeLog;

import java.util.UUID;

public interface IMessage {

    void setAmount(float amount);

    float getAmount();

    String getContent();

    int getId();

    String getPackageName();

    String getTag();

    long getTime();

    String getTitle();

    int getType();

    default TradeLog createLog() {
        TradeLog log = new TradeLog();
        log.setUuid(UUID.randomUUID().toString().replace("-", ""));
        log.setAmount(getAmount());
        log.setTitle(getTitle());
        log.setContent(getContent());
        log.setTime(getTime());
        log.setType(getType());
        log.setPackageName(getPackageName());
        log.setId(getId());
        log.setTag(getTag());
        return log;
    }
}
