package com.qiantang.smartparty.module.study.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxMyStudy;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyComment;
import com.qiantang.smartparty.modle.RxStudyList;
import com.qiantang.smartparty.modle.RxStudyUserMap;
import com.qiantang.smartparty.modle.RxStudyZan;
import com.qiantang.smartparty.module.study.adapter.StudyAdapter;
import com.qiantang.smartparty.module.study.adapter.StudyMyAdapter;
import com.qiantang.smartparty.module.study.popup.CommentPop;
import com.qiantang.smartparty.module.study.view.StudyFragment;
import com.qiantang.smartparty.module.study.view.StudyMyActivity;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class StudyMyViewModel extends BaseObservable implements ViewModel, CommentPop.OnCommentPopupClickListener {
    private BaseBindActivity fragment;
    private ObservableField<RxStudyUserMap> userMap = new ObservableField<>();
    private StudyAdapter adapter;
    private int pageNo = 1;
    private int delPos;
    private boolean isDeleting = false;
    private boolean isDealing = false;
    private int commentPos;
    private CommentPop commentPop;

    public StudyMyViewModel(BaseBindActivity fragment, StudyAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
        commentPop = new CommentPop(fragment);
        commentPop.setmOnCommentPopupClickListener(this);
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public void getData(int i) {
        if (i == 1) {
            pageNo = 1;
        }
        ApiWrapper.getInstance().getMyStudyList(i)
                .compose(fragment.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyStudy>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        fragment.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxMyStudy data) {
                        fragment.refreshOK();
                        adapter.setPagingData(data.getResultList(), pageNo);
                    }
                });
    }

    private void deleteComment(String id) {
        ApiWrapper.getInstance().deleteComment(id)
                .compose(fragment.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDeleting = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        adapter.getData().remove(delPos);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void commentLike(String id, int type, String content) {
        isDealing = true;
        ApiWrapper.getInstance().commentLike(type, id, content)
                .compose(fragment.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        if (type == 1) {
                            //点赞成功
                            adapter.getData().get(commentPos).getZanAppMap().add(new RxStudyZan(adapter.getData().get(commentPos).getUsername(), "", MyApplication.USER_ID));
                        }
                        if (type == 2) {
                            adapter.getData().get(commentPos).getCommentAppMap().add(new RxStudyComment(adapter.getData().get(commentPos).getUsername(), content, MyApplication.USER_ID));
                        }
                        adapter.notifyItemChanged(commentPos + 1);
                    }
                });
    }

    private void cancelLike(String id) {
        isDealing = true;
        ApiWrapper.getInstance().cancelLike(id)
                .compose(fragment.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isDealing = false)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {

                        //取消点赞成功
                        List<RxStudyZan> zanList = adapter.getData().get(commentPos).getZanAppMap();
                        for (int i = 0; i < zanList.size(); i++) {
                            if (TextUtils.equals(MyApplication.USER_ID, zanList.get(i).getUser_id())) {
                                zanList.remove(i);
                                break;
                            }
                        }
                        adapter.notifyItemChanged(commentPos+1);
                    }
                });
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapterT, View view, int position) {
                super.onItemChildClick(adapterT, view, position);
                switch (view.getId()) {
                    case R.id.tv_del:
                        if (isDeleting) {
                            return;
                        }
                        delPos = position;
                        deletePop(adapter.getData().get(position).getComment_id());
                        break;
                    case R.id.iv_comment:
                        if (isDealing) {
                            return;
                        }
                        commentPos = position;
                        if (commentPop != null) {
                            commentPop.updateMomentInfo(adapter.getData().get(position));
                            commentPop.anchorView(view);
                            commentPop.showAtLocation(0, 0);
                        }
                        break;
                }
            }
        };
    }

    /**
     * 删除感悟弹窗
     */
    private void deletePop(String id) {
        ActionSheet.createBuilder(fragment, fragment.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("确定删除")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        isDeleting = true;
                        deleteComment(id);
                    }
                }).show();
    }


    @Override
    public void onLikeClick(View v, @NonNull RxStudyList info, boolean hasLiked) {
        if (hasLiked) {
            cancelLike(info.getComment_id());
        } else {
            commentLike(info.getComment_id(), 1, "");
        }
    }

    @Override
    public void onCommentClick(View v, @NonNull RxStudyList info) {
        View view = adapter.getViewByPosition(commentPos + 1, R.id.iv_comment);
        ((StudyMyActivity) fragment).showCommentBox(view, commentPos, info.getComment_id(), null);
    }


    @Override
    public void destroy() {

    }
}
