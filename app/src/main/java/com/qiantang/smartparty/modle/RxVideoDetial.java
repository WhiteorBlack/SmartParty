package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class RxVideoDetial {
    private RxVideoInfo video;
    private List<RxComment> comment;

    public RxVideoInfo getVideo() {
        return video;
    }

    public void setVideo(RxVideoInfo video) {
        this.video = video;
    }

    public List<RxComment> getComment() {
        return comment;
    }

    public void setComment(List<RxComment> comment) {
        this.comment = comment;
    }
}
