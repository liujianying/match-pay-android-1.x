package com.fintech.match.pay.common.widget.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fintech.match.pay.R;

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;

    public EmptyViewHolder(View view) {
        super(view);
        imageView = view.findViewById(R.id.iv_empty_image);
        textView = view.findViewById(R.id.tv_empty_description);
    }
}