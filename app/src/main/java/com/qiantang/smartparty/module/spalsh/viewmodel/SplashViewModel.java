package com.qiantang.smartparty.module.spalsh.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.modle.RxPersonalCenter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class SplashViewModel implements ViewModel {
    private BaseBindActivity activity;

    public SplashViewModel(BaseBindActivity activity) {
        this.activity = activity;
        getInfo();
    }

    @Override
    public void destroy() {

    }

    public void getInfo() {
        if (!MyApplication.isLogin()) {
            return;
        }
        ApiWrapper.getInstance().archives()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPersonalCenter>() {
                    @Override
                    public void onSuccess(RxPersonalCenter data) {
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo1 -> {
                            rxMyUserInfo1.setCounts(data.getCounts());
                            rxMyUserInfo1.setLearningability(data.getLearningability());
                            rxMyUserInfo1.setAvatar(data.getAvatar());
                            rxMyUserInfo1.setMemeber(data.getMember());
                            rxMyUserInfo1.setUsername(data.getUsername());
                            rxMyUserInfo1.setDeptId(data.getDeptId());
                            rxMyUserInfo1.setStatus(data.getSta());
                            MyApplication.mCache.saveInfo(rxMyUserInfo1, rxMyUserInfo1.getId());
                        });
                    }
                });
    }

    public void jumpNextPage() {
        ActivityUtil.startMainActivity(activity);
        activity.onBackPressed();
    }
}
