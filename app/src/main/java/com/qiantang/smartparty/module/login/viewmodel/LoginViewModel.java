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
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxAuthorizeUserInfo;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.module.mine.viewmodel.ForgetRequest;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

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
    public UMShareAPI mShareAPI;
    private final LoadingWindow loadingWindow;

    public LoginViewModel(BaseBindActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
        loadingWindow = new LoadingWindow(activity);
    }

    /**
     * 微信授权
     */
    public void authWechat() {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(activity);
        }
        if (mShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            IWXAPI api = WXAPIFactory.createWXAPI(activity, Config.WX_APP_ID);
            api.sendReq(req);
            loadingWindow.showWindow();
        } else {
            ToastUtil.toast("请安装微信客户端");
        }
    }

    /**
     * QQ授权
     */
    public void authQQ() {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(activity);
        }
        if (mShareAPI.isInstall(activity, SHARE_MEDIA.QQ)) {
            loadingWindow.showWindow();
            mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtil.toast("请安装QQ客户端");
        }
    }

    /**
     * 授权回调
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (platform == SHARE_MEDIA.QQ) {
                Map<String, String> params = data;
                qqAuthLogin(data.get("openid"), 2);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            loadingWindow.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            loadingWindow.dismiss();
            ToastUtil.toast("已取消登录");
        }
    };


    /**
     * qq授权登录请求
     */
    private void qqAuthLogin(String id, int type) {
        ApiWrapper.getInstance().thirdLogin(id, type)
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.toast("未绑定手机号");
                        ActivityUtil.startBindPhoneActivity(activity, type, id);
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo bean) {
                        loginSuccess(bean);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForgetRequest.SmsTime smsTime) {
        if (!getIsCounting()){
            return;
        }
        int time = smsTime.getTime();
        if (time == 60) {
            setIsCounting(false);
            setMsg("重新发送");
        } else {
            setIsCounting(true);
            setMsg(time + "秒后重发");
        }
    }

    /**
     * 微信授权回调
     *
     * @param resp
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendAuth.Resp resp) {
        int errCode = resp.errCode;
        if (errCode == BaseResp.ErrCode.ERR_OK) {
            String code = resp.code;
            getWXOpenId(code);
            loadingWindow.showWindow();
        } else {
            ToastUtil.toast("授权失败！");
            loadingWindow.hidWindow();
        }
    }

    private void getWXOpenId(String code) {
        ApiWrapper.getInstance().wxToken(code)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        qqAuthLogin(data.getOpenid(), 1);
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer integer) {
        if (integer == Event.RELOAD_STUDY||integer==Event.RELOAD) {
            activity.onBackPressed();
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
                        ToastUtil.toast(e.getMessage());
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
        } else {
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
                        ToastUtil.toast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo data) {
                        loginSuccess(data);
                    }
                });
    }

    private void loginSuccess(RxMyUserInfo data) {
        MyApplication.mCache.saveInfo(data, data.getId());
        EventBus.getDefault().post(Event.RELOAD_STUDY);
        PushAgent.getInstance(activity).setAlias(data.getId(), "party", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {

            }
        });
        activity.onBackPressed();
    }

    private void loginPassword(String phone, String code) {
        ApiWrapper.getInstance().passwordLogin(phone, code)
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
