package com.qiantang.smartparty.module.spalsh.view;

import android.databinding.DataBindingUtil;
import android.os.Handler;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivitySplashBinding;
import com.qiantang.smartparty.module.spalsh.viewmodel.SplashViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.SharedPreferences;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class SplahsActivity extends BaseBindActivity {
    private SplashViewModel viewModel;
    private ActivitySplashBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        viewModel = new SplashViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        if (SharedPreferences.getInstance().getBoolean(CacheKey.FIRST, true)) {
            startGuide();
        } else {
            intent();
        }
    }

    private void startGuide() {
        ActivityUtil.startGuideActivity(this);
    }

    private void intent() {
        new Handler().postDelayed(() -> viewModel.jumpNextPage(), Config.SPLASH_TIME);
    }


    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
