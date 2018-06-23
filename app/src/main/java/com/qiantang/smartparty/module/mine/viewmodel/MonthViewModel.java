package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.ObservableInt;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMonthScore;
import com.qiantang.smartparty.module.mine.adapter.MonthScoreAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class MonthViewModel implements ViewModel {
    private BaseBindActivity activity;
    private MonthScoreAdapter adapter;
    private int pageNo = 1;
    private String date;
    public ObservableInt socre = new ObservableInt(0);

    public MonthViewModel(BaseBindActivity activity, MonthScoreAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        initDate();
    }

    private void initDate() {
        date = activity.getIntent().getStringExtra("date");
        if (TextUtils.isEmpty(date)) {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            String monthString;
            if (month < 10) {
                monthString = "0" + month;
            } else {
                monthString = "" + month;
            }
            date = calendar.get(Calendar.YEAR) + monthString;
        }
    }

    public void onLoadMore() {
        pageNo++;
        testData();
    }

    public void testData() {
        ApiWrapper.getInstance().learnMonths(pageNo, date)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxMonthScore>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxMonthScore> data) {
                        adapter.setPagingData(data, pageNo);
                        if (data.size() > 0) {
                            socre.set(data.get(0).getSocre());
                        }
                    }
                });
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
