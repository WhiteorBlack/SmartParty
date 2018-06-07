package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.modle.RxRankPersonalList;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankPersonalAdapter extends EasyBindQuickAdapter<RxRankPersonalList> {
    public RankPersonalAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxRankPersonalList item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
