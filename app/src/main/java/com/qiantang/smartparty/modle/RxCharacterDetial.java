package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxCharacterDetial {
    private int isComment;
    private int count;
    private List<RxComment> pl;
    private RxCharacterInfo rw;
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

    public List<RxComment> getPl() {
        return pl;
    }

    public void setPl(List<RxComment> pl) {
        this.pl = pl;
    }

    public RxCharacterInfo getRw() {
        return rw;
    }

    public void setRw(RxCharacterInfo rw) {
        this.rw = rw;
    }

    public List<RxPicUrl> getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(List<RxPicUrl> imgSrc) {
        this.imgSrc = imgSrc;
    }
}
