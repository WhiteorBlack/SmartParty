package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.utils.ACache;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class InfoViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<RxMyUserInfo> userInfo = new ObservableField<>();

    public InfoViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
    }

    public void initData() {
        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> setUserInfo(rxMyUserInfo));
    }


    @BindingAdapter("avatar")
    public static void avatar(SimpleDraweeView sdv, String avatar) {
        sdv.setImageURI(Config.IMAGE_HOST + avatar);
    }


    @Bindable
    public RxMyUserInfo getUserInfo() {
        return userInfo.get();
    }

    public void setUserInfo(RxMyUserInfo userInfo) {
        this.userInfo.set(userInfo);
        notifyPropertyChanged(BR.userInfo);
    }

    @Override
    public void destroy() {
    }
}
