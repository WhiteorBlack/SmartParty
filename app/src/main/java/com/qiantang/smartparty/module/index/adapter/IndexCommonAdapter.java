package com.qiantang.smartparty.module.index.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxVideoStudy;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class IndexCommonAdapter extends EasyBindQuickAdapter<RxIndexCommon> {
    public IndexCommonAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxIndexCommon item) {
        holder.getBinding().setVariable(BR.item, item);
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(item.getImgSrc());
        holder.getBinding().executePendingBindings();
    }
}
