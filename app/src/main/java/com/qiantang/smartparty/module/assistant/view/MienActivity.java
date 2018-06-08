package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.MienViewModel;
import com.qiantang.smartparty.module.assistant.viewmodel.MsgViewModel;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class MienActivity extends BaseBindActivity {
    private IndexCommonAdapter adapter;
    private MienViewModel viewModel;
    private ActivityRecycleviewBinding binding;

    @Override
    protected void initBind() {
        adapter = new IndexCommonAdapter(R.layout.item_study_state);
        viewModel = new MienViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党建风采");
        binding.toolbar.setResId(R.mipmap.icon_search_black);
        initRv(binding.rv);
        viewModel.getData();
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_right:
                ActivityUtil.startSearchActivity(this, Config.MIEN_TYPE);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
