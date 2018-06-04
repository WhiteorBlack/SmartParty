package com.qiantang.smartparty.module.login.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class CompeteViewModel implements ViewModel {
    private BaseBindActivity activity;

    public CompeteViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否本单位人员
     */
    public void nation() {

    }

    /**
     * 所属组织
     */
    public void orgPop() {
    }

    /**
     * 身份
     */
    public void idPop() {

    }

    /**
     * 入党时间
     */
    public void datePop() {

    }

    public void commit() {
    }
}
