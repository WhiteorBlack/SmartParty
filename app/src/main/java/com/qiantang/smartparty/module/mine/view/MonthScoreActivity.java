package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityMonthScoreBinding;
import com.qiantang.smartparty.module.mine.adapter.MonthScoreAdapter;
import com.qiantang.smartparty.module.mine.viewmodel.MonthViewModel;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.utils.WebUtil;
import com.qiantang.smartparty.widget.RecycleViewDivider;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class MonthScoreActivity extends BaseBindActivity {
    private MonthViewModel viewModel;
    private ActivityMonthScoreBinding binding;
    private MonthScoreAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month_score);
        adapter = new MonthScoreAdapter(R.layout.item_month_score);
        viewModel = new MonthViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        initRv(binding.rv);
        viewModel.testData();
    }

    private void initRv(RecyclerView rv) {
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.onLoadMore(), rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_intro:
                WebUtil.jumpWeb(this, URLs.USER_PROTOCOL+1,"学习值说明");
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
