package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxBookDetial {
    private int isComment;
    private int count;
    private List<RxComment> comment;
    private RxBookInfo detail;

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

    public RxBookInfo getDetail() {
        return detail;
    }

    public void setDetail(RxBookInfo detail) {
        this.detail = detail;
    }
}
