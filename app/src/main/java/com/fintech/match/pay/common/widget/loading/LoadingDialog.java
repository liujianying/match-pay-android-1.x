package com.fintech.match.pay.common.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.fintech.match.pay.R;

public class LoadingDialog extends Dialog {

    private TextView tv_text;

    public LoadingDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.layout_loading);
        tv_text = findViewById(R.id.tv_text);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public LoadingDialog setMessage(String message) {
        tv_text.setText(message);
        return this;
    }
}