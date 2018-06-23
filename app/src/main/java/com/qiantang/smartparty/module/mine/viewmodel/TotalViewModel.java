package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.ObservableInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMonthScore;
import com.qiantang.smartparty.modle.RxTotalScore;
import com.qiantang.smartparty.module.mine.adapter.TotalScoreAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class TotalViewModel implements ViewModel {
    private BaseBindActivity activity;
    private TotalScoreAdapter adapter;
    private int pageNo = 1;
    public ObservableInt score = new ObservableInt(0);

    public TotalViewModel(BaseBindActivity activity, TotalScoreAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void onLoadMore() {
        pageNo++;
        testData();
    }

    public void testData() {
        ApiWrapper.getInstance().learningability(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxTotalScore>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxTotalScore> data) {
                        adapter.setPagingData(data, pageNo);
                        if (data.size() > 0) {
                            score.set(data.get(0).getCount());
                        }
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adaptera, View view, int position) {
                ActivityUtil.startMonthScoreActivity(activity, adapter.getData().get(position).getMonths());
            }
        };
    }

    @Override
    public void destroy() {

    }
}
