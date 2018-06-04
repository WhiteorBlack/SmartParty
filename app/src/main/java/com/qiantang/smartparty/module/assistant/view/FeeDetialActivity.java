package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityFeeDetialBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.FeeDetialViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeDetialActivity extends BaseBindActivity {
    private FeeDetialViewModel viewModel;
    private ActivityFeeDetialBinding binding;

    @Override
    protected void initBind() {
        viewModel = new FeeDetialViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_detial);
        binding.setViewModel(viewModel);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:

                break;
            case R.id.tv_right:
                ActivityUtil.startFeeRecordActivity(this);
                break;
        }
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党费缴纳");
        binding.toolbar.setRight("缴费记录");
        binding.toolbar.setIsHide(false);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
