package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.modle.RxPartyActivityDetial;
import com.qiantang.smartparty.modle.RxSignInfo;
import com.qiantang.smartparty.modle.RxStudyComment;
import com.qiantang.smartparty.modle.RxStudyZan;
import com.qiantang.smartparty.module.assistant.adapter.SignRecordAdapter;
import com.qiantang.smartparty.module.assistant.view.ActivityDetial;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.qiantang.smartparty.BR;
import com.trello.rxlifecycle2.android.FragmentEvent;

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
    private ObservableInt commentCount = new ObservableInt(0);
    private ObservableBoolean hasSign = new ObservableBoolean(false);
    private ObservableInt status = new ObservableInt(0);
    private SignRecordAdapter signRecordAdapter;
    private boolean isDealing = false;
    public ObservableBoolean isInit=new ObservableBoolean(false);
    public ObservableBoolean isApply=new ObservableBoolean(false);
    private int commentPos = 0;


    public ActivityDetialViewModel(BaseBindActivity activity, CommentAdapter adapter, SignRecordAdapter signRecordAdapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.signRecordAdapter = signRecordAdapter;
        initData();
    }

    private void initData() {
        id = activity.getIntent().getStringExtra("id");
        int status = activity.getIntent().getIntExtra("status", 0);
        setStatus(status);
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int pageNo) {
        this.pageNo = pageNo;
        ApiWrapper.getInstance().djActivityDetails(pageNo, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxActivityDetial>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        activity.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxActivityDetial data) {
                        activity.refreshOK();
                        ((ActivityDetial)activity).scorllTop();
                        if (Config.isLoadMore) {
                            adapter.setPagingData(data.getPl(), pageNo);
                        } else {
                            adapter.setNewData(data.getPl());
                        }
                        isApply.set(data.getIsApply()==1);
                        if (pageNo == 1) {
                            setCommentCount(data.getCount());
                            setDetials(data.getDetials());
                            dealSign(data.getQd());
                        }
                        isInit.set(true);
                    }
                });
    }

    private void dealSign(List<RxSignInfo> qd) {
        if (qd == null) {
            setHasSign(false);
            return;
        }
        setHasSign(qd.size() > 0);
        if (qd.size() > 9) {
            qd = qd.subList(0, 8);
            qd.add(new RxSignInfo());
        }
        signRecordAdapter.setNewData(qd);
    }

    public void comment(String content) {
        ApiWrapper.getInstance().comment(content, getDetials().getActivityId())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
//                        RxPartyActivityDetial detial = getDetials();
//                        setCommentCount(getCommentCount() + 1);
//                        setDetials(detial);
                        getData(pageNo + 1);
                        ((ActivityDetial) activity).dissmissCommentBox();
                    }
                });
    }

    public void enroll() {
        if (isApply.get()){
            return;
        }
        ApiWrapper.getInstance().enroll(getDetials().getActivityId())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ToastUtil.toast("报名成功");
                        setStatus(4);
//                        List<RxSignInfo> signInfos = signRecordAdapter.getData();
//                        if (signInfos.size() < 9) {
//                            RxSignInfo rxSignInfo = new RxSignInfo();
//                            rxSignInfo.setAvatar(MyApplication.mCache.getAsString(CacheKey.USER_AVATAR));
//                            rxSignInfo.setUserId(MyApplication.USER_ID);
//                            signInfos.add(signInfos.size(), rxSignInfo);
//                            setHasSign(true);
//                            signRecordAdapter.notifyItemInserted(signInfos.size());
//                        }
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
        sdv.setImageURI(Config.IMAGE_HOST + url);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adaptert, View view, int position) {
                commentPos = position;
                super.onItemChildClick(adaptert, view, position);
                switch (view.getId()) {
                    case R.id.iv_praise:
                        cancelLike(adapter.getData().get(position).getContentId());
                        break;
                    case R.id.iv_unpraise:
                        commentLike(adapter.getData().get(position).getContentId());
                        break;
                }
            }
        };
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }


    public RecyclerView.OnItemTouchListener onSignItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 9 && signRecordAdapter.getData().get(position).getAvatar() == null) {
                    //签到列表
                    ActivityUtil.startSignListActivity(activity, getDetials().getActivityId());
                }
            }
        };
    }

    @Bindable
    public int getCommentCount() {
        return commentCount.get();
    }

    public void setCommentCount(int commentCount) {
        this.commentCount.set(commentCount);
        notifyPropertyChanged(BR.commentCount);
    }

    @Bindable
    public int getStatus() {
        return status.get();
    }

    public void setStatus(int status) {
        this.status.set(status);
        notifyPropertyChanged(BR.status);
    }

    @Override
    public void destroy() {

    }
}
