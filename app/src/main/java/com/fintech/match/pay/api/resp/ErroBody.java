package com.fintech.match.pay.api.resp;

public class ErroBody {

    private String code;

    private String msg;

    public ErroBody(String msg) {
        this.msg = msg;
    }

    public ErroBody(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    @Override
    public String toString() {
        return "ErroBody{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}