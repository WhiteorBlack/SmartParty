package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.databinding.ActivityRecycleviewCommenboxBinding;
import com.qiantang.smartparty.databinding.ViewWebviewHeadBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.HeadWebViewModel;
import com.qiantang.smartparty.module.assistant.viewmodel.MienDetialViewModel;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.IComment;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class MeetingDetialActivity extends BaseBindActivity implements CommentBox.OnCommentSendClickListener {
    private ViewWebviewHeadBinding headBinding;
    private ActivityRecycleviewCommenboxBinding binding;
    private CommentAdapter adapter;
    private MienDetialViewModel viewModel;
    private HeadWebViewModel headViewModel;

    @Override
    protected void initBind() {
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new MienDetialViewModel(this, adapter);
        headViewModel = new HeadWebViewModel(this);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_webview_head, null, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview_commenbox);
        headBinding.setViewModel(headViewModel);
    }

    @Override
    public void initView() {
        AutoUtils.auto(headBinding.getRoot());
        binding.toolbar.setTitle("会议纪要");
        binding.toolbar.setResId(R.mipmap.icon_share_black);
        headViewModel.initWev(headBinding.llContent);
        initRv(binding.rv);
        binding.input.setOnCommentSendClickListener(this);
        binding.input.showCommentBox();
    }

    private void initRv(RecyclerView rv) {
        headViewModel.isFinish.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.getData();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        adapter.addHeaderView(headBinding.getRoot());

    }

    public void updateCount(int count) {
        headViewModel.setCommentCount(count);
    }

    /**
     * 提交成功之后隐藏键盘
     */
    public void dissmissCommentBox() {
        binding.input.hideInput();
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
