package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityAdviseBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.AdviseViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class AdviseActivity extends BaseBindActivity {
    private AdviseViewModel viewModel;
    private ActivityAdviseBinding binding;

    @Override
    protected void initBind() {
        viewModel = new AdviseViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advise);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("意见反馈");
        binding.toolbar.setRight("反馈记录");
        binding.toolbar.setIsHide(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                ActivityUtil.startAdviseRecordActivity(this);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
