package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class MsgViewModel implements ViewModel {
    private BaseBindActivity activity;
    private MsgAdapter adapter;
    private int pageNo = 1;

    public MsgViewModel(BaseBindActivity activity, MsgAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void loadMore() {
        pageNo++;
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().tzNotice(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxMsg>>() {
                    @Override
                    public void onSuccess(List<RxMsg> data) {
                        adapter.setPagingData(data, pageNo);
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
