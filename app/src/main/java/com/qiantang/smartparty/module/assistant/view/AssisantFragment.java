package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentAssisantBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.AssisantViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class AssisantFragment extends BaseBindFragment {
    private FragmentAssisantBinding binding;
    private AssisantViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        viewModel = new AssisantViewModel(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assisant, container, false);
        binding.setViewMdoel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党建助手");
        binding.toolbar.setIsHide(true);
        binding.toolbar.setIsTextHide(true);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
