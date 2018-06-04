package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxIndexClass;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/23.
 */
public class ClassAdapter extends EasyBindQuickAdapter<RxIndexClass> {
    public ClassAdapter(int layoutResId, List<RxIndexClass> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxIndexClass item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }
}
