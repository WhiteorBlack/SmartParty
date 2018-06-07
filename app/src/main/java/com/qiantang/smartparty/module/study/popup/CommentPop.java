package com.qiantang.smartparty.module.study.popup;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.modle.RxStudyList;
import com.qiantang.smartparty.modle.RxStudyZan;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.widget.CommentPopAnim;

/**
 * Created by zhaoyong bai on 2018/6/7.
 */
public class CommentPop extends BasePopup<CommentPop> implements View.OnClickListener {
    private ImageView ivLike;
    private TextView mLikeText;
    private View view;
    private Handler handler;
    private ScaleAnimation mScaleAnimation;
    private RxStudyList mMomentsInfo;
    private boolean hasLiked;
    private OnCommentPopupClickListener mOnCommentPopupClickListener;
    private CommentPopAnim slideRightEnter;

    public OnCommentPopupClickListener getmOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setmOnCommentPopupClickListener(OnCommentPopupClickListener mOnCommentPopupClickListener) {
        this.mOnCommentPopupClickListener = mOnCommentPopupClickListener;
    }

    public CommentPop(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreatePopupView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.popup_comment, null, false);
        ivLike = view.findViewById(R.id.iv_like);
        mLikeText = view.findViewById(R.id.tv_like);
        AutoUtils.auto(view);
        return view;
    }

    @Override
    public void setUiBeforShow() {
        handler = new Handler();
        view.findViewById(R.id.item_comment).setOnClickListener(this);
        view.findViewById(R.id.item_like).setOnClickListener(this);
        buildAnima();
        mOnCreateView.setClickable(false);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        slideRightEnter = new CommentPopAnim();
//        showAnim(slideRightEnter);
    }


    @Override
    public CommentPop offset(float xOffset, float yOffset) {
        return super.offset(xOffset, yOffset);
    }

    @Override
    public CommentPop anchorView(View anchorView) {
        return super.anchorView(anchorView);
    }

    private void buildAnima() {
        mScaleAnimation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(300);
        mScaleAnimation.setInterpolator(new SpringInterPolator());
        mScaleAnimation.setFillAfter(false);

        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void updateMomentInfo(@NonNull RxStudyList info) {
        this.mMomentsInfo = info;
        hasLiked = false;
        if (info.getZanAppMap() != null && info.getZanAppMap().size() > 0) {
            for (RxStudyZan likesInfo : info.getZanAppMap()) {
                if (TextUtils.equals(likesInfo.getUser_id(), MyApplication.USER_ID)) {
                    hasLiked = true;
                    break;
                }
            }
        }
        mLikeText.setText(hasLiked ? "取消" : "赞");
    }

    @Override
    public CommentPop showAnim(BaseAnimatorSet showAnim) {
        return super.showAnim(showAnim);
    }

    @Override
    public CommentPop dismissAnim(BaseAnimatorSet dismissAnim) {
        return super.dismissAnim(dismissAnim);
    }

    @Override
    public void showAtLocation(int x, int y) {
        offset(-40, -28);
        super.showAtLocation(x, y);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(view, mMomentsInfo, hasLiked);
                    ivLike.clearAnimation();
                    ivLike.startAnimation(mScaleAnimation);
                }
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(view, mMomentsInfo);
                    dismiss();
                }
                break;
        }
    }

    static class SpringInterPolator extends LinearInterpolator {

        public SpringInterPolator() {
        }


        @Override
        public float getInterpolation(float input) {
            return (float) Math.sin(input * Math.PI);
        }
    }

    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, @NonNull RxStudyList info, boolean hasLiked);

        void onCommentClick(View v, @NonNull RxStudyList info);
    }

}
