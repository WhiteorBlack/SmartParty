package com.qiantang.smartparty.module.mine.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.CacheKey;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_modify_phone:
                ActivityUtil.startModifyPhoneActivity(this);
                break;
            case R.id.btn_confirm:
                viewModel.logout();
                break;
            case R.id.ll_service:
                callPhone(viewModel.servicePhone.get());
                break;
        }

    }

    private void callPhone(String phone) {
        Intent intent1 = new Intent(Intent.ACTION_DIAL);
        intent1.setData(Uri.parse("tel:" + phone));
        startActivity(intent1);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
