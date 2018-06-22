package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/22.
 */
public class RxSpecialDetial {
    private List<RxComment> comment;
    private RxSpecialInfo detail;

    public List<RxComment> getComment() {
        return comment;
    }

    public void setComment(List<RxComment> comment) {
        this.comment = comment;
    }

    public RxSpecialInfo getDetail() {
        return detail;
    }

    public void setDetail(RxSpecialInfo detail) {
        this.detail = detail;
    }
}
