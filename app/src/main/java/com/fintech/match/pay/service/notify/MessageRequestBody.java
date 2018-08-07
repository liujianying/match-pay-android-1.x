package com.fintech.match.pay.service.notify;

import com.fintech.match.pay.SignRequestBody;
import com.fintech.match.pay.api.model.RequestBody;
import com.fintech.match.pay.data.source.TradeLog;
import com.fintech.match.pay.service.message.IMessage;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class MessageRequestBody extends SignRequestBody {

    public MessageRequestBody() {
        super();
    }

    protected void copyFromMessage(TradeLog log) {
        put("uuid", log.getUuid());
        put("amount", log.getAmount());
        put("title", log.getTitle());
        put("content", log.getContent());
//        put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(iMessage.getTime()));
        put("time", log.getTime());
        put("type", log.getType());
        put("packageName", log.getPackageName());
        put("id", log.getId());
        put("tag", log.getTag());
    }

    private int count;

    public int getNotifyCount() {
        return count;
    }

    public void addNotifyCount() {
        this.count += 1;
    }

    public String key() {
        return String.valueOf(this.get("type")) + String.valueOf(this.get("time")) + String.valueOf(this.get("amount"));
    }

}
