package com.fintech.match.pay.common.widget.loading;

import android.app.Activity;


public class LoadingUtils {

    public static void dismiss() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }

    public static LoadingDialog loadingDialog;

    public static void showProgress(Activity activity, String message) {
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.setMessage(message).show();
    }


    public static void showProgress(Activity activity) {
        showProgress(activity, "正在加载...");
    }

}
