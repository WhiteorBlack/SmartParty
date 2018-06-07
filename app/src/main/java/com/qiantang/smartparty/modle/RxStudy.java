package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class RxStudy {
    private RxStudyUserMap userMap;
    private List<RxStudyList> resultList;

    public RxStudyUserMap getUserMap() {
        return userMap;
    }

    public void setUserMap(RxStudyUserMap userMap) {
        this.userMap = userMap;
    }

    public List<RxStudyList> getResultList() {
        return resultList;
    }

    public void setResultList(List<RxStudyList> resultList) {
        this.resultList = resultList;
    }
}
