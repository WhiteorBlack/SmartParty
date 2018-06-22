package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxApplyDetial;
import com.qiantang.smartparty.module.study.adapter.StudyImageAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class ApplyDetailViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<RxApplyDetial> applyDetial = new ObservableField<>();
    private static StudyImageAdapter adapter;


    public ApplyDetailViewModel(BaseBindActivity activity, StudyImageAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        getData();
    }

    private void getData() {
        ApiWrapper.getInstance().applyDetial()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxApplyDetial>() {
                    @Override
                    public void onSuccess(RxApplyDetial data) {
                        setApplyDetial(data);
                    }
                });
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }

    @BindingAdapter("image")
    public static void image(RecyclerView rv,String images) {
        List<String> imgList = new ArrayList<>();
        if (!TextUtils.isEmpty(images)) {
            String[] imageStrings = images.split(",");
            for (int i = 0; i < imageStrings.length; i++) {
                imgList.add(imageStrings[i]);
            }
            adapter.setNewData(imgList);
        }
    }

    @Bindable
    public RxApplyDetial getApplyDetial() {
        return applyDetial.get();
    }

    public void setApplyDetial(RxApplyDetial applyDetial) {
        this.applyDetial.set(applyDetial);
        notifyPropertyChanged(BR.applyDetial);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @Override
    public void destroy() {

    }
}
