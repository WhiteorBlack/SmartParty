package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class AboutUsViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<String> content = new ObservableField<>();

    public AboutUsViewModel(BaseBindActivity activity) {
        this.activity = activity;
        getData();
    }

    private void getData() {
        ApiWrapper.getInstance()
                .lookContent(2).compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        setContent(data);
                    }
                });
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml tvContent, String content) {
        tvContent.loadContent(content);
    }

    @Bindable
    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
        notifyPropertyChanged(BR.content);
    }

    @Override
    public void destroy() {

    }
}
