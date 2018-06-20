package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityFeeRecordBinding;
import com.qiantang.smartparty.module.assistant.adapter.FeeRecordAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.FeeRecordViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeRecordActivity extends BaseBindActivity {
    private FeeRecordViewModel viewModel;
    private FeeRecordAdapter adapter;
    private ActivityFeeRecordBinding binding;

    @Override
    protected void initBind() {
        adapter = new FeeRecordAdapter(R.layout.item_fee_record);
        viewModel = new FeeRecordViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_record);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("缴费记录");
        binding.toolbar.setIsHide(true);
        initRv(binding.rv);
        viewModel.testData();
    }

    private void initRv(RecyclerView rv) {
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(()->viewModel.loadMore(),rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
