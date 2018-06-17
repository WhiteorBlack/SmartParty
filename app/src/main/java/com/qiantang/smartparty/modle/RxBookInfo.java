package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class RxBookInfo {
    private int commentSum;
    private String intro;
    private String title;
    private int collect;
    private String content;
    private String printurl;

    public String getPrinturl() {
        return printurl;
    }

    public void setPrinturl(String printurl) {
        this.printurl = printurl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
