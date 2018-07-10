package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.SuperKotlin.pictureviewer.PictureConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxPartyFeeDetial;
import com.qiantang.smartparty.module.photo.view.CheckPhotoActivity;
import com.qiantang.smartparty.module.study.adapter.StudyImageAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeDetialSpecialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private String id;
    private ObservableField<RxPartyFeeDetial> feeDetial = new ObservableField<>();
    private StudyImageAdapter adapter;

    public FeeDetialSpecialViewModel(BaseBindActivity activity, StudyImageAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        id = activity.getIntent().getStringExtra("id");
        getDetial();
    }

    private void getDetial() {
        ApiWrapper.getInstance().partyMoneyDetails(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPartyFeeDetial>() {
                    @Override
                    public void onSuccess(RxPartyFeeDetial data) {
                        setFeeDetial(data);
                        dealImg(data.getImgSrc());
                    }
                });
    }

    private void dealImg(String imgSrc) {
        if (TextUtils.isEmpty(imgSrc)) {
            return;
        }
        String[] img = imgSrc.split(",");
        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            imgList.add(img[i]);
        }
        adapter.setNewData(imgList);
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }

    @Bindable
    public RxPartyFeeDetial getFeeDetial() {
        return feeDetial.get();
    }

    public void setFeeDetial(RxPartyFeeDetial feeDetial) {
        this.feeDetial.set(feeDetial);
        notifyPropertyChanged(BR.feeDetial);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adaptera, View view, int position) {
                PictureConfig config = new PictureConfig.Builder()
                        .setListData((ArrayList<String>) adapter.getData())    //图片数据List<String> list
                        .setPosition(position)    //图片下标（从第position张图片开始浏览）
                        .setDownloadPath("smartParty")    //图片下载文件夹地址
                        .setIsShowNumber(true)//是否显示数字下标
                        .needDownload(true)    //是否支持图片下载
                        .setPlacrHolder(R.mipmap.ic_launcher)    //占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                        .build();
                CheckPhotoActivity.startActivity(activity, config);
            }
        };
    }

    @Override
    public void destroy() {

    }
}
