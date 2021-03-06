package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxAssientHome;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.module.assistant.view.AssisantFragment;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class AssisantViewModel implements ViewModel {
    private BaseBindFragment fragment;

    public AssisantViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        EventBus.getDefault().register(this);
    }

    public List<RxIndexClass> getClassData() {
        List<RxIndexClass> classes = new ArrayList<>();
        classes.add(new RxIndexClass("党费缴纳", R.mipmap.icon_party_fee, 0));
        classes.add(new RxIndexClass("入党申请", R.mipmap.icon_apply_party, 1));
        classes.add(new RxIndexClass("思想汇报", R.mipmap.icon_maind_report, 2));
        classes.add(new RxIndexClass("组织架构", R.mipmap.icon_nation, 3));
        classes.add(new RxIndexClass("党建活动", R.mipmap.icon_party_activity, 4));
        classes.add(new RxIndexClass("会议纪要", R.mipmap.icon_meeting_record, 5));
        classes.add(new RxIndexClass("人物表彰", R.mipmap.icon_praise_man, 6));
        classes.add(new RxIndexClass("意见反馈", R.mipmap.icon_advice, 7));
        return classes;
    }

    //接收更新请求
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD_STUDY) {
            ((AssisantFragment) fragment).refreshData();
        }
    }

    public void getListData(MsgAdapter msgAdapter, ActivityAdapter activityAdapter, IndexCommonAdapter indexCommonAdapter) {
        ApiWrapper.getInstance().assientHome()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxAssientHome>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        fragment.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxAssientHome data) {
                        fragment.refreshOK();
                        msgAdapter.setNewData(data.getTz());
                        activityAdapter.setNewData(data.getDj());
                        indexCommonAdapter.setNewData(data.getFc());
                    }
                });
    }


    /**
     * 通知公告点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener msgToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(fragment.getActivity());
                    return;
                }
                if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
                    ToastUtil.toast("仅内部人员可查看");
                    return;
                }
                if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
                    ToastUtil.toast("您尚未通过审核，请耐心等待");
//                    return;
                }
                ActivityUtil.jumpWeb(fragment.getActivity(), URLs.MESSAGE_DETIAL + ((RxMsg) adapter.getData().get(position)).getNoticeId(), "公告详情");
            }
        };
    }

    /**
     * 党建风采点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener stateToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ActivityUtil.startMienDetialActivity(fragment.getActivity(), ((RxIndexCommon) adapter.getData().get(position)).getContentId());
                ActivityUtil.startHeadWebActivity(fragment.getActivity(), ((RxIndexCommon) adapter.getData().get(position)).getContentId(), "党建风采", URLs.NOTICE_DETIAL, 0, ((RxIndexCommon) adapter.getData().get(position)).getImgSrc());
            }
        };
    }


    /**
     * 党建活动点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener activityToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(fragment.getActivity());
                    return;
                }
                if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
                    ToastUtil.toast("仅内部人员可查看");
                    return;
                }
                if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
                    ToastUtil.toast("您尚未通过审核，请耐心等待");
                    return;
                }
                RxActivity rxActivity = (RxActivity) adapter.getData().get(position);
                ActivityUtil.startActivityDetialActivity(fragment.getActivity(), rxActivity.getActivityId(), rxActivity.getStatus());
            }
        };
    }

    /**
     * 分类模块点击事件
     *
     * @return
     */
    public RecyclerView.OnItemTouchListener classToucnListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int pos = ((RxIndexClass) adapter.getData().get(position)).pos;
                switch (pos) {
                    case 0:
                        ActivityUtil.startPartyFeeActivity(fragment.getActivity());
                        break;
                    case 1:
                        if (MyApplication.isLogin()) {
                            String startue = MyApplication.mCache.getAsString(CacheKey.IS_APPLY_FOR);
                            if (TextUtils.equals(startue, "2")) {
                                ActivityUtil.startApplyPartySuccessActivity(fragment.getActivity());
                            } else {
                                if (TextUtils.equals(startue, "0")) {
                                    ActivityUtil.startApplyPartyActivity(fragment.getActivity());
                                }
                                if (TextUtils.equals(startue, "1")) {
                                    ActivityUtil.startApplyPartyDeitalActivity(fragment.getActivity());
                                }
                            }
                        } else {
                            ActivityUtil.startLoginActivity(fragment.getActivity());
                        }

                        break;
                    case 2:
                        ActivityUtil.startThinkingActivity(fragment.getActivity());
                        break;
                    case 3:
                        ActivityUtil.startStructureActivity(fragment.getActivity());
                        break;
                    case 4:
                        ActivityUtil.startPartyActivity(fragment.getActivity());
                        break;
                    case 5:
                        ActivityUtil.startMeetingActivity(fragment.getActivity());
                        break;
                    case 6:
                        ActivityUtil.startCharacterActivity(fragment.getActivity());
                        break;
                    case 7:
                        ActivityUtil.startAdviseActivity(fragment.getActivity());
                        break;

                }
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_news_more:
                ActivityUtil.startMsgActivity(fragment.getActivity());
                break;
            case R.id.tv_study_state_more:
                //党建活动
                ActivityUtil.startPartyActivity(fragment.getActivity());
                break;
            case R.id.tv_study_video:
                //党建风采
                ActivityUtil.startMienActivity(fragment.getActivity());
                break;
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
