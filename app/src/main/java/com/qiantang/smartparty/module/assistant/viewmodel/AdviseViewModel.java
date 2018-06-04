package com.qiantang.smartparty.module.assistant.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class AdviseViewModel implements ViewModel {
    private BaseBindActivity activity;

    public AdviseViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
