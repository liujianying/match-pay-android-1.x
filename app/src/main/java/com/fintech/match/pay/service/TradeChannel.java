package com.fintech.match.pay.service;

public class TradeChannel {

    public static final int wechat = 1001;

    public static final int alipay = 2001;

    public static final int bank = 3001;

    public static String getDesc(int tradeChannel) {
        switch (tradeChannel) {
            case wechat:
                return "微信";
            case alipay:
                return "支付宝";
            case bank:
                return "银行卡";
            default:
                return "";
        }
    }

}
