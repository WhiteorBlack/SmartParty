package com.qiantang.smartparty.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.modle.RxComment;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class CommentAdapter extends EasyBindQuickAdapter<RxComment> {
    public CommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxComment item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
