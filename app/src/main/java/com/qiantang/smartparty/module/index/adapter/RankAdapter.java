package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxRank;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankAdapter extends EasyBindQuickAdapter<RxRank> {
    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxRank item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
