package com.qiantang.smartparty.widget;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideRightEnter;

/**
 * Created by zhaoyong bai on 2018/6/7.
 */
public class CommentPopAnim extends SlideRightEnter {
    @Override
    protected void start(View view) {
        super.start(view);
    }

    @Override
    public void setAnimation(View view) {

        view.setPivotX(view.getTranslationX());
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 250 * dm.density, 0),
                ObjectAnimator.ofFloat(view, "alpha", 1.0f, 1));
    }
}
