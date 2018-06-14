package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.TestListAdapter;
import com.qiantang.smartparty.module.index.viewmodel.TestListViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestListActivity extends BaseBindActivity {
    private ActivityRecycleviewBinding binding;
    private TestListAdapter listAdapter;
    private TestListViewModel viewModel;

    @Override
    protected void initBind() {
        listAdapter = new TestListAdapter(R.layout.item_test_list);
        viewModel = new TestListViewModel(this, listAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("考试评测");
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        listAdapter.setEnableLoadMore(true);
        listAdapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        listAdapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
