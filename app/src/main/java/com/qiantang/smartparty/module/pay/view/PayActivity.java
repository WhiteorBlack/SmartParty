package com.qiantang.smartparty.module.pay.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityPayBinding;
import com.qiantang.smartparty.module.pay.viewmodel.PayViewModel;


public class PayActivity extends BaseBindActivity {

    private ActivityPayBinding binding;
    private PayViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        viewModel = new PayViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("确认支付");
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_aliPay:
                viewModel.isLoading.set(false);
                viewModel.aliPay();
                break;
            case R.id.ll_wxPay:
                viewModel.isLoading.set(false);
                viewModel.getWeixinPayInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

}
