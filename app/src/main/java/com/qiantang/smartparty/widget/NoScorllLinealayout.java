package com.qiantang.smartparty.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class NoScorllLinealayout extends LinearLayoutManager {
    public NoScorllLinealayout(Context context) {
        super(context);
    }

    public NoScorllLinealayout(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScorllLinealayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
