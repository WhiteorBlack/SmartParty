package com.qiantang.smartparty.module.login.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class SimpleViewModel implements ViewModel {
    private BaseBindActivity activity;

    public SimpleViewModel(BaseBindActivity activity) {
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
     * 身份
     */
    public void idPop() {

    }


    public void commit() {
    }
}
