package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/12.
 */
public class RxIndex {
    private String title;
    private List<RxIndexNews> news;
    private List<RxIndexCommon> study;
    private List<RxVideoStudy> video;
    private List<RxIndexSpeak> speak;
    private List<RxIndexStudy> content;
    private int classifyId;

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RxIndexNews> getNews() {
        return news;
    }

    public void setNews(List<RxIndexNews> news) {
        this.news = news;
    }

    public List<RxIndexCommon> getStudy() {
        return study;
    }

    public void setStudy(List<RxIndexCommon> study) {
        this.study = study;
    }

    public List<RxVideoStudy> getVideo() {
        return video;
    }

    public void setVideo(List<RxVideoStudy> video) {
        this.video = video;
    }

    public List<RxIndexSpeak> getSpeak() {
        return speak;
    }

    public void setSpeak(List<RxIndexSpeak> speak) {
        this.speak = speak;
    }

    public List<RxIndexStudy> getContent() {
        return content;
    }

    public void setContent(List<RxIndexStudy> content) {
        this.content = content;
    }
}
