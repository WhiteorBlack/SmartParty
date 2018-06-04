package com.qiantang.smartparty.module.login.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityLoginBinding;
import com.qiantang.smartparty.module.login.viewmodel.LoginViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class LoginActivity extends BaseBindActivity {
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void initBind() {
        viewModel = new LoginViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
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

                break;
            case R.id.tv_register:
                ActivityUtil.startRegisterActivity(this);
                break;
            case R.id.iv_qq:

                break;
            case R.id.iv_wechat:

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
