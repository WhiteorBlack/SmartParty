package com.qiantang.smartparty.module.index.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexNews;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/23.
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<RxIndexNews, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsAdapter(List<RxIndexNews> data) {
        super(data);
        addItemType(RxIndexNews.ITEM_TOP, R.layout.item_news_top);
        addItemType(RxIndexNews.ITEM_BOTTOM, R.layout.item_news_common);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int resId = 0;
        boolean isAuto = true;
        switch (viewType) {
            case RxIndexNews.ITEM_BOTTOM:
                resId = R.layout.item_news_common;
                break;
            case RxIndexNews.ITEM_TOP:
                resId = R.layout.item_news_top;
                break;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, resId, parent, false);
        return new BindingViewHolder<>(binding, isAuto);
    }

    @Override
    protected void convert(BaseViewHolder helper, RxIndexNews item) {
        ViewDataBinding binding = ((BindingViewHolder) helper).getBinding();
        binding.setVariable(BR.item, item);
        ((SimpleDraweeView) binding.getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST+item.getImgSrc());
        binding.executePendingBindings();
    }
}
