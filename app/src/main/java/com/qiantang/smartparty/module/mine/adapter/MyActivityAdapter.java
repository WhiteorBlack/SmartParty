package com.qiantang.smartparty.module.mine.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxActivity;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class MyActivityAdapter extends EasyBindQuickAdapter<RxActivity> {
    public MyActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxActivity item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.tv_check)
                .addOnClickListener(R.id.tv_del);
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST + item.getImgSrc());
    }
}
