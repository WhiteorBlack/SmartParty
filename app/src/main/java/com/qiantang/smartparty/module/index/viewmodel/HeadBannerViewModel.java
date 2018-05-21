package com.qiantang.smartparty.module.index.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxAds;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.widget.MyBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by 123 on 2017/9/5.
 */

public class HeadBannerViewModel extends BaseObservable implements ViewModel, BGABanner.Delegate<View, RxAds>, BGABanner.Adapter {
    private Activity activity;
    private BaseBindActivity bindActivity;
    private BaseBindFragment fragment;
    public List<RxAds> bannerList = new ArrayList<>();
    private static String dataCode;

    public HeadBannerViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public HeadBannerViewModel(BaseBindActivity activity) {
        this.bindActivity = activity;
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
        if (TextUtils.equals(dataCode, "classfy")) {
            ((SimpleDraweeView) itemView).setImageURI(AppUtil.getResUri(((RxAds) model).getResId()));
        } else {
            ((SimpleDraweeView) itemView).setImageURI(((RxAds) model).getPicUrl());
        }
    }

    @BindingAdapter("headBanner")
    public static void setBanner(MyBanner banner, List<RxAds> list) {
        List<String> tips = new ArrayList<>();
            if (list == null || list.size() < 1) {
                return;
            }


            for (RxAds ads : list) {
                tips.add(ads.getText());
            }


        banner.setData(R.layout.viewpager_img, list, tips);
    }

    /**
     * 获取轮播图数据
     */
    public void getBannerData(String channelCode) {
        dataCode = channelCode;

    }

    @Bindable
    public List<RxAds> getHeadBanner() {
        return bannerList;
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxAds model, int position) {

    }
}
