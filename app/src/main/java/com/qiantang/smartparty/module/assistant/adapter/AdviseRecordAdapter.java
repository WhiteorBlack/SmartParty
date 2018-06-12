package com.qiantang.smartparty.module.assistant.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
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
        ((SimpleDraweeView)holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getAvatar());
    }
}
