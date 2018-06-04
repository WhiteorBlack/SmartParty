package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.SimpleFragmentAdapter;
import com.qiantang.smartparty.databinding.ActivityMyCollectionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class MyCollectionActivity extends BaseBindActivity {
    private ActivityMyCollectionBinding binding;
    private String[] titles=new String[]{"视频学习","系列讲话","专题学习","理论在线","先进典范","好书推荐"};

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_collection);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的收藏");
        binding.toolbar.setIsHide(true);
        binding.tablayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager(),getFragments(),titles));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments=new ArrayList<>();
        return fragments;
    }

    @Override
    protected void viewModelDestroy() {

    }
}
