package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentIndexBinding;
import com.qiantang.smartparty.module.index.adapter.ClassAdapter;
import com.qiantang.smartparty.module.index.adapter.NewsAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.viewmodel.HeadBannerViewModel;
import com.qiantang.smartparty.module.index.viewmodel.IndexViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.widget.MyBanner;
import com.qiantang.smartparty.widget.SpaceItemDecoration;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class IndexFragment extends BaseBindFragment {
    private FragmentIndexBinding binding;
    private IndexViewModel viewModel;
    private HeadBannerViewModel headBannerViewModel;
    private NewsAdapter newsAdapter;
    private IndexCommonAdapter studyStateAdapter, studyVideoAdapter, speechAdapter, studyProAdapter, rulesAdapter;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        newsAdapter = new NewsAdapter(null);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_index, container, false);
        viewModel = new IndexViewModel(this, newsAdapter);
        binding.setViewModel(viewModel);
        headBannerViewModel = new HeadBannerViewModel(this);
        binding.headBanner.setViewModel(headBannerViewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setIsHide(false);
        binding.toolbar.setTitle("首页");
        binding.toolbar.setResId(R.mipmap.icon_scan);
        binding.toolbar.ivScan.setOnClickListener(this::onClick);
        initBanner(binding.headBanner.headBanner);
        initBannerSize(binding.headBanner.headBanner);
        initClassRv(binding.rvClass);
        initNewsRv(binding.rvNews);
        initStudyState(binding.rvStudyState);
        initStudyVideo(binding.rvStudyVideo);
        initSpeech(binding.rvSpeech);
        initStudyPro(binding.rvStudyPractice);
        initRules(binding.rvRules);
    }

    /**
     * 党章党规
     *
     * @param rvRules
     */
    private void initRules(RecyclerView rvRules) {
        rulesAdapter = new IndexCommonAdapter(R.layout.item_index_rules);
        rvRules.setNestedScrollingEnabled(false);
        rvRules.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvRules.addOnItemTouchListener(viewModel.rulesToucnListener());
        rvRules.setAdapter(rulesAdapter);
    }

    /**
     * 两学一做
     *
     * @param rvStudyPractice
     */
    private void initStudyPro(RecyclerView rvStudyPractice) {
        studyProAdapter = new IndexCommonAdapter(R.layout.item_index_rules);
        rvStudyPractice.setNestedScrollingEnabled(false);
        rvStudyPractice.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvStudyPractice.addItemDecoration(new SpaceItemDecoration(0, 0, 12, 0));
        rvStudyPractice.setAdapter(studyProAdapter);
        rvStudyPractice.addOnItemTouchListener(viewModel.studyProToucnListener());
    }

    /**
     * 系列讲话
     *
     * @param rvSpeech
     */
    private void initSpeech(RecyclerView rvSpeech) {
        speechAdapter = new IndexCommonAdapter(R.layout.item_study_state);
        rvSpeech.setNestedScrollingEnabled(false);
        rvSpeech.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSpeech.setAdapter(speechAdapter);
        rvSpeech.addOnItemTouchListener(viewModel.speechToucnListener());
    }

    /**
     * 学习视频
     *
     * @param rvStudyVideo
     */
    private void initStudyVideo(RecyclerView rvStudyVideo) {
        studyVideoAdapter = new IndexCommonAdapter(R.layout.item_index_rules);
        rvStudyVideo.setNestedScrollingEnabled(false);
        rvStudyVideo.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvStudyVideo.setAdapter(studyVideoAdapter);
        rvStudyVideo.addItemDecoration(new SpaceItemDecoration(0, 0, 12, 0));
        rvStudyVideo.addOnItemTouchListener(viewModel.studyVideoToucnListener());
    }

    /**
     * 学习动态
     *
     * @param rvStudyState
     */
    private void initStudyState(RecyclerView rvStudyState) {
        studyStateAdapter = new IndexCommonAdapter(R.layout.item_study_state);
        rvStudyState.setNestedScrollingEnabled(false);
        rvStudyState.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudyState.setAdapter(studyStateAdapter);
        rvStudyState.addOnItemTouchListener(viewModel.studyStateToucnListener());
    }

    /**
     * 新闻快报
     *
     * @param rvNews
     */
    private void initNewsRv(RecyclerView rvNews) {
        rvNews.setNestedScrollingEnabled(false);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.addOnItemTouchListener(viewModel.newsToucnListener());
        rvNews.setAdapter(newsAdapter);
        viewModel.testNew();
    }

    /**
     * 顶部类别
     *
     * @param rvClass
     */
    private void initClassRv(RecyclerView rvClass) {
        rvClass.setNestedScrollingEnabled(false);
        rvClass.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvClass.addItemDecoration(new SpaceItemDecoration(0, 0, 12, 0));
        rvClass.setAdapter(new ClassAdapter(R.layout.item_index_class, viewModel.getClassData()));
        rvClass.addOnItemTouchListener(viewModel.classToucnListener());
    }

    private void initBannerSize(MyBanner headBanner) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headBanner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels * 0.48);
        headBanner.setLayoutParams(params);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                ActivityUtil.startQRActivity(getActivity());
//                ActivityUtil.startSignActivity(getActivity());
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
        headBannerViewModel.destroy();
    }
}
