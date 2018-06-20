package com.qiantang.smartparty.module.mine.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.BookRecommendAdapter;
import com.qiantang.smartparty.module.mine.viewmodel.BookFragmentViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/17.
 * 好书推荐
 */
public class BookRecommendFragment extends BaseBindFragment {
    private FragmentRecycleviewBinding binding;
    private BookRecommendAdapter adapter;
    private BookFragmentViewModel viewModel;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new BookRecommendAdapter(R.layout.item_book_recommend);
        viewModel = new BookFragmentViewModel(this, adapter);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_recycleview, container, false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
