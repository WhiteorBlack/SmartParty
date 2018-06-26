package com.qiantang.smartparty.module.study.adapter;

import com.android.databinding.library.baseAdapters.BR;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class StudyImageAdapter extends EasyBindQuickAdapter<String> {
    public StudyImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    public StudyImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, String item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.sdv);
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item);
    }
}
