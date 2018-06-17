package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.viewmodel.ParagonViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class ParagonActivity extends BaseBindActivity {
    private IndexCommonAdapter adapter;
    private ParagonViewModel viewModel;
    private ActivityRecycleviewBinding binding;

    @Override
    protected void initBind() {
        adapter = new IndexCommonAdapter(R.layout.item_paragon);
        viewModel = new ParagonViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("先进典范");
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(()->viewModel.loadMore(),rv);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
