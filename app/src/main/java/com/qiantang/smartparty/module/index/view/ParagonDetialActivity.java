package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.databinding.ActivityCharacterDetialBinding;
import com.qiantang.smartparty.databinding.ViewCharacterdetialHeadBinding;
import com.qiantang.smartparty.databinding.ViewParagonHeadBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.CharacterDetialViewModel;
import com.qiantang.smartparty.module.index.viewmodel.ParagonDetialViewModel;
import com.qiantang.smartparty.module.index.viewmodel.ParagonViewModel;
import com.qiantang.smartparty.module.input.viewmodel.InputViewModel;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.RecycleViewUtils;
import com.qiantang.smartparty.widget.MyBanner;
import com.qiantang.smartparty.widget.commentwidget.CommentBox;
import com.qiantang.smartparty.widget.commentwidget.IComment;
import com.qiantang.smartparty.widget.commentwidget.KeyboardControlMnanager;

/**
 * Created by zhaoyong bai on 2018/5/28.
 * 人物表彰详情
 */
public class ParagonDetialActivity extends BaseBindActivity implements ViewPager.OnPageChangeListener {
    private ParagonDetialViewModel viewModel;
    private ActivityCharacterDetialBinding binding;
    private ViewParagonHeadBinding headBinding;
    private CommentAdapter adapter;
    private InputViewModel inputViewModel;

    @Override
    protected void initBind() {
        inputViewModel = new InputViewModel(this);
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new ParagonDetialViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detial);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_paragon_head, null, false);
        headBinding.setViewModel(viewModel);
        binding.input.setViewModel(inputViewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("先进典范");
        binding.toolbar.setResId(R.mipmap.icon_share_white);
        initRv(binding.rv);
        initBanner(headBinding.headBanner);
        setBanner(headBinding.headBanner);
        inputViewModel.setHint("发表学习感悟...");
        inputViewModel.setShareVis(false);
        initKeyboardHeightObserver();
        initRefresh(binding.cptr);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
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

    public void updateCollect(boolean isCollect) {
        inputViewModel.setIsCollect(isCollect);
    }


    private void setBanner(MyBanner headBanner) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headBanner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels * 0.677);
        headBanner.setLayoutParams(params);
        headBanner.setAutoPlayAble(false);
        headBanner.setAdapter(viewModel);
        headBanner.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_collect:
                viewModel.cancelPrase();
                break;
            case R.id.iv_uncollect:
                viewModel.prase();
                break;
            case R.id.tv_send:
                viewModel.comment(inputViewModel.getTextString());
                break;
        }
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
        viewModel.getData(1);
    }

    /**
     * 提交成功之后隐藏键盘
     */
    public void dissmissCommentBox() {
        closeInput();
        inputViewModel.setTextString("");
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewModel.setPicCount(position + 1 + "/" + viewModel.picListSize);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
