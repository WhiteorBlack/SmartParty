package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxThinkDetial;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class ThinkDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<RxThinkDetial> detial = new ObservableField<>();
    private String id;

    public ThinkDetialViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
    }

    public void getData() {
        ApiWrapper.getInstance().thinkingDetails(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxThinkDetial>() {
                    @Override
                    public void onSuccess(RxThinkDetial data) {
                        setDetial(data);
                    }
                });
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }

    @Bindable
    public RxThinkDetial getDetial() {
        return detial.get();
    }

    public void setDetial(RxThinkDetial detial) {
        this.detial.set(detial);
        notifyPropertyChanged(BR.detial);
    }

    @Override
    public void destroy() {

    }
}
