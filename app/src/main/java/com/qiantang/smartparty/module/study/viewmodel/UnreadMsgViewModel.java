package com.qiantang.smartparty.module.study.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxStudyUnreadMsg;
import com.qiantang.smartparty.module.study.adapter.UnreadAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class UnreadMsgViewModel implements ViewModel {
    private BaseBindActivity activity;
    private UnreadAdapter adapter;
    private int pageNo = 1;

    public UnreadMsgViewModel(BaseBindActivity activity, UnreadAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().getUnreadMsg()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxStudyUnreadMsg>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(List<RxStudyUnreadMsg> data) {
                        adapter.setPagingData(data, pageNo);
                        adapter.loadMoreEnd();
                        activity.refreshOK();
                    }
                });
    }

    @Override
    public void destroy() {

    }
}
