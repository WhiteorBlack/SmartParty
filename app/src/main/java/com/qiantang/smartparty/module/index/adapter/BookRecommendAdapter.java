package com.qiantang.smartparty.module.index.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxBookRecommend;
import com.qiantang.smartparty.modle.RxIndexCommon;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class BookRecommendAdapter extends EasyBindQuickAdapter<RxBookRecommend> {
    public BookRecommendAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxBookRecommend item) {
        holder.getBinding().setVariable(BR.item, item);
        try {
            ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getPrinturl());
        } catch (NullPointerException e) {

        }

        holder.getBinding().executePendingBindings();
    }
}
