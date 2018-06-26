package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.TestListAdapter;
import com.qiantang.smartparty.module.index.viewmodel.TestListViewModel;
import com.qiantang.smartparty.module.mine.viewmodel.MyTestViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class MyTestActivity extends BaseBindActivity {
    private ActivityRecycleviewBinding binding;
    private TestListAdapter listAdapter;
    private MyTestViewModel viewModel;

    @Override
    protected void initBind() {
        listAdapter = new TestListAdapter(R.layout.item_test_mine);
        viewModel = new MyTestViewModel(this, listAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的评测");
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
