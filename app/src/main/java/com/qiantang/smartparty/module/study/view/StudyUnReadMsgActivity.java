package com.qiantang.smartparty.module.study.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.study.adapter.UnreadAdapter;
import com.qiantang.smartparty.module.study.viewmodel.UnreadMsgViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class StudyUnReadMsgActivity extends BaseBindActivity {
    private ActivityRecycleviewBinding binding;
    private UnreadMsgViewModel viewModel;
    private UnreadAdapter adapter;

    @Override
    protected void initBind() {
        adapter = new UnreadAdapter(R.layout.item_study_unread_msg);
        viewModel = new UnreadMsgViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("学习感悟");
        viewModel.getData(1);
        initRv(binding.rv);
        initRefresh(binding.cptr);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
