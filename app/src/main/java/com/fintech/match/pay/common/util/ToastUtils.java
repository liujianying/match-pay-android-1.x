package com.fintech.match.pay.common.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.fintech.match.pay.App;


public class ToastUtils {

    public static Toast mToast;

    public static void show(Context context, CharSequence message, int duration) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void show(CharSequence message) {
        show(App.getApplication().getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

}
