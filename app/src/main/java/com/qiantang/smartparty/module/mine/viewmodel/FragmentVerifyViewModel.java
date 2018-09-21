package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.module.mine.view.ModifyPhoneActivity;
import com.qiantang.smartparty.module.mine.view.ModifyPwdActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class FragmentVerifyViewModel extends BaseObservable implements ViewModel {
    private BaseBindFragment fragment;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();

    public FragmentVerifyViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        String phone = MyApplication.mCache.getAsString(CacheKey.PHONE);
        this.phone.set(phone);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForgetRequest.SmsTime smsTime) {
        int time = smsTime.getTime();
        if (time == 60) {
            setIsCounting(false);
            setMsg("重新发送");
        } else {
            setIsCounting(true);
            setMsg(time + "秒后重发");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_sms:
                fragment.closeInput();
                if (getIsCounting()) {
                    notifyPropertyChanged(BR.isCounting);
                    return;
                }
                getCode();
                break;
            case R.id.btn_confirm:
                verifyCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        ApiWrapper.getInstance().modifyPhone(phone.get())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast("发送失败");
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ToastUtil.toast("发送成功");
                        ForgetRequest.smsCodeTime();
                        ForgetRequest.isStart = true;
                        isCounting.set(true);
                    }
                });
    }

    public void verifyCode() {
        if (TextUtils.isEmpty(getSmsCode())) {
            ToastUtil.toast("请输入收到的验证码");
            return;
        }
        ApiWrapper.getInstance().modifyPhoneAfter(phone.get(), getSmsCode())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast("验证码输入有误");
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        try {
                            ((ModifyPhoneActivity) fragment.getActivity()).nextStep();
                        } catch (Exception e) {
                            ((ModifyPwdActivity) fragment.getActivity()).nextStep();
                        }
                        ForgetRequest.cancelCodeTime();
                    }
                });
    }

    @Bindable
    public boolean getIsCounting() {
        return isCounting.get();
    }

    public void setIsCounting(boolean isCounting) {
        this.isCounting.set(isCounting);
        notifyPropertyChanged(BR.isCounting);
    }

    @Bindable
    public String getMsg() {
        return msg.get();
    }

    public void setMsg(String msg) {
        this.msg.set(msg);
        notifyPropertyChanged(BR.msg);
    }

    @Bindable
    public String getSmsCode() {
        return smsCode.get();
    }

    public void setSmsCode(String smsCode) {
        this.smsCode.set(smsCode);
        notifyPropertyChanged(BR.smsCode);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
