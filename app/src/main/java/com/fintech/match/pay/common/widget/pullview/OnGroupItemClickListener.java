package com.fintech.match.pay.common.widget.pullview;

import android.view.View;


public interface OnGroupItemClickListener<G> {
    void onGroupItemClick(int groupPosition, G g, View view);
}
