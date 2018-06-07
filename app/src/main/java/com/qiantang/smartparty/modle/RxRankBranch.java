package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/5.
 */
public class RxRankBranch {
    private List<RxRankBranchList> branchList;
    private RxRankBranchMap peopleMap;

    public List<RxRankBranchList> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<RxRankBranchList> branchList) {
        this.branchList = branchList;
    }

    public RxRankBranchMap getPeopleMap() {
        return peopleMap;
    }

    public void setPeopleMap(RxRankBranchMap peopleMap) {
        this.peopleMap = peopleMap;
    }
}
