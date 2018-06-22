package com.qiantang.smartparty.module.index.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentRecycleviewBinding;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.module.index.adapter.LearnAdapter;
import com.qiantang.smartparty.module.index.viewmodel.LearnFragmentViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class FragmentLearn extends BaseBindFragment {
    private FragmentRecycleviewBinding binding;
    private LearnAdapter learnAdapter;
    private LearnFragmentViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        learnAdapter = new LearnAdapter(R.layout.item_learn_list);
        viewModel = new LearnFragmentViewModel(this, learnAdapter);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycleview, container, false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(learnAdapter);
        learnAdapter.setEnableLoadMore(true);
        learnAdapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        learnAdapter.setOnLoadMoreListener(() -> viewModel.loadMore(), binding.rv);
        binding.rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        initRefresh(binding.cptr);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
