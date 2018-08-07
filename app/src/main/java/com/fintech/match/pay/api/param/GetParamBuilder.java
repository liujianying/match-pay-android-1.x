package com.fintech.match.pay.api.param;


import java.util.HashMap;
import java.util.Map;

public class GetParamBuilder {

    private Map<String, String> map;

    private GetParamBuilder() {
        map = new HashMap();
//        map.put("terminal", "Android");
        map.put("channel", "Android");
    }

    public static GetParamBuilder instance() {
        return new GetParamBuilder();
    }

    public GetParamBuilder put(String key, String value) {
        map.put(key, value);
        return this;
    }

    public Map build() {
        return map;
    }

}
