package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxPartyActivityDetial;
import com.qiantang.smartparty.modle.RxSignInfo;
import com.qiantang.smartparty.module.assistant.adapter.SignRecordAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.qiantang.smartparty.BR;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class ActivityDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private CommentAdapter adapter;
    public ObservableBoolean isInput = new ObservableBoolean(false);
    private int pageNo = 1;
    private String id;
    private ObservableField<RxPartyActivityDetial> detials = new ObservableField<>();
    private ObservableBoolean hasSign = new ObservableBoolean(false);
    private SignRecordAdapter signRecordAdapter;


    public ActivityDetialViewModel(BaseBindActivity activity, CommentAdapter adapter, SignRecordAdapter signRecordAdapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.signRecordAdapter = signRecordAdapter;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
    }

    public void loadMore() {
        pageNo++;
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().djActivityDetails(pageNo, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxActivityDetial>() {
                    @Override
                    public void onSuccess(RxActivityDetial data) {
                        adapter.setPagingData(data.getPl(), pageNo);
                        if (pageNo == 1) {
                            setDetials(data.getDetials());
                            dealSign(data.getQd());
                        }
                    }
                });
    }

    private void dealSign(List<RxSignInfo> qd) {
        if (qd.size() > 9) {
            qd = qd.subList(0, 8);
            qd.add(new RxSignInfo());
        }
        signRecordAdapter.setNewData(qd);
    }

    @Bindable
    public boolean getHasSign() {
        return hasSign.get();
    }

    public void setHasSign(boolean hasSign) {
        this.hasSign.set(hasSign);
        notifyPropertyChanged(BR.hasSign);
    }

    @Bindable
    public RxPartyActivityDetial getDetials() {
        return detials.get();
    }

    public void setDetials(RxPartyActivityDetial detials) {
        this.detials.set(detials);
        notifyPropertyChanged(BR.detials);
    }

    @BindingAdapter("topPic")
    public static void topPic(SimpleDraweeView sdv, String url) {
        sdv.setImageURI(url);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml,String content){
        textViewForFullHtml.loadContent(content);
    }


    public RecyclerView.OnItemTouchListener onSignItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 9 && signRecordAdapter.getData().get(position).getAvatar() == null) {
                    //签到列表
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
