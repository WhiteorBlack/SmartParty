package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityDateDeptBinding;
import com.qiantang.smartparty.module.mine.viewmodel.DateDeptViewModel;

public class DateDeptActivity extends BaseBindActivity {

    private ActivityDateDeptBinding binding;
    private DateDeptViewModel viewModel;
    private int type = 0;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date_dept);
        viewModel = new DateDeptViewModel(this);
        binding.setViewModel(viewModel);
    }

    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        String name = getIntent().getStringExtra("info");
        viewModel.info.set(name);
        if (type == 0) {
            viewModel.infoTitle.set("所属组织");
            binding.toolbar.setTitle("所属组织");
            viewModel.getOrgData();
        } else {
            viewModel.infoTitle.set("入党时间");
            binding.toolbar.setTitle("入党时间");
            viewModel.initDateData();
        }
        binding.toolbar.setRight("保存");
    }

    @Override
    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                if (type == 0) {
                    viewModel.modifyName();
                } else {
                    viewModel.modifyNick();
                }
                break;
            case R.id.ll_name:
                viewModel.showPop();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
