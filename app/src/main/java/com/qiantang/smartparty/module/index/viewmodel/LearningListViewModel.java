package com.qiantang.smartparty.module.index.viewmodel;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxLearningClass;
import com.qiantang.smartparty.module.index.adapter.LearningViewPagerAdapter;
import com.qiantang.smartparty.module.index.fragment.FragmentLearn;
import com.qiantang.smartparty.module.index.view.LearningListActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class LearningListViewModel implements ViewModel {
    private BaseBindActivity activity;
    private LearningViewPagerAdapter viewPagerAdapter;
    private int classId;

    public LearningListViewModel(BaseBindActivity activity, LearningViewPagerAdapter viewPagerAdapter) {
        this.activity = activity;
        this.viewPagerAdapter = viewPagerAdapter;
        initData();
    }

    private void initData() {
        classId = activity.getIntent().getIntExtra("id", -1);
    }

    @Override
    public void destroy() {

    }

    public void getData() {
        ApiWrapper.getInstance().specialClassify()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxLearningClass>>() {
                    @Override
                    public void onSuccess(List<RxLearningClass> data) {
                        if (data.size() > 0) {
                            viewPagerAdapter.setData(getFragments(data), data);
                            if (classId > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    if (classId == data.get(i).getClassifyId()) {
                                        ((LearningListActivity) activity).setPagerPos(i);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                });
    }

    private List<Fragment> getFragments(List<RxLearningClass> classList) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++) {

            Fragment fragment = new FragmentLearn();
            Bundle bundle = new Bundle();
            bundle.putInt("id", classList.get(i).getClassifyId());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        return fragments;
    }
}
