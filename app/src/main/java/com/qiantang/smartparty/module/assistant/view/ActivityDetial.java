package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.databinding.ActivityDetialBinding;
import com.qiantang.smartparty.databinding.ViewActivityDetialHeadBinding;
import com.qiantang.smartparty.module.assistant.adapter.SignRecordAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.ActivityDetialViewModel;
import com.qiantang.smartparty.module.input.viewmodel.InputViewModel;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/5/28.
 * 活动详情
 */
public class ActivityDetial extends BaseBindActivity {
    private ActivityDetialViewModel viewModel;
    private ActivityDetialBinding binding;
    private ViewActivityDetialHeadBinding headBinding;
    private CommentAdapter adapter;
    private InputViewModel inputViewModel;
    private SignRecordAdapter signRecordAdapter;

    @Override
    protected void initBind() {
        signRecordAdapter = new SignRecordAdapter(R.layout.item_sign_record);
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new ActivityDetialViewModel(this, adapter, signRecordAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detial);
        binding.setViewModel(viewModel);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_activity_detial_head, null, false);
        headBinding.setViewModel(viewModel);
        inputViewModel = new InputViewModel(this);
        binding.input.setViewModel(inputViewModel);
    }

    @Override
    public void initView() {
        mImmersionBar.keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        binding.toolbar.setTitle("活动详情");
        binding.toolbar.setIsHide(false);
        initRv(binding.rv);
        initRecordRv(headBinding.rvRecord);
        inputViewModel.setIsPop(true);
    }

    private void initRecordRv(RecyclerView rvRecord) {
        rvRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecord.setAdapter(signRecordAdapter);
        rvRecord.addOnItemTouchListener(viewModel.onSignItemTouchListener());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_comment:
                viewModel.isInput.set(true);
                binding.input.et.requestFocus();
                break;
            case R.id.tv_join:

                break;
            case R.id.rv:
                viewModel.isInput.set(false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel.isInput.get()) {
            viewModel.isInput.set(false);
            return;
        }
        super.onBackPressed();
    }

    private void initRv(RecyclerView rv) {
        adapter.setEnableLoadMore(true);
        AutoUtils.auto(headBinding.getRoot());
        adapter.addHeaderView(headBinding.getRoot());
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        viewModel.getData();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
