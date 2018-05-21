package com.qiantang.smartparty.module.study.viewmodel;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class StudyViewModel implements ViewModel {
    private BaseBindFragment fragment;

    public StudyViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void destroy() {

    }
}
