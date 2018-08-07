package com.fintech.match.pay.common;

import android.Manifest;

public class Constant {

    //分页,每页size
    public static final int pageSize = 12;

    public static final int RC_PERMISSION = 110;

    //-------------------permissions--------------------------------
    public static final int ALL_PERMISSION = 122;

    public static String[] PERMISSIONS_GROUP = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE
    };
    //-------------------permissions--------------------------------


}
