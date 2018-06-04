package com.qiantang.smartparty.module.mine.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class InfoViewModel implements ViewModel {
    private BaseBindActivity activity;

    public InfoViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
