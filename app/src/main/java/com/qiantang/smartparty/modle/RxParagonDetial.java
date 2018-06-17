package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxParagonDetial {
    private int isComment;
    private int count;
    private List<RxComment> comment;
    private RxParagonInfo detail;
    private List<RxPicUrl> imgSrc;

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RxComment> getComment() {
        return comment;
    }

    public void setComment(List<RxComment> comment) {
        this.comment = comment;
    }

    public RxParagonInfo getDetail() {
        return detail;
    }

    public void setDetail(RxParagonInfo detail) {
        this.detail = detail;
    }

    public List<RxPicUrl> getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(List<RxPicUrl> imgSrc) {
        this.imgSrc = imgSrc;
    }
}
