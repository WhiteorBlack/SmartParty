package com.qiantang.smartparty.module.mine.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxMonthScore;
import com.qiantang.smartparty.modle.RxTotalScore;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class TotalScoreAdapter extends EasyBindQuickAdapter<RxTotalScore> {
    public TotalScoreAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxTotalScore item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
