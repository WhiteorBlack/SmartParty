package com.qiantang.smartparty.module.main.viewmodel;

import android.Manifest;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.updata.NotificationDownloadCreator;
import com.qiantang.smartparty.config.updata.NotificationInstallCreator;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxAddScore;
import com.qiantang.smartparty.modle.RxIsApplyFor;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.modle.RxPersonalCenter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ACache;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class MainViewModel implements ViewModel {
    private BaseBindActivity activity;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MainViewModel(BaseBindActivity activity) {
        this.activity = activity;
        checkPermission();
//        versionCheck();
        getServicePhone();
        EventBus.getDefault().register(this);
    }

    private void getServicePhone() {
        ApiWrapper.getInstance().lookContent(3)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        MyApplication.mCache.put(CacheKey.SERVICE_PHONE, data);
                    }
                });
    }

    /**
     * 权限检测
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkPermission() {
        EasyPermission.with(activity)
                .rationale("需要读取您的内存卡")
                .addRequestCode(PermissionCode.RG_READ_EXTERNAL_STORAGE)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE).request();
    }

    /**
     * 版本检测
     * ps:采取延时检测提升用户体验
     */
    public void versionCheck() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (MyApplication.isInit) {
                        UpdateBuilder.create().strategy(new UpdateStrategy() {
                            @Override
                            public boolean isShowUpdateDialog(Update update) {
                                return true;
                            }

                            @Override
                            public boolean isAutoInstall() {
                                return true;
                            }

                            @Override
                            public boolean isShowDownloadDialog() {
                                return true;
                            }
                        })
                                .installDialogCreator(new NotificationInstallCreator())
                                .downloadDialogCreator(new NotificationDownloadCreator())
                                .check();

                    }
                });
    }

    public void getUserInfo() {
        ApiWrapper.getInstance().center()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPersonalCenter>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(RxPersonalCenter data) {

                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo1 -> {
                            rxMyUserInfo1.setCounts(data.getCounts());
                            rxMyUserInfo1.setLearningability(data.getLearningability());
                            rxMyUserInfo1.setAvatar(data.getAvatar());
                            rxMyUserInfo1.setMemeber(data.getMember());
                            rxMyUserInfo1.setStatus(data.getSta());
                            MyApplication.mCache.saveInfo(rxMyUserInfo1, rxMyUserInfo1.getId());
                        });

                    }
                });
    }

    public void isApplyFor() {
        ApiWrapper.getInstance().isApplyFor()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxIsApplyFor>() {
                    @Override
                    public void onSuccess(RxIsApplyFor data) {
                        MyApplication.mCache.put(CacheKey.IS_APPLY_FOR, data.getApplyStatus() + "");
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxAddScore rxAddScore) {
        if (rxAddScore != null) {
            addScore(rxAddScore.getType(), rxAddScore.getTime(), rxAddScore.getId());
        }
    }

    /**
     * 添加学习值
     *
     * @param type
     * @param time
     * @param id
     */
    public void addScore(int type, int time, String id) {
        ApiWrapper.getInstance().addLearningability(type, time, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        Logger.e("addScore" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        Logger.e("addScore" + data.getErrorMessage());
                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
