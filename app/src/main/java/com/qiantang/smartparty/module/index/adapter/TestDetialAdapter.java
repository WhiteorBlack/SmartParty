package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxTestDetial;
import com.qiantang.smartparty.BR;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDetialAdapter extends EasyBindQuickAdapter<RxTestDetial> {
    public TestDetialAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxTestDetial item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
