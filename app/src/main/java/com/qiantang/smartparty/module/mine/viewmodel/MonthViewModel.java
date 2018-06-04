package com.qiantang.smartparty.module.mine.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxMonthScore;
import com.qiantang.smartparty.module.mine.adapter.MonthScoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class MonthViewModel implements ViewModel {
    private BaseBindActivity activity;
    private MonthScoreAdapter adapter;

    public MonthViewModel(BaseBindActivity activity, MonthScoreAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void onLoadMore() {

    }

    public void testData() {
        List<RxMonthScore> monthScores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            monthScores.add(new RxMonthScore());
        }
        adapter.setNewData(monthScores);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @Override
    public void destroy() {

    }
}
