package com.qiantang.smartparty.module.search.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.adapter.NewsAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/7/10.
 */
public class FcNoticeViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private int pageNo = 1;
    private int type;
    private String keyword;
    private IndexCommonAdapter adapter;

    public FcNoticeViewModel(BaseBindFragment fragment, int type) {
        this.fragment = fragment;
        this.type = type;
        EventBus.getDefault().register(this);
    }

    public void onLoadMore() {
        pageNo++;
        getData(pageNo, keyword);
    }

    public void setAdapter(IndexCommonAdapter adapter){
        this.adapter=adapter;
    }

    public void getData(int pageNo, String keyword) {
        this.pageNo = pageNo;
        this.keyword = keyword;
        ApiWrapper.getInstance().fcNotice(pageNo, type, keyword)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxIndexCommon>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                    }

                    @Override
                    public void onSuccess(List<RxIndexCommon> data) {
                        adapter.setPagingData(data, pageNo);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adaptera, View view, int position) {
                RxIndexCommon rxIndexCommon = adapter.getData().get(position);
                switch (type) {
                    case Event.SEARCH_CHARACTER:
                        ActivityUtil.startCharacterDetialActivity(fragment.getActivity(), rxIndexCommon.getContentId(), rxIndexCommon.getPrinturl());
                        break;
                    case Event.SEARCH_MEETING:
                        ActivityUtil.startHeadWebActivity(fragment.getActivity(), rxIndexCommon.getContentId(), "会议纪要", URLs.NOTICE_DETIAL, 0, rxIndexCommon.getImgSrc());
                        break;
                    case Event.SEARCH_NEWS:
                        ActivityUtil.startHeadWebActivity(fragment.getActivity(), rxIndexCommon.getContentId(),
                                "新闻快报", URLs.NOTICE_DETIAL, 0, rxIndexCommon.getImgSrc());
                        break;
                    case Event.SEARCH_MIEN:
                        ActivityUtil.startHeadWebActivity(fragment.getActivity(), rxIndexCommon.getContentId(), "党建风采", URLs.NOTICE_DETIAL, 0, rxIndexCommon.getImgSrc());
                        break;
                    case Event.SEARCH_STUDY_STATE:
                        ActivityUtil.startHeadWebActivity(fragment.getActivity(), rxIndexCommon.getContentId(),
                                "学习动态", URLs.NOTICE_DETIAL, 0, rxIndexCommon.getImgSrc());
                        break;
                }
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        getData(1, keyword);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
