package com.qiantang.smartparty.module.spalsh.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class SplashViewModel implements ViewModel {
    private BaseBindActivity activity;

    public SplashViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }

    public void jumpNextPage() {
        ActivityUtil.startMainActivity(activity);
        activity.onBackPressed();
    }
}
