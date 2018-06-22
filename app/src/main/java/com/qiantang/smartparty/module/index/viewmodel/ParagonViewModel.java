package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class ParagonViewModel implements ViewModel {
    private BaseBindActivity activity;
    private IndexCommonAdapter adapter;
    private int pageNo = 1;

    public ParagonViewModel(BaseBindActivity activity, IndexCommonAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void destroy() {

    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterq, View view, int position) {
                RxIndexCommon rxIndexCommon = adapter.getData().get(position);
                ActivityUtil.startParagonDetialActivity(activity, rxIndexCommon.getContentId(), rxIndexCommon.getPrinturl());
            }
        };
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().paragonList(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxIndexCommon>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(List<RxIndexCommon> data) {
                        activity.refreshOK();
                        adapter.setPagingData(data, pageNo);
                    }
                });
    }
}
