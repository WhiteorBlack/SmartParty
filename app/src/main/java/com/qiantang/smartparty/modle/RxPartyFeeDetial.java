package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class RxPartyFeeDetial {
    /**
     *"record_id": 13,
     "dues_end": "2018-06-09",                            党费缴纳结束时间
     "dues_remark": "<p>大家路爱了交钱啦</p>",            党费说明  一般 特殊党费时会返回
     "dept_name": "计算机科学与技术系党支部",            用户所属部门名称
     "type": "1",                党费类型 0表示正常党费   1表示特殊党费    当 为0时  党费说明是写死的 一般 不会返回dues_remark 字段
     "imgSrc": "/uploadDir/2018/06/01/b21210376bbbdc9a43909f93a959c9a0.jpg",     图片地址 多张图片时 是以  逗号 隔开的
     "dues_name": "交钱交钱",        党费项目名称
     "money":66,                党费金额  如果为0表示自由金额
     "username": "朋林23"        用户姓名
     */
    private String record_id;
    private String dues_end;
    private double money;
    private int overdue;
    private String user_id;
    private String duesId;
    private int type;
    private String dues_name;
    private String dues_remark;
    private String dept_name;
    private String imgSrc;
    private String username;
    public String getRecord_id() {
        return record_id;
    }

    public String getDues_remark() {
        return dues_remark;
    }

    public void setDues_remark(String dues_remark) {
        this.dues_remark = dues_remark;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
