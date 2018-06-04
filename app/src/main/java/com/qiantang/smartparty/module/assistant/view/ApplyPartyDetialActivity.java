package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityApplyPartyDetialBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.ApplyDetailViewModel;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class ApplyPartyDetialActivity extends BaseBindActivity {
    private ApplyDetailViewModel viewModel;
    private ActivityApplyPartyDetialBinding binding;
    @Override
    protected void initBind() {
        viewModel=new ApplyDetailViewModel(this);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_apply_party_detial);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("入党申请");
        binding.toolbar.setIsHide(false);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
