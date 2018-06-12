package com.qiantang.smartparty.module.assistant.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxAdviseRecord;
import com.qiantang.smartparty.module.assistant.adapter.AdviseRecordAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class AdviseRecordViewModel implements ViewModel {
    private BaseBindActivity activity;
    private AdviseRecordAdapter adviseRecordAdapter;
    private int pageNo = 1;

    public AdviseRecordViewModel(BaseBindActivity activity, AdviseRecordAdapter adviseRecordAdapter) {
        this.activity = activity;
        this.adviseRecordAdapter = adviseRecordAdapter;
    }

    public void onLoadMore() {
        pageNo++;
        getData();
    }

    @Override
    public void destroy() {

    }

    public void getData() {
        ApiWrapper.getInstance().ideaList(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAdviseRecord>>() {
                    @Override
                    public void onSuccess(List<RxAdviseRecord> data) {
                        adviseRecordAdapter.setPagingData(data,pageNo);
                    }
                });
    }
}
