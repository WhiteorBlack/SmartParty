package com.qiantang.smartparty.module.study.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityMyStudyBinding;
import com.qiantang.smartparty.databinding.FragmentStudyBinding;
import com.qiantang.smartparty.databinding.ViewStudyHeadBinding;
import com.qiantang.smartparty.module.study.adapter.StudyAdapter;
import com.qiantang.smartparty.module.study.viewmodel.StudyMyViewModel;
import com.qiantang.smartparty.module.study.viewmodel.StudyViewModel;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.commentwidget.CircleViewHelper;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.CommentWidget;
import com.qiantang.smartparty.widget.commentwidget.IComment;
import com.qiantang.smartparty.widget.commentwidget.KeyboardControlMnanager;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class StudyMyActivity extends BaseBindActivity implements CommentBox.OnCommentSendClickListener {
    private ActivityMyStudyBinding binding;
    private StudyMyViewModel viewModel;
    private StudyAdapter adapter;
    private CircleViewHelper circleViewHelper;

    @Override
    protected void initBind() {
        adapter = new StudyAdapter(R.layout.item_study);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_study);
        viewModel = new StudyMyViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的感悟");
        binding.toolbar.setIsHide(false);
        binding.commentBox.setOnCommentSendClickListener(this);
        initRecycleView(binding.rv);
        initRefresh(binding.cptr);
        if (circleViewHelper == null) {
            circleViewHelper = new CircleViewHelper(this);
        }
        initKeyboardHeightObserver();
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
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


    @Override
    public void update() {
        super.update();
        binding.cptr.autoRefresh();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initRecycleView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:

                break;
        }
    }


    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
