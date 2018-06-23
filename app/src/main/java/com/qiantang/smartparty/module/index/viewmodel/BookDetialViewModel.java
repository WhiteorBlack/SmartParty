package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxAddScore;
import com.qiantang.smartparty.modle.RxBookDetial;
import com.qiantang.smartparty.modle.RxBookInfo;
import com.qiantang.smartparty.modle.RxParagonDetial;
import com.qiantang.smartparty.modle.RxParagonInfo;
import com.qiantang.smartparty.modle.RxPicUrl;
import com.qiantang.smartparty.module.index.view.BookDetialActivity;
import com.qiantang.smartparty.module.index.view.ParagonDetialActivity;
import com.qiantang.smartparty.module.index.view.VoiceSpeechDetialActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.qiantang.smartparty.widget.MyBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class BookDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private CommentAdapter adapter;
    public ObservableBoolean isInput = new ObservableBoolean(false);
    private int pageNo = 1;
    private String id;
    private ObservableField<RxBookInfo> detials = new ObservableField<>();
    private boolean isDealing = false;
    private int commentPos = 0;

    public BookDetialViewModel(BaseBindActivity activity, CommentAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().bookDetails(pageNo, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxBookDetial>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxBookDetial data) {
                        activity.refreshOK();
                        ((BookDetialActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                        if (Config.isLoadMore) {
                            adapter.setPagingData(data.getComment(), pageNo);
                        } else {
                            adapter.setNewData(data.getComment());
                        }
                        setDetials(data.getDetail());
                    }
                });
    }


    public void comment(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.toast("请输入评论内容");
            return;
        }
        isDealing = true;
        ApiWrapper.getInstance().comment(content, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
//                        RxBookInfo detial = getDetials();
//                        detial.setCommentSum(detial.getCommentSum() + 1);
//                        setDetials(detial);
                        EventBus.getDefault().post(new RxAddScore(CacheKey.COMMENT, 0, id));
                        getData(pageNo + 1);
                        ((BookDetialActivity) activity).dissmissCommentBox();
                    }
                });
    }

    public void commentLike(String id) {
        isDealing = true;
        ApiWrapper.getInstance().videoLike(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        adapter.getData().get(commentPos).setIsDz(1);
                        adapter.getData().get(commentPos).setDz(adapter.getData().get(commentPos).getDz() + 1);
                        adapter.notifyItemChanged(commentPos + 1);
                    }
                });
    }


    private void cancelLike(String id) {
        isDealing = true;
        ApiWrapper.getInstance().removeVideoLike(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        //取消点赞成功
                        adapter.getData().get(commentPos).setIsDz(0);
                        adapter.getData().get(commentPos).setDz(adapter.getData().get(commentPos).getDz() - 1);
                        adapter.notifyItemChanged(commentPos + 1);
                    }
                });
    }

    /**
     * 收藏
     */
    public void prase() {
        ApiWrapper.getInstance().collectSave(id, 6)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ((BookDetialActivity) activity).updateCollect(true);
                    }
                });
    }

    public void cancelPrase() {
        ApiWrapper.getInstance().collectAbolish(id, 6)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        ((BookDetialActivity) activity).updateCollect(false);
                    }
                });
    }

    @Bindable
    public RxBookInfo getDetials() {
        return detials.get();
    }

    public void setDetials(RxBookInfo detials) {
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

            @Override
            public void onItemChildClick(BaseQuickAdapter adaptert, View view, int position) {
                super.onItemChildClick(adaptert, view, position);
                if (isDealing) {
                    return;
                }
                commentPos = position;

                switch (view.getId()) {
                    case R.id.iv_praise:
                        cancelLike(adapter.getData().get(position).getComment_id());
                        break;
                    case R.id.iv_unpraise:
                        commentLike(adapter.getData().get(position).getComment_id());
                        break;
                }
            }
        };
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }


    @Override
    public void destroy() {

    }

}
