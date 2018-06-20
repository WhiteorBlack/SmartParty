package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxPartyFeeDetial;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private String id;
    private ObservableField<RxPartyFeeDetial> feeDetial = new ObservableField<>();

    public FeeDetialViewModel(BaseBindActivity activity) {
        this.activity = activity;
        id = activity.getIntent().getStringExtra("id");
        getDetial();
    }

    private void getDetial() {
        ApiWrapper.getInstance().partyMoneyDetails(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPartyFeeDetial>() {
                    @Override
                    public void onSuccess(RxPartyFeeDetial data) {
                        setFeeDetial(data);
                    }
                });
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml,String content){
        textViewForFullHtml.loadContent(content);
    }

    @Bindable
    public RxPartyFeeDetial getFeeDetial() {
        return feeDetial.get();
    }

    public void setFeeDetial(RxPartyFeeDetial feeDetial) {
        this.feeDetial.set(feeDetial);
        notifyPropertyChanged(BR.feeDetial);
    }

    @Override
    public void destroy() {

    }
}
