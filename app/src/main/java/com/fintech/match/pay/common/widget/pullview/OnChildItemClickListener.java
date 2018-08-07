package com.fintech.match.pay.common.widget.pullview;

import android.view.View;

public interface OnChildItemClickListener<C> {
    void onChildItemClick(int groupPosition, int childPosition, C c, View view);
}
