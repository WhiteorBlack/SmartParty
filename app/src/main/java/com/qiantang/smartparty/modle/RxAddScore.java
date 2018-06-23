package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/23.
 * 增加积分的model
 */
public class RxAddScore {
    private int type;
    private int time;
    private String id;

    public RxAddScore(int type, int time, String id) {
        this.type = type;
        this.time = time;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
