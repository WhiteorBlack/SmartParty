package com.qiantang.smartparty.module.index.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.modle.RxSpeechDetial;
import com.qiantang.smartparty.modle.RxSpeechInfo;
import com.qiantang.smartparty.modle.RxVideoDetial;
import com.qiantang.smartparty.modle.RxVideoInfo;
import com.qiantang.smartparty.module.index.adapter.VideoDetialAdapter;
import com.qiantang.smartparty.module.index.view.VideoSpeechDetialActivity;
import com.qiantang.smartparty.module.index.view.VideoStudyDetialActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class VideoSpeechDetialViewMdoel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private CommentAdapter adapter;
    private String id;
    private int pageNo = 1;
    private ObservableField<RxSpeechInfo> videoInfo = new ObservableField<>();
    private boolean isDealing = false;
    private int addCommentCount = 0;
    private int commentCount = 0;
    private int commentPos = 0;

    public VideoSpeechDetialViewMdoel(BaseBindActivity activity, CommentAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void initData() {
        Intent intent = activity.getIntent();
        id = intent.getStringExtra("id");
    }

    public void testData() {
        ApiWrapper.getInstance().speechDetial(pageNo, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxSpeechDetial>() {
                    @Override
                    public void onSuccess(RxSpeechDetial data) {
                        adapter.setPagingData(data.getComment(), pageNo);
                        if (pageNo == 1) {
                            setVideoInfo(data.getVideo());
                            commentCount = data.getVideo().getReview();
                            ((VideoSpeechDetialActivity) activity).startVideo(data.getVideo().getSpeakurl(), data.getVideo().getTitle());
                            ((VideoSpeechDetialActivity) activity).updateCollect(data.getVideo().getCollect() != 0);
                        }
                    }
                });
    }

    /**
     * 评论
     *
     * @param content
     */
    public void comment(String content) {
        isDealing = true;
        ApiWrapper.getInstance().commentVideo(content, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        addCommentCount++;
                        RxSpeechInfo rxVideoInfo = getVideoInfo();
                        commentCount += 1;
                        rxVideoInfo.setReview(commentCount);
                        RxComment rxComment = new RxComment();
                        rxComment.setUsername(MyApplication.mCache.getAsString(CacheKey.USER_NAME));
                        rxComment.setUserId(MyApplication.mCache.getAsString(CacheKey.USER_ID));
                        rxComment.setContent(content);
                        rxComment.setAvatar(MyApplication.mCache.getAsString(CacheKey.USER_AVATAR));
                        rxComment.setIsDz(0);
                        rxComment.setDz(0);
                        rxComment.setCreationtime(AppUtil.getNowDate());
                        adapter.getData().add(adapter.getData().size(), rxComment);
                        adapter.notifyItemInserted(adapter.getData().size());
                    }
                });
    }

    /**
     * 评论点赞
     *
     * @param id
     */
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

    /**
     * 取消点赞
     *
     * @param id
     */
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
     * 点赞
     */
    public void prase() {
        ApiWrapper.getInstance().collectSave(id, 2)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ((VideoStudyDetialActivity) activity).updateCollect(true);
                    }
                });
    }

    public void cancelPrase() {
        ApiWrapper.getInstance().collectAbolish(id, 2)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        ((VideoStudyDetialActivity) activity).updateCollect(false);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapterq, View view, int position) {
                super.onItemChildClick(adapterq, view, position);
                commentPos = position;
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

    public void loadMore() {
        pageNo++;
        testData();
    }

    @BindingAdapter("loadContent")
    public static void loadContent(TextViewForFullHtml textViewForFullHtml, String content) {
        textViewForFullHtml.loadContent(content);
    }


    @Bindable
    public RxSpeechInfo getVideoInfo() {
        return videoInfo.get();
    }

    public void setVideoInfo(RxSpeechInfo videoInfo) {
        this.videoInfo.set(videoInfo);
        notifyPropertyChanged(BR.videoInfo);
    }

    @Override
    public void destroy() {

    }
}
