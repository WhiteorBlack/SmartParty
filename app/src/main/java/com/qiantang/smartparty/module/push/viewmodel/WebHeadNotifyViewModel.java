package com.qiantang.smartparty.module.push.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseNotifyBindActivity;
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
import com.qiantang.smartparty.modle.ShareInfo;
import com.qiantang.smartparty.module.push.view.HeadWebNotifyActivity;
import com.qiantang.smartparty.module.web.view.HeadWebActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.dialog.ShareBottomDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/11.
 */
public class WebHeadNotifyViewModel implements ViewModel {
    private BaseNotifyBindActivity activity;
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
    private String title = "";
    private String content = "";
    private String url = "";
    private ShareBottomDialog shareDialog;
    private String imgUrl = "";

    public WebHeadNotifyViewModel(BaseNotifyBindActivity activity, CommentAdapter commentAdapter) {
        this.activity = activity;
        this.commentAdapter = commentAdapter;
//        initData();
    }

    public void initData(String id, int type, String url, String imgUrl) {
        startTime = System.currentTimeMillis();
        this.id = id;
        this.type = type;
        this.url = url;
        this.imgUrl = imgUrl;
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
                            title = data.getDetail().getTitle();
                            content = data.getDetail().getContent();
                            ((HeadWebNotifyActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                            ((HeadWebNotifyActivity) activity).updateCount(data.getDetail().getCommentSum());
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
                            title = data.getDetail().getTitle();
                            content = data.getDetail().getContent();
                            commentCount = data.getDetail().getCommentSum();
                            ((HeadWebNotifyActivity) activity).updateCollect(data.getDetail().getCollect() != 0);
                            ((HeadWebNotifyActivity) activity).updateCount(data.getDetail().getCommentSum());
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
                            title = data.getDetials().getTitle();
                            content = data.getDetials().getContent();
                            commentCount = data.getCount();
                            ((HeadWebNotifyActivity) activity).updateCollect(data.getDetials().getCollect() != 0);
                            ((HeadWebNotifyActivity) activity).updateCount(data.getCount());
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
                        ((HeadWebNotifyActivity) activity).updateCollect(true);
                    }
                });
    }

    public void cancelPrase() {
        ApiWrapper.getInstance().collectAbolish(id, type)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        ((HeadWebNotifyActivity) activity).updateCollect(false);
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

    public void share() {
        ShareInfo shareInfo = new ShareInfo("", title, Config.IMAGE_HOST + imgUrl, url + id, "智慧党建-党员在线学习平台\n");
        if (shareDialog == null) {
            shareDialog = new ShareBottomDialog(activity, shareInfo);
        } else {
            shareDialog.setShareInfo(shareInfo);
        }
        shareDialog.show();
    }

    @Override
    public void destroy() {
        EventBus.getDefault().post(new RxAddScore(CacheKey.READ, (int) (System.currentTimeMillis() - startTime), id));
    }
}
