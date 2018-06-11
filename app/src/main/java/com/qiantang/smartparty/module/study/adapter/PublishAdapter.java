package com.qiantang.smartparty.module.study.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.utils.DraweeViewUtils;
import com.qiantang.smartparty.BR;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PublishAdapter extends EasyBindQuickAdapter<PhotoInfo> {

    public PublishAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, PhotoInfo item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
        ((SimpleDraweeView)holder.getView(R.id.sdv)).setImageURI(DraweeViewUtils.getUriPath(Config.IMAGE_HOST+item.getPhotoPath()));
        holder.addOnClickListener(R.id.publish_item_lose)
                .addOnClickListener(R.id.sdv_item_pic);
    }
}
