package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.adapter.VideoStudyAdapter;
import com.qiantang.smartparty.module.mine.adapter.VideoStudyFragmentAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class VideoStudyViewModel implements ViewModel {
    private BaseBindActivity activity;
    private VideoStudyAdapter adapter;
    private BaseBindFragment fragment;
    private VideoStudyFragmentAdapter fragmentAdapter;
    private int pageNo = 1;

    public VideoStudyViewModel(BaseBindActivity activity, VideoStudyAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public VideoStudyViewModel(BaseBindFragment activity, VideoStudyFragmentAdapter adapter) {
        this.fragment = activity;
        this.fragmentAdapter = adapter;
        this.activity = (BaseBindActivity) fragment.getActivity();
    }

    public void onLoadMore() {
        pageNo++;
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().videoList(pageNo)
                .compose(fragment == null ? activity.bindUntilEvent(ActivityEvent.DESTROY) : fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxVideoStudy>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (fragmentAdapter != null) {
                            fragmentAdapter.loadMoreEnd();
                            return;
                        }
                        adapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxVideoStudy> data) {
                        if (fragmentAdapter != null) {
                            fragmentAdapter.setPagingData(data, pageNo);
                            return;
                        }
                        adapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterQ, View view, int position) {
                if (fragmentAdapter != null) {
                    RxVideoStudy rxVideoStudy = fragmentAdapter.getData().get(position);
                    ActivityUtil.startVideoDetialActivity(activity, rxVideoStudy.getVideourl(), rxVideoStudy.getTitle(), rxVideoStudy.getVideo_id());
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter qadapter, View view, int position) {
                super.onItemChildClick(qadapter, view, position);
                RxVideoStudy rxVideoStudy;
                if (fragmentAdapter != null) {
                    rxVideoStudy = fragmentAdapter.getData().get(position);
                } else {
                    rxVideoStudy = adapter.getData().get(position);
                }
                switch (view.getId()) {
                    case R.id.tv_name:
                        ActivityUtil.startVideoDetialActivity(activity, rxVideoStudy.getVideourl(), rxVideoStudy.getTitle(), rxVideoStudy.getVideo_id());
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
