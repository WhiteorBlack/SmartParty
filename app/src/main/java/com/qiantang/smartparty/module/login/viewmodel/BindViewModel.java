package com.qiantang.smartparty.module.login.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.android.databinding.library.baseAdapters.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.modle.RxPersonalCenter;
import com.qiantang.smartparty.module.login.view.BindPhoneActivity;
import com.qiantang.smartparty.module.mine.viewmodel.ForgetRequest;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.qiantang.smartparty.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2018/7/6.
 */
public class BindViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableField<String> account = new ObservableField<>("");
    public ObservableField<String> smsCode = new ObservableField<>("");
    public ObservableField<String> pwd = new ObservableField<>("");
    public ObservableBoolean isAgree = new ObservableBoolean(false);
    private String id;
    private int type;

    public BindViewModel(BaseBindActivity bindPhoneActivity) {
        this.activity = bindPhoneActivity;
        EventBus.getDefault().register(this);
        id = activity.getIntent().getStringExtra("id");
        type = activity.getIntent().getIntExtra("type", 0);
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

    public void sendMsg() {
        if (getIsCounting()) {
            return;
        }
        checkMobile(getAccount());
    }

    /**
     * 验证手机号码
     */
    public void checkMobile(String account) {
        boolean accountEmpty = StringUtil.isEmpty(account);
        if (accountEmpty) {
            ToastUtil.toast("账号不能为空");
            setIsCounting(false);
        } else if (!StringUtil.isPhoneNumberValid(account)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
            setIsCounting(false);
        } else {
            getCode(account);
        }
    }

    private void getCode(String account) {
        ApiWrapper.getInstance().center(account)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPersonalCenter>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);

                        ToastUtil.toast("该手机号尚未注册");
                    }

                    @Override
                    public void onSuccess(RxPersonalCenter data) {
                        ForgetRequest.smsCodeTime();
                        ForgetRequest.isStart = true;
                        isCounting.set(true);
                        getSms(account);
                    }
                });
    }

    private void getSms(String phone) {
        ApiWrapper.getInstance().modifyPhone(phone)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {

                    }
                });
    }

    public void bindPhone() {
        if (TextUtils.isEmpty(getSmsCode())){
            ToastUtil.toast("请输入收到的验证码");
            return;
        }
        ApiWrapper.getInstance().band(getSmsCode(), getAccount(), id, type)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo data) {
                        loginSuccess(data);
                    }
                });
    }

    private void loginSuccess(RxMyUserInfo data) {
        EventBus.getDefault().post(Event.RELOAD_STUDY);
        MyApplication.mCache.saveInfo(data, data.getId());
        activity.onBackPressed();
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
    public String getAccount() {
        return account.get();
    }

    public void setAccount(String account) {
        this.account.set(account);
        notifyPropertyChanged(BR.account);
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
