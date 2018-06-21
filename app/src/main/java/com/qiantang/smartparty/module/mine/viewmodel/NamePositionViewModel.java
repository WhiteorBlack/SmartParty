package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static android.app.Activity.RESULT_OK;

public class NamePositionViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    public ObservableField<String> info = new ObservableField<>();
    public ObservableBoolean checkSelection = new ObservableBoolean();

    public NamePositionViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initCache();
    }

    private void initCache() {
        String data = activity.getIntent().getStringExtra("info");
        if (!StringUtil.isEmpty(data)) {
            info.set(data);
            checkSelection.set(true);
        }
    }

    public ObservableBoolean getCheckSelection() {
        return checkSelection;
    }

    public void setCheckSelection(ObservableBoolean checkSelection) {
        this.checkSelection = checkSelection;
    }

    /**
     * 修改姓名
     */
    public void modifyName() {
        String realName = info.get();
        if (StringUtil.isEmpty(realName)) {
            ToastUtil.toast("姓名不能为空");
            return;
        }
        ApiWrapper.getInstance()
                .modifyArchives("", realName, "", "")
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(HttpResult bean) {
                        ToastUtil.toast("修改成功");
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> {
                            rxMyUserInfo.setUsername(realName);
                            MyApplication.mCache.put(CacheKey.USER_INFO, rxMyUserInfo);
                            activity.finish();
                        });

                    }
                });
    }

    /**
     * 修改职位
     */
    public void modifyNick() {
        String nickName = info.get();
        if (StringUtil.isEmpty(nickName)) {
            ToastUtil.toast("职位不能为空");
            return;
        }
        ApiWrapper.getInstance().modifyArchives("", "", nickName, "")
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(HttpResult bean) {
                        ToastUtil.toast("修改成功");
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> {
                            rxMyUserInfo.setPosition(nickName);
                            MyApplication.mCache.put(CacheKey.USER_INFO, rxMyUserInfo);
                            activity.finish();
                        });
                    }
                });
    }

    @Override
    public void destroy() {
    }
}
