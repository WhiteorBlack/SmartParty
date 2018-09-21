package com.qiantang.smartparty.module.mine.fragment;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentBindPhoneBinding;
import com.qiantang.smartparty.databinding.FragmentNewPwdBinding;
import com.qiantang.smartparty.module.mine.viewmodel.FragmentBindViewModel;
import com.qiantang.smartparty.module.mine.viewmodel.FragmentNewPwdViewModel;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class NewPwdFragment extends BaseBindFragment {
    private FragmentNewPwdViewModel viewModel;
    private FragmentNewPwdBinding binding;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        viewModel = new FragmentNewPwdViewModel(this);
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_new_pwd,container,false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {

    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
