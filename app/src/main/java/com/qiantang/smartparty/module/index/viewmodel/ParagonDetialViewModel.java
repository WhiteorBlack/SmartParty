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
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxParagonDetial;
import com.qiantang.smartparty.modle.RxParagonInfo;
import com.qiantang.smartparty.modle.RxPicUrl;
import com.qiantang.smartparty.module.index.view.ParagonDetialActivity;
import com.qiantang.smartparty.module.index.view.VoiceSpeechDetialActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.qiantang.smartparty.widget.MyBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class ParagonDetialViewModel extends BaseObservable implements ViewModel, BGABanner.Delegate<View, RxPicUrl>, BGABanner.Adapter {
    private BaseBindActivity activity;
    private CommentAdapter adapter;
    public ObservableBoolean isInput = new ObservableBoolean(false);
    private int pageNo = 1;
    private String id;
    private ObservableField<RxParagonInfo> detials = new ObservableField<>();
    private ObservableField<List<RxPicUrl>> picList = new ObservableField<>();
    private boolean isDealing = false;
    private int commentPos = 0;
    private String printurl;
    private ObservableField<String> picCount = new ObservableField<>();
    public int picListSize = 0;


    public ParagonDetialViewModel(BaseBindActivity activity, CommentAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
        printurl = activity.getIntent().getStringExtra("printurl");
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().paragonDetails(pageNo, id, printurl)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxParagonDetial>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreEnd();
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxParagonDetial data) {
                        adapter.setPagingData(data.getComment(), pageNo);
                        activity.refreshOK();
                        ((ParagonDetialActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                        setDetials(data.getDetail());
                        if (pageNo == 1 && picListSize == 0) {
                            picListSize = data.getImgSrc().size();
                            setPicList(data.getImgSrc());
                            setPicCount("1/" + picListSize);
                        }
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
//                        RxParagonInfo detial = getDetials();
//                        detial.setCommentSum(detial.getCommentSum() + 1);
//                        setDetials(detial);
                        getData(pageNo + 1);
                        ((ParagonDetialActivity) activity).dissmissCommentBox();
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
        ApiWrapper.getInstance().collectSave(id, 5)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ((ParagonDetialActivity) activity).updateCollect(true);
                    }
                });
    }

    public void cancelPrase() {
        ApiWrapper.getInstance().collectAbolish(id, 5)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        ((ParagonDetialActivity) activity).updateCollect(false);
                    }
                });
    }

    @Bindable
    public List<RxPicUrl> getPicList() {
        return picList.get();
    }

    public void setPicList(List<RxPicUrl> picList) {
        this.picList.set(picList);
        notifyPropertyChanged(BR.picList);
    }

    @Bindable
    public String getPicCount() {
        return picCount.get();
    }

    public void setPicCount(String picCount) {
        this.picCount.set(picCount);
        notifyPropertyChanged(BR.picCount);
    }

    @Bindable
    public RxParagonInfo getDetials() {
        return detials.get();
    }

    public void setDetials(RxParagonInfo detials) {
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

    @BindingAdapter("headBanner")
    public static void setBanner(MyBanner banner, List<RxPicUrl> list) {
        List<String> tips = new ArrayList<>();
        if (list == null || list.size() < 1) {
            return;
        }

        banner.setData(R.layout.viewpager_img, list, null);
    }

    @Override
    public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
        ((SimpleDraweeView) itemView).setImageURI(Config.IMAGE_HOST + ((RxPicUrl) model).getImg_src());
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxPicUrl model, int position) {

    }
}
