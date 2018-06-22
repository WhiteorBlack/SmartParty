package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityNewsBinding;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.viewmodel.NewsViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/5/22.
 * 系列讲话
 */
public class NewsActivity extends BaseBindActivity {
    private NewsViewModel viewModel;
    private IndexCommonAdapter adapter;
    private ActivityNewsBinding binding;
    private int type;

    @Override
    protected void initBind() {
        adapter = new IndexCommonAdapter(R.layout.item_news);
        viewModel = new NewsViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        binding.toolbar.setResId(R.mipmap.icon_search_black);
        initRv(binding.rv);
        initData();
        initRefresh(binding.cptr);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.testData(type,1);
    }

    private void initData() {
        String title = "";

        switch (type) {
            case 8:
                title = "新闻快报";
                break;
            case 9:
                title = "学习动态";
                break;
        }
        viewModel.testData(type,1);
        binding.toolbar.setTitle(title);
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
            case R.id.tv_right:

                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
