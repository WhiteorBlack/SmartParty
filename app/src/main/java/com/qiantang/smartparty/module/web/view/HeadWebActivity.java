package com.qiantang.smartparty.module.web.view;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivityRecycleviewCommenboxBinding;
import com.qiantang.smartparty.databinding.ViewWebviewHeadBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.HeadWebViewModel;
import com.qiantang.smartparty.module.assistant.viewmodel.MienDetialViewModel;
import com.qiantang.smartparty.module.input.viewmodel.InputViewModel;
import com.qiantang.smartparty.module.web.viewmodel.WebHeadViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.IComment;
import com.qiantang.smartparty.widget.commentwidget.KeyboardControlMnanager;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class HeadWebActivity extends BaseBindActivity {
    private ViewWebviewHeadBinding headBinding;
    private ActivityRecycleviewCommenboxBinding binding;
    private CommentAdapter adapter;
    private WebHeadViewModel viewModel;
    private HeadWebViewModel headViewModel;
    private InputViewModel inputViewModel;
    private int type = 0;

    @Override
    protected void initBind() {
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new WebHeadViewModel(this, adapter);
        inputViewModel = new InputViewModel(this);
        headViewModel = new HeadWebViewModel(this);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_webview_head, null, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview_commenbox);
        headBinding.setViewModel(headViewModel);
        binding.input.setViewModel(inputViewModel);
    }

    @Override
    public void initView() {
        String title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);
        if (type==0){
            inputViewModel.setIsPop(true);
        }
        AutoUtils.auto(headBinding.getRoot());
        binding.toolbar.setTitle(title);
        binding.toolbar.setResId(R.mipmap.icon_share_white);
        headViewModel.initWev(headBinding.llContent);
        inputViewModel.setHint("发表学习感悟...");
        inputViewModel.setShareVis(false);
        initRv(binding.rv);
        initRefresh(binding.cptr);
        initKeyboardHeightObserver();
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                if (type == 0) {
                    inputViewModel.setIsPop(true);
                    return;
                }
                if (isVisible) { //键盘弹出
                    inputViewModel.setIsPop(true);
                } else { //键盘收起的时候判断是否有文字输入,如果有则继续展示发送按钮
                    if (TextUtils.isEmpty(inputViewModel.getTextString())) {
                        inputViewModel.setIsPop(false);
                    } else {
                        inputViewModel.setIsPop(true);
                    }
                }
            }
        });
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1, true);
    }

    private void initRv(RecyclerView rv) {
        headViewModel.isFinish.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                viewModel.getData(1, false);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEnableLoadMore(Config.isLoadMore);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        if (Config.isLoadMore)
        {
            adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        }
        adapter.addHeaderView(headBinding.getRoot());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_send:
                if (!MyApplication.isLogin()){
                    ActivityUtil.startLoginActivity(this);
                    return;
                }
                viewModel.comment(inputViewModel.getTextString());
                inputViewModel.setTextString("");
                closeInput();
                break;
            case R.id.iv_share:

                break;
            case R.id.iv_collect:
                viewModel.cancelPrase();
                break;
            case R.id.iv_uncollect:
                viewModel.prase();
                break;
        }
    }

    public void updateCollect(boolean isCollect) {
        inputViewModel.setIsCollect(isCollect);
    }

    public void updateCount(int count) {
        headViewModel.setCommentCount(count);
    }

    /**
     * 提交成功之后隐藏键盘
     */
    public void dissmissCommentBox() {
        closeInput();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

}
