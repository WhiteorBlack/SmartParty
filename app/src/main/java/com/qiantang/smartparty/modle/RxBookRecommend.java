package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxBookRecommend {
    private int commentCount;
    private String creationtime;
    private String contentId;
    private String title;
    private String printurl;
    private String intro;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }
}
