package com.qiantang.smartparty.module.login.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityLoginBinding;
import com.qiantang.smartparty.module.login.viewmodel.LoginViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.umeng.socialize.UMShareAPI;

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
    protected void onResume() {
        super.onResume();
        if (MyApplication.isLogin()){
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                viewModel.getLogin();
                break;
            case R.id.tv_register:
                ActivityUtil.startRegisterActivity(this);
                break;
            case R.id.iv_qq:
                viewModel.authQQ();
                break;
            case R.id.iv_wechat:
                viewModel.authWechat();
                break;
            case R.id.tv_count:
                viewModel.sendMsg();
                break;
            case R.id.tv_password:
                viewModel.pwdLogin();
                break;
            case R.id.tv_code:
                viewModel.codeLogin();
                break;
            case R.id.iv_close:
                onBackPressed();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
