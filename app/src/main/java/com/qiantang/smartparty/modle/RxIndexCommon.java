package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxIndexCommon {
    private int read;
    private String createTime;
    private String contentId;
    private String title;
    private int pl;
    private String imgSrc;
    private String printurl;

    public String getPrinturl() {
        return printurl;
    }

    public void setPrinturl(String printurl) {
        this.printurl = printurl;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
