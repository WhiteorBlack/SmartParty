package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.utils.WebUtil;
import com.qiantang.smartparty.widget.dialog.DefaultDialog;
import com.qiantang.smartparty.widget.dialog.OnDialogExecuteListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SettingViewModel implements ViewModel {
    private BaseBindActivity activity;
    private DefaultDialog dialog;
    public ObservableField<String> servicePhone=new ObservableField<>();
    public SettingViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
    }

    private void initData() {
        String phone=MyApplication.mCache.getAsString(CacheKey.SERVICE_PHONE);
        if (!TextUtils.isEmpty(phone)){
            servicePhone.set(phone);
        }
    }

    /**
     * 退出登录
     */
    public static void logOut() {
        MyApplication.mCache.remove(CacheKey.USER_ID);
        MyApplication.mCache.remove(CacheKey.INFO);
        MyApplication.mCache.remove(CacheKey.USER_INFO);
        MyApplication.mCache.remove(CacheKey.PHONE);
        MyApplication.TOKEN = "";
        MyApplication.USER_ID = "";
        System.gc();
        WebUtil.removeCookie();
        EventBus.getDefault().post(Event.RELOAD);
        EventBus.getDefault().post(Event.LOGOUT);
        MyApplication.isLoginOB.set(false);
    }

    @Override
    public void destroy() {

    }

    public void logout() {
        if (dialog == null) {
            dialog = new DefaultDialog(activity, "是否退出登录？", new OnDialogExecuteListener() {
                @Override
                public void execute() {
                    logOut();
                }

                @Override
                public void cancel() {

                }
            });
        }
        dialog.show();
    }
}
