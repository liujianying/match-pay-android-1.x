package com.fintech.match.pay.service.message;

import com.fintech.match.pay.service.TradeChannel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NWeChatMessage extends NAbstractMessage {

    public NWeChatMessage() {
        super(TradeChannel.wechat);
    }

    private void parseAmount() {
        String str = getContent();

        String pattern = "收款([0-9]+.[0-9][0-9])元$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
            String group = m.group(1);
            float v = Float.parseFloat(group);
            setAmount(v);
        }
    }

    protected final void rebuild() {
        setContent(super.getContent());
        try {
            parseAmount();
        } catch (Exception e) {
            e.printStackTrace();
            setAmount(0.00f);
        }
    }
}

