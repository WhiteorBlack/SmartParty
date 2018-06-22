package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.module.mine.view.ModifyPhoneActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ACache;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class FragmentBindViewModel extends BaseObservable implements ViewModel {
    private BaseBindFragment fragment;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();

    public FragmentBindViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;

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
                if (TextUtils.isEmpty(phone.get())) {
                    ToastUtil.toast("请输入要绑定的手机号");
                    return;
                }
                getCode();
                break;
            case R.id.btn_confirm:
                bindPhone();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        EventBus.getDefault().register(this);
        ApiWrapper.getInstance().bindPhone(phone.get())
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

    private void bindPhone() {
        if (TextUtils.isEmpty(getSmsCode())){
            ToastUtil.toast("请输入收到的验证码");
            return;
        }
        String oldPHone = MyApplication.mCache.getAsString(CacheKey.PHONE);
        ApiWrapper.getInstance().bindPhone(phone.get(), getSmsCode(), oldPHone)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast("绑定失败请重试");
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ToastUtil.toast(data.getErrorMessage());
                        ForgetRequest.cancelCodeTime();
                        MyApplication.mCache.put(CacheKey.PHONE, phone.get());
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, new ACache.CacheResultListener<RxMyUserInfo>() {
                            @Override
                            public void onResult(RxMyUserInfo rxMyUserInfo) {
                                rxMyUserInfo.setPhone(phone.get());
                                MyApplication.mCache.put(CacheKey.USER_INFO, rxMyUserInfo);
                                ((ModifyPhoneActivity) fragment.getActivity()).onBackPressed();
                            }
                        });
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
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String msg) {
        this.phone.set(msg);
        notifyPropertyChanged(BR.phone);
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
