package com.qiantang.smartparty.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
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
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.iv_praise)
                .addOnClickListener(R.id.iv_unpraise);
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getAvatar());
    }
}
