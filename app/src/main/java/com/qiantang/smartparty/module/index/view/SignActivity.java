package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.module.index.viewmodel.SignViewModel;
import com.qiantang.smartparty.databinding.ActivitySignBinding;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class SignActivity extends BaseBindActivity {
    private SignViewModel viewModel;
    private ActivitySignBinding binding;

    @Override
    protected void initBind() {
        viewModel = new SignViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:

                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
