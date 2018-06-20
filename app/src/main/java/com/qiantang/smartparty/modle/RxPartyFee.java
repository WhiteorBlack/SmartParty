package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class RxPartyFee {
    /**
     * "record_id": 13,
     "dues_end": "2018-06-09",            党费截止时间
     "money": 66,                        党费金额  如果为0 表示 自由金额
     "overdue": "0",                        是否逾期 0 表示逾期 其它值 表示 不逾期
     "user_id": 38,
     "duesId": 19,
     "type": "1",                        党费类型  0 表示正常党费 1 表示特殊党费
     "dues_name": "交钱交钱"            党费项目名称
     */
    private String record_id;
    private String dues_end;
    private double money;
    private int overdue;
    private String user_id;
    private String duesId;
    private int type;
    private String dues_name;

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getDues_end() {
        return dues_end;
    }

    public void setDues_end(String dues_end) {
        this.dues_end = dues_end;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getOverdue() {
        return overdue;
    }

    public void setOverdue(int overdue) {
        this.overdue = overdue;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDuesId() {
        return duesId;
    }

    public void setDuesId(String duesId) {
        this.duesId = duesId;
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
