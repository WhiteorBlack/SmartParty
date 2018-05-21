package com.qiantang.smartparty.module.spalsh.view;

import android.databinding.DataBindingUtil;
import android.os.Handler;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivitySplashBinding;
import com.qiantang.smartparty.module.spalsh.viewmodel.SplashViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class SplahsActivity extends BaseBindActivity {
    private SplashViewModel viewModel;
    private ActivitySplashBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        viewModel=new SplashViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        intent();
    }

    private void intent(){
        new Handler().postDelayed(() -> viewModel.jumpNextPage(), Config.SPLASH_TIME);
    }


    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
