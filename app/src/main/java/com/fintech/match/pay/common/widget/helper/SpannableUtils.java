package com.fintech.match.pay.common.widget.helper;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class SpannableUtils {

    //px
    public static Spannable buildMultiColorText(
            String pre,
            int preTextSize,
            int preColor,
            String end,
            int endTextSize,
            int endColor
    ) {
        String content = pre.concat(end);
        Spannable textSpan = new SpannableStringBuilder(content);
        textSpan.setSpan(new AbsoluteSizeSpan(preTextSize), 0, pre.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textSpan.setSpan(new ForegroundColorSpan(preColor), 0, pre.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(endTextSize), pre.length(), content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textSpan.setSpan(new ForegroundColorSpan(endColor), pre.length(), content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return textSpan;
    }


}
