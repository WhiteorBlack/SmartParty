package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentMineBinding;
import com.qiantang.smartparty.module.mine.viewmodel.MineViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class MineFragment extends BaseBindFragment {
    private FragmentMineBinding binding;
    private MineViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        viewModel = new MineViewModel(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的");
        binding.toolbar.setIsTextHide(true);
        binding.toolbar.setIsHide(true);
    }

    @Override
    protected void viewModelDestroy() {

    }
}
