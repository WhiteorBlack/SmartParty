package com.qiantang.smartparty.module.mine.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SettingViewModel implements ViewModel {
    private BaseBindActivity activity;

    public SettingViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
