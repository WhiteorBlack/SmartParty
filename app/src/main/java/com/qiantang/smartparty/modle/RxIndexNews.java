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
    private int read;
    private String imgSrc;
    private String createTime;
    private String title;
    private String printurl;
    private String contentId;
    private int pl;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrinturl() {
        return printurl;
    }

    public void setPrinturl(String printurl) {
        this.printurl = printurl;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    public RxIndexNews(int itemType) {
        this.itemType = itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
