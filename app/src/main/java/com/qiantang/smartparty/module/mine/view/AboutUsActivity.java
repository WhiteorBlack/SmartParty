package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityAboutUsBinding;
import com.qiantang.smartparty.module.mine.viewmodel.AboutUsViewModel;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class AboutUsActivity extends BaseBindActivity {
    private ActivityAboutUsBinding binding;
    private AboutUsViewModel viewModel;

    @Override
    protected void initBind() {
        viewModel = new AboutUsViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("关于我们");
        binding.toolbar.setIsHide(true);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
