package com.fintech.match.pay.api.model;

import java.io.Serializable;


public class RequestBody implements Serializable {

    protected static final long serialVersionUID = 1l;

    private String channel = "Android";

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }






}
