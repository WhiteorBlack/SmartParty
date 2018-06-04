package com.qiantang.smartparty.module.assistant.adapter;

import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxPartyFee;
import com.qiantang.smartparty.BR;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class PartyFeeAdapter extends EasyBindQuickAdapter<RxPartyFee> {
    public PartyFeeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPartyFee item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
