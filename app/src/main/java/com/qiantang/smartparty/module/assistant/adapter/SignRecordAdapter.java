package com.qiantang.smartparty.module.assistant.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxSignInfo;
import com.qiantang.smartparty.BR;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class SignRecordAdapter extends EasyBindQuickAdapter<RxSignInfo> {
    public SignRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxSignInfo item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getAvatar());
    }
}
