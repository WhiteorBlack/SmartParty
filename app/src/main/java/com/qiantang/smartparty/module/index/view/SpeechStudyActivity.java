package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivitySpeechStudyBinding;
import com.qiantang.smartparty.databinding.ActivityVideoStudyBinding;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.adapter.SpechAdapter;
import com.qiantang.smartparty.module.index.viewmodel.SpeechStudyViewModel;
import com.qiantang.smartparty.module.index.viewmodel.VideoStudyViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/5/22.
 * 系列讲话
 */
public class SpeechStudyActivity extends BaseBindActivity {
    private SpeechStudyViewModel viewModel;
    private SpechAdapter adapter;
    private ActivitySpeechStudyBinding binding;

    @Override
    protected void initBind() {
        adapter = new SpechAdapter(R.layout.item_index_speech);
        viewModel = new SpeechStudyViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_speech_study);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("系列讲话");
        binding.toolbar.setIsHide(false);
        binding.toolbar.setResId(R.mipmap.icon_search_black);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                ActivityUtil.startSearchActivity(this, Config.SPEECH_STUDY);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
