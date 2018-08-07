package com.fintech.match.pay.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


public class DialogUtils {

    private static volatile Dialog dialog;

    public static void show(Activity context, String message) {
        show(context, message, (dialog, which) -> dialog.dismiss());
    }

    public static void show(Activity context, String message, DialogInterface.OnClickListener listener) {
        dismiss();

        if (context == null || context.isDestroyed() || context.isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        dialog = builder.create();
        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
