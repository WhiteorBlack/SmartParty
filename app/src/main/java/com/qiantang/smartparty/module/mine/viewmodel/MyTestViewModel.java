package com.qiantang.smartparty.module.mine.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxTest;
import com.qiantang.smartparty.module.index.adapter.TestListAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class MyTestViewModel implements ViewModel {
    private BaseBindActivity activity;
    private TestListAdapter listAdapter;
    private int pageNo = 1;

    public MyTestViewModel(BaseBindActivity activity, TestListAdapter listAdapter) {
        this.activity = activity;
        this.listAdapter = listAdapter;
    }

    @Override
    public void destroy() {

    }

    public void loadMore() {
        pageNo++;
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().myTest(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxTest>>() {
                    @Override
                    public void onSuccess(List<RxTest> data) {
                        listAdapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.tv_check:
                        ActivityUtil.startTestDoneInfoActivity(activity, listAdapter.getData().get(position).getUserquestionnaire_id());
                        break;
                    case R.id.btn_test:
                        ActivityUtil.startTestInfoActivity(activity, listAdapter.getData().get(position).getQuestionnaire_id());
                        break;
                }
            }
        };
    }
}
