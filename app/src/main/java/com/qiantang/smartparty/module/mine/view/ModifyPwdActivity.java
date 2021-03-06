package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.SimpleFragmentAdapter;
import com.qiantang.smartparty.databinding.ActivityModifyPhoneBinding;
import com.qiantang.smartparty.databinding.ActivityModifyPwdBinding;
import com.qiantang.smartparty.module.mine.fragment.BindPhoneFragment;
import com.qiantang.smartparty.module.mine.fragment.NewPwdFragment;
import com.qiantang.smartparty.module.mine.fragment.VerifyPhoneFragment;
import com.qiantang.smartparty.module.mine.viewmodel.ModifyPhoneViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class ModifyPwdActivity extends BaseBindActivity {
    private ModifyPhoneViewModel viewModel;
    private ActivityModifyPwdBinding binding;

    @Override
    protected void initBind() {
        viewModel = new ModifyPhoneViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_pwd);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("修改密码");
        binding.toolbar.setIsHide(true);
        binding.viewpager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager(), getFragments(), new String[]{"", ""}));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VerifyPhoneFragment());
        fragments.add(new NewPwdFragment());
        return fragments;
    }

    public void nextStep() {
        binding.viewpager.setCurrentItem(1);
        viewModel.setStepNext(false);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
