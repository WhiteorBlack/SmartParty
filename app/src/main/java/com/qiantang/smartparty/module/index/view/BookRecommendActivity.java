package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.BookRecommendAdapter;
import com.qiantang.smartparty.module.index.viewmodel.BookRecommendViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/17.
 * 好书推荐
 */
public class BookRecommendActivity extends BaseBindActivity {
    private ActivityRecycleviewBinding binding;
    private BookRecommendAdapter adapter;
    private BookRecommendViewModel viewModel;

    @Override
    protected void initBind() {
        adapter=new BookRecommendAdapter(R.layout.item_book_recommend);
        viewModel=new BookRecommendViewModel(this,adapter);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("好书推荐");
        binding.toolbar.setResId(R.mipmap.icon_search_black);
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(()->viewModel.loadMore(),rv);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
