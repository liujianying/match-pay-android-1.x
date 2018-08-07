package com.fintech.match.pay.data.source;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fintech.match.pay.common.util.Utils;
import com.fintech.match.pay.service.message.IMessage;
import com.fintech.match.pay.service.notify.MessageRequestBody;

@Entity(tableName = "trade_log")
public class TradeLog implements IMessage {

    @NonNull
    @ColumnInfo(name = "uuid")
    @PrimaryKey
    private String uuid;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "packageName")
    private String packageName;

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "post")
    private boolean post;

    @ColumnInfo(name = "erroMsg")
    private String erroMsg;

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public String getErroMsg() {
        if (TextUtils.isEmpty(erroMsg)) {
            return "";
        }
        return erroMsg;
    }

    public void setErroMsg(String erroMsg) {
        this.erroMsg = erroMsg;
    }

    public MessageRequestBody createSignRequestBody() {
        MessageRequestBody body = new MessageRequestBody();
        body.put("uuid", this.getUuid());
        body.put("amount", this.getAmount());
        body.put("title", this.getTitle());
        body.put("content", this.getContent());
        body.put("time", this.getTime());
        body.put("type", this.getType());
        body.put("packageName", this.getPackageName());
        body.put("id", this.getId());
        body.put("tag", this.getTag());
        body.sign();
        return body;
    }

    @Override
    public String toString() {
        return "TradeLog{" +
                "uuid='" + uuid + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", packageName='" + packageName + '\'' +
                ", id=" + id +
                ", tag='" + tag + '\'' +
                ", post=" + post +
                ", erroMsg=" + erroMsg +
                '}';
    }

    public String getDesc() {
        return getTitle() + " | " + getAmount() + " | " + getContent() + " | " + isPost() + " --> " + getErroMsg() + "\n" + Utils.formatDate(time);
    }
}
