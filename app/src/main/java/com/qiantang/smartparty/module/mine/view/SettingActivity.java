package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivitySettingBinding;
import com.qiantang.smartparty.module.mine.viewmodel.SettingViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SettingActivity extends BaseBindActivity {
    private SettingViewModel viewModel;
    private ActivitySettingBinding binding;

    @Override
    protected void initBind() {
        viewModel = new SettingViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setIsHide(true);
        binding.toolbar.setTitle("设置");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_modify_phone:
                ActivityUtil.startModifyPhoneActivity(this);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
