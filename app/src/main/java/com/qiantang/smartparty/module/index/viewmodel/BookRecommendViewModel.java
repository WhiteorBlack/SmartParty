package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxBookRecommend;
import com.qiantang.smartparty.module.index.adapter.BookRecommendAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class BookRecommendViewModel implements ViewModel {
    private BaseBindActivity activity;
    private BookRecommendAdapter recommendAdapter;
    private int pageNo = 1;

    public BookRecommendViewModel(BaseBindActivity activity, BookRecommendAdapter recommendAdapter) {
        this.activity = activity;
        this.recommendAdapter = recommendAdapter;
    }

    @Override
    public void destroy() {

    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().recommend(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxBookRecommend>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(List<RxBookRecommend> data) {
                        activity.refreshOK();
                        recommendAdapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adaptera, View view, int position) {
                ActivityUtil.startBookDetialActivity(activity, recommendAdapter.getData().get(position).getContentId());
            }
        };
    }
}
