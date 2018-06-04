package com.qiantang.smartparty.module.assistant.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxMsg;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class MsgAdapter extends EasyBindQuickAdapter<RxMsg> {
    public MsgAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxMsg item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }
}
