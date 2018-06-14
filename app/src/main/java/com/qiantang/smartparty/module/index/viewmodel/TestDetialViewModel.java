package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxTestDetial;
import com.qiantang.smartparty.module.index.adapter.TestDetialAdapter;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private TestDetialAdapter detialAdapter;
    private ObservableField<String> countTime = new ObservableField<>();
    private int timeSeconde = 0;
    private int questionCount = 0;
    private String id;

    public TestDetialViewModel(BaseBindActivity activity, TestDetialAdapter detialAdapter) {
        this.activity = activity;
        this.detialAdapter = detialAdapter;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
        timeSeconde = activity.getIntent().getIntExtra("time", 0);
        questionCount = activity.getIntent().getIntExtra("count", 0);
        getData();
    }

    private void getData() {
        ApiWrapper.getInstance().questionnairecheck(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxTestDetial>>() {
                    @Override
                    public void onSuccess(List<RxTestDetial> data) {
                        detialAdapter.setNewData(data);
                    }
                });
    }

    @Bindable
    public String getCountTime() {
        return countTime.get();
    }

    public void setCountTime(String countTime) {
        this.countTime.set(countTime);
    }

    @Override
    public void destroy() {

    }
}
