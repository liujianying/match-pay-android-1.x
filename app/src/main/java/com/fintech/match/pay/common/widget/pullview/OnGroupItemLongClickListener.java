package com.fintech.match.pay.common.widget.pullview;

import android.view.View;


public interface OnGroupItemLongClickListener<G> {
    boolean onGroupLongClick(int groupPosition, G g, View view);
}
