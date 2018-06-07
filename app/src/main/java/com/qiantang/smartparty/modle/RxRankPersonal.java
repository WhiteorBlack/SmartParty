package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RxRankPersonal {
   private RxRankPersonalMap myPeopleMap;
   private List<RxRankPersonalList> peopleList;

   public RxRankPersonalMap getMyPeopleMap() {
      return myPeopleMap;
   }

   public void setMyPeopleMap(RxRankPersonalMap myPeopleMap) {
      this.myPeopleMap = myPeopleMap;
   }

   public List<RxRankPersonalList> getPeopleList() {
      return peopleList;
   }

   public void setPeopleList(List<RxRankPersonalList> peopleList) {
      this.peopleList = peopleList;
   }
}
