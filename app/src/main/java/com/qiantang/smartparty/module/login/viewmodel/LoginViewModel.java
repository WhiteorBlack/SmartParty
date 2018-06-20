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
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.module.mine.viewmodel.ForgetRequest;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import static com.qiantang.smartparty.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class LoginViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> smsCode = new ObservableField<>();
    private ObservableBoolean loginType = new ObservableBoolean(true);
    private ObservableField<String> password = new ObservableField<>();

    public LoginViewModel(BaseBindActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
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

    public void pwdLogin() {
        setLoginType(true);
        setIsCounting(false);
        setMsg("获取验证码");
        setSmsCode("");
    }

    public void codeLogin() {
        setLoginType(false);
        setPassword("");
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
            ToastUtil.toast("手机号不能为空");
            setIsCounting(false);
        } else if (!StringUtil.isPhoneNumberValid(account)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
            setIsCounting(false);
        } else {
            getCode(account);
        }
    }

    private void getCode(String account) {

        ApiWrapper.getInstance().loginCode(account)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (e.getCode() == 3) {
                            ToastUtil.toast("改手机号还未注册");
                        }
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        if (data.getStatus() == 0) {
                            ForgetRequest.smsCodeTime();
                            ForgetRequest.isStart = true;
                            isCounting.set(true);
                        }

                    }
                });
    }

    public void getLogin() {
        if (TextUtils.isEmpty(account.get())) {
            ToastUtil.toast("手机号不能为空");
            setIsCounting(false);
        } else if (!StringUtil.isPhoneNumberValid(account.get())) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
            setIsCounting(false);
        }  else {
            if (getLoginType()) {
                if (TextUtils.isEmpty(getPassword())) {
                    ToastUtil.toast("请输入登录密码");
                    return;
                }
                loginPassword(getAccount(), getPassword());
            } else {
                if (TextUtils.isEmpty(smsCode.get())) {
                    ToastUtil.toast("请输入收到的验证码");
                    return;
                }
                login(account.get(), smsCode.get());
            }
        }
    }

    private void login(String phone, String code) {
        ApiWrapper.getInstance().login(phone, code)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (e.getCode() == 1) {
                            ToastUtil.toast("验证码过期，请重新获取");
                        }
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo data) {
                        loginSuccess(data);
                    }
                });
    }

    private void loginSuccess(RxMyUserInfo data) {
        MyApplication.mCache.saveInfo(data, data.getUserId());
        activity.onBackPressed();
    }

    private void loginPassword(String phone, String code) {
        ApiWrapper.getInstance().passwordLogin(phone, code)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (e.getCode() == 1) {
                        }
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo data) {
                        loginSuccess(data);
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

    @Bindable
    public boolean getLoginType() {
        return loginType.get();
    }

    public void setLoginType(boolean loginType) {
        this.loginType.set(loginType);
        notifyPropertyChanged(BR.loginType);
    }

    @Bindable
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        notifyPropertyChanged(BR.password);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
