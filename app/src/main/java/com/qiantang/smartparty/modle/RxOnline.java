package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/15.
 */
public class RxOnline {
    private String pictureurl;
    private List<RxLearningList> list;

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public List<RxLearningList> getList() {
        return list;
    }

    public void setList(List<RxLearningList> list) {
        this.list = list;
    }
}
