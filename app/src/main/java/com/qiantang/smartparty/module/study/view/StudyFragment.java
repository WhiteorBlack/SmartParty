package com.qiantang.smartparty.module.study.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentStudyBinding;
import com.qiantang.smartparty.databinding.ViewStudyHeadBinding;
import com.qiantang.smartparty.modle.RxStudyList;
import com.qiantang.smartparty.module.study.adapter.StudyAdapter;
import com.qiantang.smartparty.module.study.viewmodel.StudyViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.SpaceItemDecoration;
import com.qiantang.smartparty.widget.commentwidget.CircleViewHelper;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.CommentWidget;
import com.qiantang.smartparty.widget.commentwidget.IComment;
import com.qiantang.smartparty.widget.commentwidget.KeyboardControlMnanager;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class StudyFragment extends BaseBindFragment implements CommentBox.OnCommentSendClickListener {
    private FragmentStudyBinding binding;
    private StudyViewModel viewModel;
    private StudyAdapter adapter;
    private ViewStudyHeadBinding headBinding;
    private CircleViewHelper circleViewHelper;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new StudyAdapter(R.layout.item_study);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study, container, false);
        viewModel = new StudyViewModel(this, adapter);
        binding.setViewModel(viewModel);
        headBinding = DataBindingUtil.inflate(inflater, R.layout.view_study_head, container, false);
        headBinding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        ImmersionBar.with(this).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        binding.toolbar.setTitle("学习感悟");
        binding.toolbar.setIsHide(false);
        binding.toolbar.setResId(R.mipmap.icon_publish);
        binding.toolbar.ivScan.setOnClickListener(this::onClick);
        binding.commentBox.setOnCommentSendClickListener(this);
        initRecycleView(binding.rv);
        initRefresh(binding.cptr);
        if (circleViewHelper == null) {
            circleViewHelper = new CircleViewHelper(getActivity());
        }
        initKeyboardHeightObserver();
    }

    @Override
    public void update() {
        super.update();
        binding.cptr.autoRefresh(false);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initRecycleView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        AutoUtils.auto(headBinding.getRoot());
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.addHeaderView(headBinding.getRoot());
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(getActivity(), new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = binding.commentBox.getCommentType();
                MyApplication.isPop.set(isVisible);
                if (isVisible) {
                    //定位评论框到view
                    anchorView = circleViewHelper.alignCommentBoxToView(binding.rv, binding.commentBox, commentType);
                } else {
                    //定位到底部
                    binding.commentBox.dismissCommentBox(false);
                    circleViewHelper.alignCommentBoxToViewWhenDismiss(binding.rv, binding.commentBox, commentType, anchorView);
                }
            }
        });
    }

    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid, CommentWidget commentWidget) {
        if (viewHolderRootView != null) {
            circleViewHelper.setCommentAnchorView(viewHolderRootView);
        } else if (commentWidget != null) {
            circleViewHelper.setCommentAnchorView(commentWidget);
        }
        circleViewHelper.setCommentItemDataPosition(itemPos);
        binding.commentBox.toggleCommentBox(momentid, commentWidget == null ? null : commentWidget.getData(), false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                ActivityUtil.startPublishActivity(getActivity());
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

    @Override
    public void onCommentSendClick(View v, IComment comment, String commentContent) {
        if (TextUtils.isEmpty(commentContent)) {
            ToastUtil.toast("评论内容不能为空");
            return;
        }
        viewModel.commentLike(binding.commentBox.getMomentid(), 2, commentContent);
        binding.commentBox.clearDraft();
        binding.commentBox.dismissCommentBox(true);
    }
}
