package com.qiantang.smartparty.module.push.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseNotifyBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxTestInfo;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestInfoNotifyViewModel extends BaseObservable implements ViewModel {
    private BaseNotifyBindActivity activity;
    private ObservableField<RxTestInfo> testInfo = new ObservableField<>();

    public TestInfoNotifyViewModel(BaseNotifyBindActivity activity) {
        this.activity = activity;
    }

    public void getData(String id) {
        ApiWrapper.getInstance().questionnaireDetails(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxTestInfo>() {
                    @Override
                    public void onSuccess(RxTestInfo data) {
                        setTestInfo(data);
                    }
                });
    }

    @Bindable
    public RxTestInfo getTestInfo() {
        return testInfo.get();
    }

    public void setTestInfo(RxTestInfo testInfo) {
        this.testInfo.set(testInfo);
        notifyPropertyChanged(BR.testInfo);
    }

    @Override
    public void destroy() {

    }
}
