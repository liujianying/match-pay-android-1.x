package com.fintech.match.pay.common.widget.pullview;

import android.view.View;


public interface OnChildItemLongClickListener<C> {
    boolean onClickItemLongClick(int groupPosition, int childPosition, C c, View view);
}
