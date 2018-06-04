package com.qiantang.smartparty.module.assistant.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxActivity;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class ActivityAdapter extends EasyBindQuickAdapter<RxActivity> {
    public ActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxActivity item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
