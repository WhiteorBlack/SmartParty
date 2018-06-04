package com.qiantang.smartparty.module.mine.viewmodel;

import android.view.View;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class MineViewModel implements ViewModel {
    private BaseBindFragment fragment;

    public MineViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_info:
                //个人档案
                ActivityUtil.startInfoActivity(fragment.getActivity());
                break;
            case R.id.ll_month:
                //每月学习值
                ActivityUtil.startMonthScoreActivity(fragment.getActivity());
                break;
            case R.id.ll_total:
                //累计学习值
                ActivityUtil.startTotalScoreActivity(fragment.getActivity());
                break;
            case R.id.fl_collection:
                ActivityUtil.startMyCollectionActivity(fragment.getActivity());
                break;
            case R.id.fl_activity:
                ActivityUtil.startMyActivity(fragment.getActivity());
                break;
            case R.id.fl_test:

                break;
            case R.id.fl_about_us:
                ActivityUtil.startAboutUsActivity(fragment.getActivity());
                break;
            case R.id.fl_setting:
                //设置
                ActivityUtil.startSettingActivity(fragment.getActivity());
                break;
        }
    }

    @Override
    public void destroy() {

    }
}
