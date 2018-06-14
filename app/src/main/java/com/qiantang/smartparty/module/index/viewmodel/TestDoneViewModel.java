package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxTestDoneInfo;
import com.qiantang.smartparty.modle.RxTestInfo;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDoneViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private String id;
    private ObservableField<RxTestDoneInfo> testInfo = new ObservableField<>();

    public TestDoneViewModel(BaseBindActivity activity) {
        this.activity = activity;
        id = activity.getIntent().getStringExtra("id");
    }

    public void getData() {
        ApiWrapper.getInstance().questionnaireDoneDetails(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxTestDoneInfo>() {
                    @Override
                    public void onSuccess(RxTestDoneInfo data) {
                        setTestInfo(data);
                    }
                });
    }

    @Bindable
    public RxTestDoneInfo getTestInfo() {
        return testInfo.get();
    }

    public void setTestInfo(RxTestDoneInfo testInfo) {
        this.testInfo.set(testInfo);
        notifyPropertyChanged(BR.testInfo);
    }

    @Override
    public void destroy() {

    }
}
