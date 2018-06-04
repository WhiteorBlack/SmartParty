package com.qiantang.smartparty.module.login.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRegisterBinding;
import com.qiantang.smartparty.module.login.viewmodel.RegisterViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class RegisterActivity extends BaseBindActivity {
    private RegisterViewModel viewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void initBind() {
        viewModel = new RegisterViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                ActivityUtil.startCompeteActivity(this);
                break;
            case R.id.tv_count:
                viewModel.sendMsg();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
