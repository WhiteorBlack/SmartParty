package com.qiantang.smartparty.module.mine.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxOnline;
import com.qiantang.smartparty.module.index.adapter.LearnAdapter;
import com.qiantang.smartparty.module.index.fragment.FragmentOnline;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class OnlineViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private LearnAdapter learnAdapter;
    private int pageNo = 1;
    private int classId = -1;

    public OnlineViewModel(BaseBindFragment fragment, LearnAdapter learnAdapter) {
        this.fragment = fragment;
        this.learnAdapter = learnAdapter;
        getData();
    }


    @Override
    public void destroy() {

    }

    public void loadMore() {
        pageNo++;
        getData();
    }

    private void getData() {
        ApiWrapper.getInstance().theoryList(pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxOnline>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        learnAdapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(RxOnline data) {
                        learnAdapter.setPagingData(data.getList(), pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtil.startHeadWebActivity(fragment.getActivity(), learnAdapter.getData().get(position).getContentId(), "理论在线", URLs.SPECIALORTHEORY);
            }
        };
    }
}
