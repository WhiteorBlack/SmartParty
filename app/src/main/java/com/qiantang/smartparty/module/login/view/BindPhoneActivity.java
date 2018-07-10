package com.qiantang.smartparty.module.login.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityBindPhoneBinding;
import com.qiantang.smartparty.databinding.FragmentBindPhoneBinding;
import com.qiantang.smartparty.module.login.viewmodel.BindViewModel;
import com.qiantang.smartparty.module.mine.viewmodel.FragmentBindViewModel;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.WebUtil;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class BindPhoneActivity extends BaseBindActivity {
    private BindViewModel viewModel;
    private ActivityBindPhoneBinding binding;


    @Override
    protected void initBind() {
        viewModel = new BindViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("绑定手机号");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.cb_sms:
                viewModel.sendMsg();
                closeInput();
                break;
            case R.id.btn_confirm:
                closeInput();
                if (viewModel.isAgree.get()) {
                    viewModel.bindPhone();
                } else {
                    ToastUtil.toast("请阅读并同意【智慧党建用户协议】");
                }
                break;
            case R.id.tv_protocal:
                WebUtil.jumpWeb(this, URLs.USER_PROTOCOL + 4, "用户协议");
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
