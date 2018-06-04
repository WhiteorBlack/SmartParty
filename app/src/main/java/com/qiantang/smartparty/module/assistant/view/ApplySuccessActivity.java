package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityApplySuccessBinding;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class ApplySuccessActivity extends BaseBindActivity {
    private ActivityApplySuccessBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_success);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("入党申请");
        binding.toolbar.setIsHide(true);
    }

    @Override
    protected void viewModelDestroy() {

    }
}
