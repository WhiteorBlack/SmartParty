package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxRankBranchList;
import com.qiantang.smartparty.modle.RxRankPersonal;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankAdapter extends EasyBindQuickAdapter<RxRankBranchList> {
    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxRankBranchList item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
