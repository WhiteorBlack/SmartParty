package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxIndexClass;
import com.qiantang.smartparty.modle.RxIndexNews;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.utils.ActivityUtil;

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
        classes.add(new RxIndexClass("意见反馈", R.mipmap.icon_maind_report, 7));
        return classes;
    }

    public void getMsgData(MsgAdapter adapter) {
        List<RxMsg> msgs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            msgs.add(new RxMsg());
        }
        adapter.setNewData(msgs);
    }

    public void getActivityData(ActivityAdapter adapter) {
        List<RxActivity> msgs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            msgs.add(new RxActivity());
        }
        adapter.setNewData(msgs);
    }

    public void getStateData(IndexCommonAdapter adapter) {
        List<RxVideoStudy> msgs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            msgs.add(new RxVideoStudy());
        }
        adapter.setNewData(msgs);
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
                ActivityUtil.startActivityDetialActivity(fragment.getActivity(), "");
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
                        ActivityUtil.startApplyPartyActivity(fragment.getActivity());
                        ActivityUtil.startApplyPartyDeitalActivity(fragment.getActivity());
                        ActivityUtil.startApplyPartySuccessActivity(fragment.getActivity());
                        break;
                    case 2:
                        ActivityUtil.startReportActivity(fragment.getActivity());
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

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

        }
    }

    @Override
    public void destroy() {

    }
}
