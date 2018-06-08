package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class RxAssientHome {
    private List<RxIndexCommon> fc;
    private List<RxActivity> dj;
    private List<RxMsg> tz;

    public List<RxIndexCommon> getFc() {
        return fc;
    }

    public void setFc(List<RxIndexCommon> fc) {
        this.fc = fc;
    }

    public List<RxActivity> getDj() {
        return dj;
    }

    public void setDj(List<RxActivity> dj) {
        this.dj = dj;
    }

    public List<RxMsg> getTz() {
        return tz;
    }

    public void setTz(List<RxMsg> tz) {
        this.tz = tz;
    }
}
