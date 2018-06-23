package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxSign;
import com.qiantang.smartparty.modle.RxSignResult;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.qiantang.smartparty.BR;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class SignViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private String result = "";
    private ObservableField<RxSign> sign = new ObservableField<>();
    public ObservableBoolean isSuccess = new ObservableBoolean(false);

    public SignViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
    }

    private void initData() {
        result = activity.getIntent().getStringExtra("result");
        getSignInfo();
    }

    private void getSignInfo() {
        ApiWrapper.getInstance().sign(result)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxSign>() {
                    @Override
                    public void onSuccess(RxSign data) {
                        setSign(data);
                    }
                });
    }

    public void setLocation(String location) {
        RxSign rxSign = getSign();
        rxSign.setPoint(location);
        setSign(rxSign);
    }

    public void sign() {
        if (TextUtils.isEmpty(getSign().getPoint())) {
            ToastUtil.toast("正在定位中,请稍定");
            return;
        }
        ApiWrapper.getInstance().signDetails(result, getSign().getPoint())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxSignResult>() {
                    @Override
                    public void onSuccess(RxSignResult data) {
                        RxSign rxSign = getSign();
                        rxSign.setTime(data.getTime());
                        setSign(rxSign);
                        isSuccess.set(true);
                    }
                });
    }

    @Bindable
    public RxSign getSign() {
        return sign.get();
    }

    public void setSign(RxSign sign) {
        this.sign.set(sign);
        notifyPropertyChanged(BR.sign);
    }

    @Override
    public void destroy() {

    }
}
