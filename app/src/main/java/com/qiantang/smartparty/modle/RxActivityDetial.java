package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxActivityDetial {
    private int isComment;
    private int count;
    private List<RxComment> pl;
    private int isSign;
    private List<RxSignInfo> qd;
    private int isApply;
    private RxPartyActivityDetial details;

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

    public List<RxComment> getPl() {
        return pl;
    }

    public void setPl(List<RxComment> pl) {
        this.pl = pl;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public List<RxSignInfo> getQd() {
        return qd;
    }

    public void setQd(List<RxSignInfo> qd) {
        this.qd = qd;
    }

    public int getIsApply() {
        return isApply;
    }

    public void setIsApply(int isApply) {
        this.isApply = isApply;
    }

    public RxPartyActivityDetial getDetials() {
        return details;
    }

    public void setDetials(RxPartyActivityDetial detials) {
        this.details = detials;
    }
}
