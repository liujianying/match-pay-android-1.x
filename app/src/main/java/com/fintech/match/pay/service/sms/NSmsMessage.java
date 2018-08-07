package com.fintech.match.pay.service.sms;

import com.fintech.match.pay.service.TradeChannel;
import com.fintech.match.pay.service.message.IMessage;

public class NSmsMessage implements IMessage {

    private String tell;

    private String content;
    private long time;

    public NSmsMessage(String tell, String content, long time) {
        this.tell = tell;
        this.content = content;
        this.time = time;
    }

    @Override
    public void setAmount(float amount) {

    }

    @Override
    public float getAmount() {
        return -1;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getPackageName() {
        return "sms";
    }

    @Override
    public String getTag() {
        return "手机短信";
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String getTitle() {
        return tell;
    }

    @Override
    public int getType() {
        return TradeChannel.bank;
    }


}
