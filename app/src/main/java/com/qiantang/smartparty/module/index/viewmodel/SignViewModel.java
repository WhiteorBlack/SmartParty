package com.qiantang.smartparty.module.index.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class SignViewModel implements ViewModel {
    private BaseBindActivity activity;

    public SignViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
