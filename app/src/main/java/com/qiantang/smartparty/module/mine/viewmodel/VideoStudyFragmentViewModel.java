package com.qiantang.smartparty.module.mine.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.module.index.adapter.VideoStudyAdapter;
import com.qiantang.smartparty.module.mine.adapter.VideoStudyFragmentAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class VideoStudyFragmentViewModel implements ViewModel {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private VideoStudyFragmentAdapter fragmentAdapter;
    private int pageNo = 1;

    public VideoStudyFragmentViewModel(BaseBindFragment activity, VideoStudyFragmentAdapter adapter) {
        this.fragment = activity;
        this.fragmentAdapter = adapter;
        this.activity = (BaseBindActivity) fragment.getActivity();
    }

    public void onLoadMore() {
        pageNo++;
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().videoUserList(pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxVideoStudy>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        fragmentAdapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxVideoStudy> data) {
                        fragmentAdapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterQ, View view, int position) {
                RxVideoStudy rxVideoStudy = fragmentAdapter.getData().get(position);
                ActivityUtil.startVideoDetialActivity(activity, rxVideoStudy.getVideourl(), rxVideoStudy.getTitle(), rxVideoStudy.getVideo_id());
            }

        };
    }

    @Override
    public void destroy() {

    }
}
