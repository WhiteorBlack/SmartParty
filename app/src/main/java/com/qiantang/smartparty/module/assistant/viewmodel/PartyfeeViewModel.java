package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxPartyFee;
import com.qiantang.smartparty.module.assistant.adapter.PartyFeeAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class PartyfeeViewModel implements ViewModel {
    private BaseBindActivity activity;
    private PartyFeeAdapter adapter;
    private int pageNo = 1;

    public PartyfeeViewModel(BaseBindActivity activity, PartyFeeAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void loadMore() {
        pageNo++;
        testData();
    }

    public void testData() {
        ApiWrapper.getInstance().partyMoney()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxPartyFee>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxPartyFee> data) {
                        adapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adaptera, View view, int position) {
                if (adapter.getData().get(position).getType()==1){
                    ActivityUtil.startFeeDetialSpecialActivity(activity,adapter.getData().get(position).getDuesId());
                }else {

                    ActivityUtil.startFeeDetialActivity(activity, adapter.getData().get(position).getDuesId());
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
