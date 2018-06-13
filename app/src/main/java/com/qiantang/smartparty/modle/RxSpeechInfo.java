package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/12.
 */
public class RxSpeechInfo {
    private String speakurl;
    private int number;
    private int speaktype;
    private int review;
    private String intro;
    private String creationtime;
    private String title;
    private int collect;
    private String speak_id;
    private String content;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSpeakurl() {
        return speakurl;
    }

    public void setSpeakurl(String speakurl) {
        this.speakurl = speakurl;
    }

    public int getSpeaktype() {
        return speaktype;
    }

    public void setSpeaktype(int speaktype) {
        this.speaktype = speaktype;
    }

    public String getSpeak_id() {
        return speak_id;
    }

    public void setSpeak_id(String speak_id) {
        this.speak_id = speak_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

}
