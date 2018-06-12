package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class AdviseViewModel implements ViewModel {
    private BaseBindActivity activity;
    public ObservableField<String> content = new ObservableField<>();

    public AdviseViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    public void publish() {
        if (TextUtils.isEmpty(content.get())) {
            ToastUtil.toast("请输入要反馈的内容");
            return;
        }
        ApiWrapper.getInstance().insertIdea(content.get())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtil.toast("您反馈的问题已提交");
                        activity.onBackPressed();
                    }
                });
    }

    @Override
    public void destroy() {

    }
}
