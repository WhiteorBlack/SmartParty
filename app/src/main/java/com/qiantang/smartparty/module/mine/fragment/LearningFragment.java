package com.qiantang.smartparty.module.mine.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.LearnAdapter;
import com.qiantang.smartparty.module.index.viewmodel.LearnFragmentViewModel;
import com.qiantang.smartparty.module.mine.viewmodel.LearningFragmentViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class LearningFragment extends BaseBindFragment {
    private FragmentRecycleviewBinding binding;
    private LearnAdapter learnAdapter;
    private LearningFragmentViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        learnAdapter = new LearnAdapter(R.layout.item_learn_list);
        viewModel = new LearningFragmentViewModel(this, learnAdapter);
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
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
