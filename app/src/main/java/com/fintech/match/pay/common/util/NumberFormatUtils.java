package com.fintech.match.pay.common.util;

import java.text.DecimalFormat;

public class NumberFormatUtils {


    public static String format5dot(double target){
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        return decimalFormat.format(target);
    }

    public static String format4dot(double target){
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        return decimalFormat.format(target);
    }

    public static String format2dot(double target){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(target);
    }


}
