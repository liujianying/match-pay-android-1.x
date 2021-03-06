package com.fintech.match.pay.data.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"account", "pos_curr", "offset", "type"})
public class User {

    @NonNull
    public String account = "";//账号
    public double amount;
    public String qr_str;//二维码长串

    public int pos_start;//初始pos
    public int pos_curr;//当前pos

    @ColumnInfo
    public int pos_end;//总共pos

    @ColumnInfo
    public int offset;//偏移
    public int offset_total;//总并发数

    @ColumnInfo
    public int multiple;//倍数


    public int type;//1:支付宝  2:微信
    public String deviceId;//设备id

    public long saveTime;

    public User() {
        saveTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", pos_start=" + pos_start +
                ", pos_curr=" + pos_curr +
                ", pos_end=" + pos_end +
                ", qr_str='" + qr_str + '\'' +
                ", offset=" + offset +
                ", offset_total=" + offset_total +
                ", multiple=" + multiple +
                ", type=" + type +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
