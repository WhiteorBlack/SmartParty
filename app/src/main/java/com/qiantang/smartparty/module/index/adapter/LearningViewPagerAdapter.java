package com.qiantang.smartparty.module.index.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qiantang.smartparty.modle.RxLearningClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/19.
 */

public class LearningViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<RxLearningClass> titles = new ArrayList<>();

    public LearningViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> fragments, List<RxLearningClass> titles) {
        this.titles = titles;
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getContent();
    }
}
