package com.fintech.match.pay.api;


import okhttp3.MediaType;

public class Constants {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

//    public static final String baseUrl = "https://api.pay.hccf8.com";
    public static String baseUrl = "https://api1.52cjzf.com/";
//    public static final String baseUrl = "http://47.52.162.210:50182";

    public static final String KEY_SP_ = "userInfo";
    public static final String KEY_MCH_ID = "mchId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LOGIN_TOKEN = "loginToken";


}
