package com.qiantang.smartparty.module.assistant.adapter;

import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxFeeRecord;
import com.qiantang.smartparty.BR;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeRecordAdapter extends EasyBindQuickAdapter<RxFeeRecord> {
    public FeeRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxFeeRecord item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }
}
