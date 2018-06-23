package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class RxMonthScore {
    /**
     * "learningability": 15,
     * "creationtime": "5.24,15:28,
     * "content": "1",
     * "status": 1
     */
    private int learningability;
    private String creationtime;
    private int status;
    private String content;
    private int socre;

    public int getSocre() {
        return socre;
    }

    public void setSocre(int socre) {
        this.socre = socre;
    }

    public int getLearningability() {
        return learningability;
    }

    public void setLearningability(int learningability) {
        this.learningability = learningability;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
