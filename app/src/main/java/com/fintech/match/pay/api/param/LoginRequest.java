package com.fintech.match.pay.api.param;

import com.fintech.match.pay.api.model.RequestBody;


public class LoginRequest extends RequestBody {

    private String password;

    private String userName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
