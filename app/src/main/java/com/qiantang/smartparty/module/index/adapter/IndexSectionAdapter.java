package com.qiantang.smartparty.module.index.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxIndexSection;
import com.qiantang.smartparty.utils.AutoUtils;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/12.
 */
public class IndexSectionAdapter extends BaseSectionQuickAdapter<RxIndexSection, IndexSectionAdapter.MovieViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public IndexSectionAdapter(int layoutResId, int sectionHeadResId, List<RxIndexSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        AutoUtils.auto(view);
        return view;
    }

    @Override
    protected void convertHead(MovieViewHolder helper, RxIndexSection item) {
        helper.getBinding().setVariable(BR.item, item);
        helper.getBinding().executePendingBindings();
        helper.addOnClickListener(R.id.tv_speech);
        helper.getBinding().getRoot().findViewById(R.id.tv_speech).setTag(item.getClassifyId());
    }


    @Override
    protected void convert(MovieViewHolder helper, RxIndexSection item) {
        helper.getBinding().setVariable(BR.item, item.getRxIndexStudy());
        helper.getBinding().executePendingBindings();
        ((SimpleDraweeView)helper.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getRxIndexStudy().getPrinturl());
    }

    public static class MovieViewHolder extends BaseViewHolder {

        public MovieViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
