package com.qiantang.smartparty.module.assistant.view;

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
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.databinding.ActivityCharacterDetialBinding;
import com.qiantang.smartparty.databinding.ViewCharacterdetialHeadBinding;
import com.qiantang.smartparty.databinding.ViewHeadBannerBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.CharacterDetialViewModel;
import com.qiantang.smartparty.module.input.viewmodel.InputViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
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
public class CharacterDetialActivity extends BaseBindActivity implements ViewPager.OnPageChangeListener {
    private CharacterDetialViewModel viewModel;
    private ActivityCharacterDetialBinding binding;
    private ViewCharacterdetialHeadBinding headBinding;
    private CommentAdapter adapter;
    private InputViewModel inputViewModel;

    @Override
    protected void initBind() {
        adapter = new CommentAdapter(R.layout.item_comment);
        viewModel = new CharacterDetialViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detial);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_characterdetial_head, null, false);
        headBinding.setViewModel(viewModel);
        inputViewModel = new InputViewModel(this);
        binding.input.setViewModel(inputViewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("人物表彰");
        binding.toolbar.setResId(R.mipmap.icon_share_white);
        initRv(binding.rv);
        initBanner(headBinding.headBanner);
        setBanner(headBinding.headBanner);
        initRefresh(binding.cptr);
        inputViewModel.setHint("发表学习感悟...");
        inputViewModel.setShareVis(false);
        inputViewModel.setIsPop(true);
        initKeyboardHeightObserver();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager.OnKeyboardStateChangeListener() {

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {

            }
        });
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

                break;
            case R.id.iv_uncollect:

                break;
            case R.id.tv_send:
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(this);
                    return;
                }
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(this);
                    return;
                }
                viewModel.comment(inputViewModel.getTextString());
                break;
        }
    }


    private void initRv(RecyclerView rv) {
        adapter.setEnableLoadMore(Config.isLoadMore);
        AutoUtils.auto(headBinding.getRoot());
        adapter.addHeaderView(headBinding.getRoot());
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        if (Config.isLoadMore) {
            adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        }
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
