package com.qiantang.smartparty.modle;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhaoyong bai on 2018/5/23.
 */
public class RxIndexNews implements MultiItemEntity {
    public static final int ITEM_TOP = 1;
    public static final int ITEM_BOTTOM = 2;
    private int itemType;

    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public RxIndexNews(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
