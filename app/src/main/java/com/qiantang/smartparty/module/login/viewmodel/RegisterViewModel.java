package com.qiantang.smartparty.module.login.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.module.mine.viewmodel.ForgetRequest;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import static com.qiantang.smartparty.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class RegisterViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> smsCode = new ObservableField<>();
    private HashMap<String, String> map;


    public RegisterViewModel(BaseBindActivity activity) {
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
            if (map == null) {
                map = new HashMap<>();
            }
            if (StringUtil.isEmpty(map.get(account))) {
                ToastUtil.toast("发送成功");
                ForgetRequest.smsCodeTime();
                ForgetRequest.isStart = true;
                isCounting.set(true);
            } else {
                setIsCounting(false);
                ToastUtil.toast("该手机号码已经被注册");
            }
        }
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
