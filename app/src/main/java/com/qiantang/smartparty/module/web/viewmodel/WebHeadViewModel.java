package com.qiantang.smartparty.module.web.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxAddScore;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.modle.RxSpecialDetial;
import com.qiantang.smartparty.module.index.view.VoiceSpeechDetialActivity;
import com.qiantang.smartparty.module.web.view.HeadWebActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.http.POST;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class WebHeadViewModel implements ViewModel {
    private BaseBindActivity activity;
    private CommentAdapter commentAdapter;
    private int pageNo = 1;
    private String id;
    private int commentPos = 0;
    private int commentCount = 0;
    private boolean isDealing = false;
    private int addCommentCount = 0;
    private int type = 0;
    private long startTime;
    public ObservableBoolean isFinish = new ObservableBoolean(false); //判断是否H5加载完毕,完毕之后在展示评论内容

    public WebHeadViewModel(BaseBindActivity activity, CommentAdapter commentAdapter) {
        this.activity = activity;
        this.commentAdapter = commentAdapter;
        initData();
    }

    private void initData() {
        startTime = System.currentTimeMillis();
        id = activity.getIntent().getStringExtra("id");
        type = activity.getIntent().getIntExtra("type", 0);
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo, false);
    }

    public void getData(int pageNo, boolean isRefresh) {
        this.pageNo = pageNo;
        if (type == 3) {
            //专题学习
            ApiWrapper.getInstance().specialDetails(pageNo, id)
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<RxSpecialDetial>() {
                        @Override
                        public void onFail(RetrofitUtil.APIException e) {
                            super.onFail(e);
                            commentAdapter.loadMoreEnd();
                            activity.refreshFail();
                        }

                        @Override
                        public void onSuccess(RxSpecialDetial data) {
                            activity.refreshOK();
                            commentCount = data.getDetail().getCommentSum();
                            ((HeadWebActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                            ((HeadWebActivity) activity).updateCount(data.getDetail().getCommentSum());
                            if (Config.isLoadMore) {
                                commentAdapter.setPagingData(data.getComment(), pageNo);
                            } else {

                                commentAdapter.setNewData(data.getComment());
                            }
                        }
                    });


        } else if (type == 4) {
            //理论在线
            ApiWrapper.getInstance().theoryDetails(pageNo, id)
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<RxSpecialDetial>() {
                        @Override
                        public void onFail(RetrofitUtil.APIException e) {
                            super.onFail(e);
                            commentAdapter.loadMoreEnd();
                            activity.refreshFail();
                        }

                        @Override
                        public void onSuccess(RxSpecialDetial data) {
                            activity.refreshOK();
                            commentCount = data.getDetail().getCommentSum();
                            ((HeadWebActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                            ((HeadWebActivity) activity).updateCount(data.getDetail().getCommentSum());
                            if (Config.isLoadMore) {
                                commentAdapter.setPagingData(data.getComment(), pageNo);
                            } else {
                                commentAdapter.setNewData(data.getComment());
                            }
                        }
                    });
        } else {
            ApiWrapper.getInstance().fcNoticeDetails(pageNo, id)
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<RxActivityDetial>() {
                        @Override
                        public void onFail(RetrofitUtil.APIException e) {
                            super.onFail(e);
                            commentAdapter.loadMoreFail();
                            activity.refreshFail();
                        }

                        @Override
                        public void onSuccess(RxActivityDetial data) {
                            activity.refreshOK();
                            commentCount = data.getCount();
                            ((HeadWebActivity) activity).updateCollect(data.getDetials().getCollect() != 0);
                            ((HeadWebActivity) activity).updateCount(data.getCount());
                            if (addCommentCount > 0) {
                                List<RxComment> comments = commentAdapter.getData();
                                for (int i = 0; i < addCommentCount; i++) {
                                    comments.remove(comments.size() - 1);
                                }
                                commentAdapter.notifyDataSetChanged();
                                addCommentCount = 0;
                            }
                            if (Config.isLoadMore) {
                                commentAdapter.setPagingData(data.getPl(), pageNo);
                            } else {
                                commentAdapter.setNewData(data.getPl());
                            }
                        }
                    });
        }
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
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        getData(pageNo + 1, false);
                        EventBus.getDefault().post(new RxAddScore(CacheKey.COMMENT, 0, id));
//                        addCommentCount++;
//                        commentCount+=1;
//                        ((HeadWebActivity) activity).updateCount(commentCount);
//                        ((HeadWebActivity) activity).dissmissCommentBox();
//                        RxComment rxComment = new RxComment();
//                        rxComment.setUsername(MyApplication.mCache.getAsString(CacheKey.USER_NAME));
//                        rxComment.setUserId(MyApplication.mCache.getAsString(CacheKey.USER_ID));
//                        rxComment.setContent(content);
//                        rxComment.setAvatar(MyApplication.mCache.getAsString(CacheKey.USER_AVATAR));
//                        rxComment.setIsDz(0);
//                        rxComment.setDz(0);
//                        rxComment.setCreationtime(AppUtil.getNowDate());
//                        commentAdapter.getData().add(commentAdapter.getData().size(), rxComment);
//                        commentAdapter.notifyItemInserted(commentAdapter.getData().size());

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
                        if (type == 3 || type == 4) {
                            commentAdapter.getData().get(commentPos).setIszan(1);
                            commentAdapter.getData().get(commentPos).setThumbs(commentAdapter.getData().get(commentPos).getThumbs() + 1);
                        } else {
                            commentAdapter.getData().get(commentPos).setIsDz(1);
                            commentAdapter.getData().get(commentPos).setDz(commentAdapter.getData().get(commentPos).getDz() + 1);
                        }

                        commentAdapter.notifyItemChanged(commentPos + 1);

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
                        if (type == 3 || type == 4) {
                            commentAdapter.getData().get(commentPos).setIszan(0);
                            commentAdapter.getData().get(commentPos).setThumbs(commentAdapter.getData().get(commentPos).getThumbs() - 1);
                        } else {
                            commentAdapter.getData().get(commentPos).setIsDz(0);
                            commentAdapter.getData().get(commentPos).setDz(commentAdapter.getData().get(commentPos).getDz() - 1);
                        }

                        commentAdapter.notifyItemChanged(commentPos + 1);
                    }
                });
    }

    /**
     * 收藏
     */
    public void prase() {
        ApiWrapper.getInstance().collectSave(id, type)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        ((HeadWebActivity) activity).updateCollect(true);
                    }
                });
    }

    public void cancelPrase() {
        ApiWrapper.getInstance().collectAbolish(id, type)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        ((HeadWebActivity) activity).updateCollect(false);
                    }
                });
    }


    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                commentPos = position;
                String id = "";
                if (type == 3 || type == 4) {
                    id = commentAdapter.getData().get(position).getComment_id();
                } else {
                    id = commentAdapter.getData().get(position).getContentId();
                }
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.iv_praise:
                        cancelLike(id);
                        break;
                    case R.id.iv_unpraise:
                        commentLike(id);
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {
        EventBus.getDefault().post(new RxAddScore(CacheKey.READ, (int) (System.currentTimeMillis() - startTime), id));
    }
}
