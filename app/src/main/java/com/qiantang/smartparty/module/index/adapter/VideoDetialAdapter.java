package com.qiantang.smartparty.module.index.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.modle.RxVideoDetial;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class VideoDetialAdapter extends EasyBindQuickAdapter<RxComment> {


    public VideoDetialAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxComment item) {

    }


}
