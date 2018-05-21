package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentIndexBinding;
import com.qiantang.smartparty.module.index.viewmodel.HeadBannerViewModel;
import com.qiantang.smartparty.module.index.viewmodel.IndexViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class IndexFragment extends BaseBindFragment {
    private FragmentIndexBinding binding;
    private IndexViewModel viewModel;
    private HeadBannerViewModel headBannerViewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_index, container, false);
        viewModel = new IndexViewModel(this);
        binding.setViewModel(viewModel);
        headBannerViewModel = new HeadBannerViewModel(this);
        binding.headBanner.setViewModel(headBannerViewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setIsHide(false);
        binding.toolbar.setIsTextHide(true);
        binding.toolbar.setTitle("首页");
        binding.toolbar.ivScan.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                ActivityUtil.startQRActivity(getActivity());
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
        headBannerViewModel.destroy();
    }
}
