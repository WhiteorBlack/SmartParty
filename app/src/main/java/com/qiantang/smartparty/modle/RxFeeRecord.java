package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class RxFeeRecord {
    /**
     * "money": 555,            党费金额
     "endtime": "2018-05-29 14:34:55",        缴费时间
     "type": "1",                党费类型 0 正常党费 1 特殊党费
     "dues_name": "惊喜"            党费项目名称
     */
    private double money;
    private String endtime;
    private int type;
    private String dues_name;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDues_name() {
        return dues_name;
    }

    public void setDues_name(String dues_name) {
        this.dues_name = dues_name;
    }
}
