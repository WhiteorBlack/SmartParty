package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityTestDoneInfoBinding;
import com.qiantang.smartparty.databinding.ActivityTestInfoBinding;
import com.qiantang.smartparty.module.index.viewmodel.TestDoneViewModel;
import com.qiantang.smartparty.module.index.viewmodel.TestInfoViewModel;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDoneInfoActivity extends BaseBindActivity {
    private ActivityTestDoneInfoBinding binding;
    private TestDoneViewModel viewModel;

    @Override
    protected void initBind() {
        viewModel = new TestDoneViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_done_info);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("考试评测");
        viewModel.getData();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
