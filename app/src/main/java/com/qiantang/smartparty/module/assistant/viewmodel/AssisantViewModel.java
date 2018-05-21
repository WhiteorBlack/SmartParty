package com.qiantang.smartparty.module.assistant.viewmodel;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class AssisantViewModel implements ViewModel {
    private BaseBindFragment fragment;

    public AssisantViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void destroy() {

    }
}
