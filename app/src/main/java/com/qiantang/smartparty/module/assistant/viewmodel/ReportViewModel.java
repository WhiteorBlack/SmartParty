package com.qiantang.smartparty.module.assistant.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.w3c.dom.Text;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class ReportViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<String> title = new ObservableField<>();
    private ObservableField<String> content = new ObservableField<>();

    public ReportViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    public void publish() {
        if (TextUtils.isEmpty(title.get())) {
            ToastUtil.toast("请输入感想标题");
            return;
        }
        if (TextUtils.isEmpty(content.get())) {
            ToastUtil.toast("请输入感想内容");
            return;
        }
        ApiWrapper.getInstance().insertThinking(getTitle(), getContent())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ToastUtil.toast("感想发表成功");
                        Intent intent = new Intent();
                        intent.putExtra("id", data.getErrorMessage());
                        intent.putExtra("title", getTitle());
                        intent.putExtra("content", getContent());
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.onBackPressed();
                    }
                });
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    @Override
    public void destroy() {

    }
}
