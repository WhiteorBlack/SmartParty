package com.qiantang.smartparty.module.assistant.adapter;

import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxAdviseRecord;
import com.qiantang.smartparty.BR;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class AdviseRecordAdapter extends EasyBindQuickAdapter<RxAdviseRecord> {
    public AdviseRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxAdviseRecord item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }
}
