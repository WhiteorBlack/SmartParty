package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class RxSpeechDetial {
    private RxSpeechInfo video;
    private List<RxComment> comment;

    public RxSpeechInfo getVideo() {
        return video;
    }

    public void setVideo(RxSpeechInfo video) {
        this.video = video;
    }

    public List<RxComment> getComment() {
        return comment;
    }

    public void setComment(List<RxComment> comment) {
        this.comment = comment;
    }
}
