package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class RxLearningList {
    private int read;
    private String contentId;
    private String printurl;
    private String creationtime;
    private String title;
    private int commentSum;

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getPrinturl() {
        return printurl;
    }

    public void setPrinturl(String printurl) {
        this.printurl = printurl;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }
}
