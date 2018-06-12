package com.qiantang.smartparty.module.web.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.module.web.view.HeadWebActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

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

    public WebHeadViewModel(BaseBindActivity activity, CommentAdapter commentAdapter) {
        this.activity = activity;
        this.commentAdapter = commentAdapter;
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
        ApiWrapper.getInstance().fcNoticeDetails(pageNo, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxActivityDetial>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        commentAdapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxActivityDetial data) {
                        commentCount = data.getCount();
                        ((HeadWebActivity) activity).updateCount(data.getCount());
                        if (addCommentCount > 0) {
                            List<RxComment> comments = commentAdapter.getData();
                            for (int i = 0; i < addCommentCount; i++) {
                                comments.remove(comments.size() - 1);
                            }
                            commentAdapter.notifyDataSetChanged();
                            addCommentCount=0;
                        }
                        commentAdapter.setPagingData(data.getPl(), pageNo);
                    }
                });
    }


    public void comment(String content) {
        isDealing = true;
        ApiWrapper.getInstance().comment(content, id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        addCommentCount++;
                        commentCount+=1;
                        ((HeadWebActivity) activity).updateCount(commentCount);
                        ((HeadWebActivity) activity).dissmissCommentBox();
                        RxComment rxComment = new RxComment();
                        rxComment.setUsername(MyApplication.mCache.getAsString(CacheKey.USER_NAME));
                        rxComment.setUserId(MyApplication.mCache.getAsString(CacheKey.USER_ID));
                        rxComment.setContent(content);
                        rxComment.setAvatar(MyApplication.mCache.getAsString(CacheKey.USER_AVATAR));
                        rxComment.setIsDz(0);
                        rxComment.setDz(0);
                        rxComment.setCreationtime(AppUtil.getNowDate());
                        commentAdapter.getData().add(commentAdapter.getData().size(), rxComment);
                        commentAdapter.notifyItemInserted(commentAdapter.getData().size());
                    }
                });
    }

    public void commentLike(String id) {
        isDealing = true;
        ApiWrapper.getInstance().commentLike(1, id, "")
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        commentAdapter.getData().get(commentPos).setIsDz(1);
                        commentAdapter.getData().get(commentPos).setDz(commentAdapter.getData().get(commentPos).getDz() + 1);
                        commentAdapter.notifyItemChanged(commentPos + 1);

                    }
                });
    }


    private void cancelLike(String id) {
        isDealing = true;
        ApiWrapper.getInstance().cancelLike(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        //取消点赞成功
                        commentAdapter.getData().get(commentPos).setIsDz(0);
                        commentAdapter.getData().get(commentPos).setDz(commentAdapter.getData().get(commentPos).getDz() - 1);
                        commentAdapter.notifyItemChanged(commentPos + 1);
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
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.iv_praise:
                        cancelLike(commentAdapter.getData().get(position).getContentId());
                        break;
                    case R.id.iv_unpraise:
                        commentLike(commentAdapter.getData().get(position).getContentId());
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
