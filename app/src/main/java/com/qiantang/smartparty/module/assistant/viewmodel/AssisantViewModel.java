package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxAssientHome;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class AssisantViewModel implements ViewModel {
    private BaseBindFragment fragment;

    public AssisantViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
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
                ActivityUtil.jumpWeb(fragment.getActivity(), URLs.MESSAGE_DETIAL + ((RxMsg) adapter.getData().get(position)).getNoticeId());
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
                ActivityUtil.startMienDetialActivity(fragment.getActivity(), ((RxIndexCommon) adapter.getData().get(position)).getContentId());
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
                            RxMyUserInfo rxMyUserInfo = MyApplication.mCache.getAsJSONBean(MyApplication.USER_ID, RxMyUserInfo.class);
                            if (rxMyUserInfo.getMemeber() == 3) {
                                ActivityUtil.startApplyPartySuccessActivity(fragment.getActivity());
                            } else {
                                if (rxMyUserInfo.getStatus() == 0) {
                                    ActivityUtil.startApplyPartyActivity(fragment.getActivity());
                                }
                                if (rxMyUserInfo.getStatus() == 1) {
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

    }
}
