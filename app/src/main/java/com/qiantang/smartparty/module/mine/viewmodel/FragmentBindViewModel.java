package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.module.mine.view.ModifyPhoneActivity;

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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_sms:
                if (getIsCounting()) {
                    notifyPropertyChanged(BR.isCounting);
                    return;
                }
                getCode();
                break;
            case R.id.btn_confirm:
                ((ModifyPhoneActivity) fragment.getActivity()).nextStep();
                ForgetRequest.cancelCodeTime();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {

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
