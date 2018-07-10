package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.modle.RxSetting;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.SharedPreferences;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.WebUtil;
import com.qiantang.smartparty.widget.dialog.DefaultDialog;
import com.qiantang.smartparty.widget.dialog.OnDialogExecuteListener;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.qiantang.smartparty.BR;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SettingViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private DefaultDialog dialog;
    public ObservableField<String> servicePhone = new ObservableField<>();
    public ObservableField<RxSetting> rxSetting = new ObservableField<>();
    public ObservableField<String> bindPhone = new ObservableField<>();
    public ObservableField<String> version = new ObservableField<>();
    public ObservableBoolean msgNotify = new ObservableBoolean(false);
    private UMShareAPI mShareAPI;

    public SettingViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
        EventBus.getDefault().register(this);
    }

    public void getInstall() {
        ApiWrapper.getInstance().install()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxSetting>() {
                    @Override
                    public void onSuccess(RxSetting data) {
                        setRxSetting(data);
                    }
                });
    }

    private void initData() {
        String phone = MyApplication.mCache.getAsString(CacheKey.SERVICE_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            servicePhone.set(phone);
        }
        String bindphone = MyApplication.mCache.getAsString(CacheKey.PHONE);
        bindPhone.set(bindphone);
        version.set(AppUtil.getVerName(activity));
        msgNotify.set(SharedPreferences.getInstance().getBoolean(CacheKey.MSG_NOTIFY, true));
    }

    /**
     * 退出登录
     */
    public void logOut() {
        PushAgent.getInstance(activity).deleteAlias(MyApplication.USER_ID, "party", (b, s) -> {

        });
        MyApplication.mCache.remove(CacheKey.USER_ID);
        MyApplication.mCache.remove(CacheKey.INFO);
        MyApplication.mCache.remove(CacheKey.USER_INFO);
        MyApplication.mCache.remove(CacheKey.PHONE);
        MyApplication.USER_ID = "";
        System.gc();
        WebUtil.removeCookie();
        EventBus.getDefault().post(Event.RELOAD);
        EventBus.getDefault().post(Event.LOGOUT);
        MyApplication.isLoginOB.set(false);

    }

    @Bindable
    public RxSetting getRxSetting() {
        return rxSetting.get();
    }

    public void setRxSetting(RxSetting rxSetting) {
        this.rxSetting.set(rxSetting);
        notifyPropertyChanged(BR.rxSetting);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public void logout() {
        if (dialog == null) {
            dialog = new DefaultDialog(activity, "是否退出登录？", new OnDialogExecuteListener() {
                @Override
                public void execute() {
                    exit();
                }

                @Override
                public void cancel() {

                }
            });
        }
        dialog.show();
    }

    private void exit() {
        ApiWrapper.getInstance().exit()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        logOut();
                        activity.onBackPressed();
                    }
                });
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
                bindPhone(data.get("openid"), 2);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtil.toast("已取消登录");
        }
    };


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
        } else {
            ToastUtil.toast("授权失败！");
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
                        bindPhone(data.getOpenid(), 1);
                    }
                });
    }

    public void bindPhone(String openId, int type) {

        ApiWrapper.getInstance().band("", MyApplication.mCache.getAsString(CacheKey.PHONE), openId, type)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(RxMyUserInfo data) {
                        ToastUtil.toast("绑定成功");
                        RxSetting rxSetting = getRxSetting();
                        if (type == 1) {
                            rxSetting.setWx(openId);
                        } else {
                            rxSetting.setQq(openId);
                        }
                        setRxSetting(rxSetting);
                    }
                });
    }


}
