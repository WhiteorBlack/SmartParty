package com.qiantang.smartparty.modle;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by zhaoyong bai on 2018/6/12.
 */
public class RxIndexSection<T> extends SectionEntity {
    private boolean isTop = false;
    private boolean isBottom=false;
    private RxIndexStudy rxIndexStudy;
    private int classifyId;

    public RxIndexSection(boolean isHeader, String header, boolean isTop,boolean isBottom,int classifyId) {
        super(isHeader, header);
        this.isTop = isTop;
        this.isBottom=isBottom;
        this.classifyId=classifyId;
    }

    public RxIndexSection(T t) {
        super(t);
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }

    public void setRxIndexStudy(RxIndexStudy rxIndexStudy) {
        this.rxIndexStudy = rxIndexStudy;
    }

    public RxIndexStudy getRxIndexStudy() {
        return rxIndexStudy;
    }

    public String getHeader() {
        return header;
    }
}
