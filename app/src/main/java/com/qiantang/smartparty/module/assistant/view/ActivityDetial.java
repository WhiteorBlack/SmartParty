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
import com.qiantang.smartparty.widget.commentwidget.CircleViewHelper;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.IComment;
import com.qiantang.smartparty.widget.commentwidget.KeyboardControlMnanager;

/**
 * Created by zhaoyong bai on 2018/5/28.
 * 活动详情
 */
public class ActivityDetial extends BaseBindActivity implements CommentBox.OnCommentSendClickListener {
    private ActivityDetialViewModel viewModel;
    private ActivityDetialBinding binding;
    private ViewActivityDetialHeadBinding headBinding;
    private CommentAdapter adapter;
    private SignRecordAdapter signRecordAdapter;
    private CircleViewHelper circleViewHelper;

    @Override
    protected void initBind() {
        signRecordAdapter = new SignRecordAdapter(R.layout.item_sign_record);
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new ActivityDetialViewModel(this, adapter, signRecordAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detial);
        binding.setViewModel(viewModel);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_activity_detial_head, null, false);
        headBinding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("活动详情");
        binding.toolbar.setIsHide(false);
        initRv(binding.rv);
        initRecordRv(headBinding.rvRecord);
        if (circleViewHelper == null) {
            circleViewHelper = new CircleViewHelper(this);
        }
        binding.input.setOnCommentSendClickListener(this);
        if (viewModel.getStatus() >= 3) {
            binding.input.showCommentBox();
        }
    }

    private void initRecordRv(RecyclerView rvRecord) {
        rvRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecord.setAdapter(signRecordAdapter);
        rvRecord.addOnItemTouchListener(viewModel.onSignItemTouchListener());
        initKeyboardHeightObserver();
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, (keyboardHeight, isVisible) -> {
            if (isVisible) {
                //定位评论框到view
//                circleViewHelper.alignCommentBoxToView(binding.rv, binding.input, binding.input.getCommentType());
            } else {
                //定位到底部
                binding.input.dismissCommentBox(true);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_comment:
                showCommentBox();
                break;
            case R.id.tv_join:
                viewModel.enroll();
                break;
        }
    }

    private void showCommentBox() {
        viewModel.isInput.set(true);
        binding.input.toggleCommentBox(viewModel.getDetials().getActivityId(), "发表评论", false);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.isInput.get() && viewModel.getStatus() == 3) {
            viewModel.isInput.set(false);
            binding.input.dismissCommentBoxWithoutShowing(false);
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
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        viewModel.getData();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

    @Override
    public void onCommentSendClick(View v, IComment comment, String commentContent) {
        viewModel.comment(commentContent);
    }
}
