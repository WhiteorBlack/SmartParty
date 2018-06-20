package com.qiantang.smartparty.module.mine.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.module.mine.adapter.VideoStudyFragmentAdapter;
import com.qiantang.smartparty.module.mine.viewmodel.VideoStudyFragmentViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.databinding.FragmentRecycleviewBinding;

/**
 * Created by zhaoyong bai on 2018/5/22.
 * 视频学习
 */
public class VideoStudyFragment extends BaseBindFragment {
    private VideoStudyFragmentViewModel viewModel;
    private VideoStudyFragmentAdapter adapter;
    private FragmentRecycleviewBinding binding;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new VideoStudyFragmentAdapter(R.layout.item_video_study_fragment);
        viewModel = new VideoStudyFragmentViewModel(this, adapter);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_recycleview, container, false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.onLoadMore(), rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
