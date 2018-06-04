package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.SimpleFragmentAdapter;
import com.qiantang.smartparty.databinding.ActivityRankBinding;
import com.qiantang.smartparty.module.index.fragment.RankFragment;
import com.qiantang.smartparty.module.index.viewmodel.RankViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 * 学习排行
 */
public class RankActivity extends BaseBindActivity {
    private RankViewModel viewModel;
    private ActivityRankBinding binding;
    private String[] titles = new String[]{"支部排行", "个人排行"};

    @Override
    protected void initBind() {
        viewModel = new RankViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rank);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.indicator.setupWithViewPager(binding.viewpager);
        binding.viewpager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager(), getFragments(), titles));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        Fragment fragment = new RankFragment();
        fragments.add(fragment);
        Fragment fragment1 = new RankFragment();
        fragments.add(fragment1);
        return fragments;
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
