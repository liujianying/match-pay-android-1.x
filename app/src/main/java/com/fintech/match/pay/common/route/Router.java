package com.fintech.match.pay.common.route;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.fintech.match.pay.common.util.ToastUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Router {

    public static final Map<String, Class<? extends Activity>> routerMap = new HashMap<>();

    static {

    }

    public static final String DATA_STRING = "DATA_STRING";
    public static final String DATA_PARCEABLE = "DATA_PARCEABLE";

    private static volatile Router instance;

    private Router() {
    }


    public static Router get() {
        if (instance == null) {
            synchronized (Router.class) {
                if (instance == null) {
                    instance = new Router();
                }
            }
        }
        instance.reset();
        return instance;
    }

    public Intent getIntent(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(DATA_STRING, param);
        if (parcelable != null) {
            intent.putExtra(DATA_PARCEABLE, parcelable);
        }
        return intent;
    }


    private void reset() {

    }

    public Router build(Target target) {
        if ("route".equals(target.getTargetType())) {
            Class<? extends Activity> aClass = routerMap.get(target.getTargetUrl());
            build(aClass).extras(target.getTargetParam());
        } else if ("url".equals(target.getTargetType())) {
            // TODO goto webview
        }
        return this;
    }

    public void go(Context context) {
        if (clazz != null) {
            loadUI(context);
        }
    }

    public void goResult(Activity activity, int requestCode) {
        if (clazz != null) {
            Intent intent = new Intent(activity.getApplicationContext(), clazz);
            intent.putExtra(DATA_STRING, param);
            if (parcelable != null) {
                intent.putExtra(DATA_PARCEABLE, parcelable);
            }
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void goResult(Fragment fragment, int requestCode) {
        if (clazz != null) {
            Intent intent = new Intent(fragment.getContext(), clazz);
            intent.putExtra(DATA_STRING, param);
            if (parcelable != null) {
                intent.putExtra(DATA_PARCEABLE, parcelable);
            }
            fragment.startActivityForResult(intent, requestCode);
            fragment.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void loadUI(Context context) {
        try {
            if (context instanceof Activity) {
                Intent intent = new Intent(context, clazz);
                intent.putExtra(DATA_STRING, param);
                if (parcelable != null) {
                    intent.putExtra(DATA_PARCEABLE, parcelable);
                }
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Intent intent = new Intent(context, clazz);
                intent.putExtra(DATA_STRING, param);
                if (parcelable != null) {
                    intent.putExtra(DATA_PARCEABLE, parcelable);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtils.show(context, ex.getMessage());
        }
    }

    private String erroMessage = "配置信息有误";
    //===================================================

    private Class<?> clazz;

    private String param;
    private Parcelable parcelable;

    public Router build(Class<? extends Activity> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Router extras(String param) {
        this.param = param;
        return this;
    }

    public Router extras(Parcelable parcelable) {
        this.parcelable = parcelable;
        return this;
    }

    public static <T extends Parcelable> T getParcelableExtra(Intent intent) {
        try {
            T extra = intent.getParcelableExtra(DATA_PARCEABLE);
            return extra;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getStringExtra(Intent intent) {
        try {
            String extra = intent.getStringExtra(DATA_STRING);
            return extra;
        } catch (Exception e) {
            return "";
        }
    }

    public static int getIntExtra(Intent intent) {
        try {
            String extra = intent.getStringExtra(DATA_STRING);
            return Integer.valueOf(extra);
        } catch (Exception e) {
            return 0;
        }
    }

    public static <T> T parseExtra(Intent intent, Class<T> clazz) {
        try {
            String extra = intent.getStringExtra(DATA_STRING);
            return new Gson().fromJson(extra, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
