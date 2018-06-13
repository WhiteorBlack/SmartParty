package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityLearningListBinding;
import com.qiantang.smartparty.module.index.adapter.LearningViewPagerAdapter;
import com.qiantang.smartparty.module.index.viewmodel.LearningListViewModel;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class LearningListActivity extends BaseBindActivity {
    private ActivityLearningListBinding binding;
    private LearningViewPagerAdapter viewPagerAdapter;
    private LearningListViewModel viewModel;

    @Override
    protected void initBind() {
        viewPagerAdapter = new LearningViewPagerAdapter(getSupportFragmentManager());
        viewModel = new LearningListViewModel(this, viewPagerAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_learning_list);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("专题学习");
        binding.toolbar.setResId(R.mipmap.icon_search_black);
        binding.tablayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setAdapter(viewPagerAdapter);
        viewModel.getData();
    }

    public void setPagerPos(int pos) {
        binding.viewpager.setCurrentItem(pos);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
