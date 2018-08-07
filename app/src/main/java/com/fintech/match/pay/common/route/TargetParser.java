package com.fintech.match.pay.common.route;


import org.json.JSONException;
import org.json.JSONObject;

public class TargetParser {

    public static JSONObject parse(String param) throws JSONException {
        JSONObject object = new JSONObject(param);
        return object;
    }

}
