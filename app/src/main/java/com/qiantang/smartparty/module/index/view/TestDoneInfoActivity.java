package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityTestDoneInfoBinding;
import com.qiantang.smartparty.databinding.ActivityTestInfoBinding;
import com.qiantang.smartparty.module.index.viewmodel.TestDoneViewModel;
import com.qiantang.smartparty.module.index.viewmodel.TestInfoViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                ActivityUtil.startTestRecordActivity(this, viewModel.getTestInfo().getUserquestionnaire_id());
                break;
            case R.id.btn_retry:
                ActivityUtil.startTestDeitalInfoActivity(this, viewModel.getTestInfo().getQuestionnaire_id(), viewModel.getTestInfo().getClippingtime() * 60, viewModel.getTestInfo().getNumber(),true);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
