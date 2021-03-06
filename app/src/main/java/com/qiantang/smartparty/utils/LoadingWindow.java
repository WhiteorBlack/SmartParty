package com.qiantang.smartparty.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qiantang.smartparty.R;
import com.qiantang.smartparty.module.main.view.MainActivity;
import com.qiantang.smartparty.widget.textloading.AnimatedView;
import com.qiantang.smartparty.widget.textloading.AnimatorPlayer;
import com.qiantang.smartparty.widget.textloading.HesitateInterpolator;
import com.qiantang.smartparty.widget.textloading.ProgressLayout;


public class LoadingWindow {

    private final Activity activity;
    private PopupWindow popWindow;
    private View windowView;
    private ProgressLayout progressLayout;

    private int size;
    private AnimatedView[] spots;
    private AnimatorPlayer animator;
    private CharSequence message;
    private static final int DELAY = 150;
    private static final int DURATION = 1500;
    private String text = "加载中...";
    private TextView tvLoading;
    private Handler handler = new Handler();

    public LoadingWindow(Activity activity) {
        this.activity = activity;
        createProgressWindow();
    }

    private void createProgressWindow() {
        windowView = activity.getLayoutInflater().inflate(R.layout.popupwindow_layout, null, false);
//        AutoUtils.auto(windowView);
        windowView.setFocusable(true);
        windowView.setFocusableInTouchMode(true);
        popWindow = new PopupWindow(windowView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOnDismissListener(() -> backgroundAlpha(1f));
//        rotatable = new Rotatable.Builder(windowView.findViewById(R.id.pb)).direction(Rotatable.ROTATE_Y).build();
        progressLayout = windowView.findViewById(R.id.progress);
        tvLoading = windowView.findViewById(R.id.tv_loading);
//        initProgress();

    }

    int i = 1;
    int color = R.color.white;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, 100);
            if (i > 5) {
                i = 1;
            }
            switch (i) {

            }
            tvLoading.setTextColor(activity.getResources().getColor(color));
            i++;
        }
    };

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    public void showWindow() {
        if (popWindow == null) {
            createProgressWindow();
        }
        if (!popIsShow()) {
            try {
                backgroundAlpha(0.5f);
                popWindow.showAtLocation(windowView, Gravity.CENTER, 0, 0);
                handler.postDelayed(runnable, 50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean popIsShow() {
        return popWindow.isShowing();
    }

    public void delayedShowWindow() {
        delayedShowWindow(100);
    }

    public void delayedShowWindow(int t) {
        MainActivity.handler.postDelayed(this::showWindow, t);
    }

    public void hidWindow() {
//        if (popWindow != null && popIsShow()) {
//            popWindow.dismiss();
//            animationDrawable.stop();
//            handler.removeCallbacks(runnable);
//        }
        delayHideWindow();
    }

    public void delayHideWindow(){
        MainActivity.handler.postDelayed(this::dismiss, 150);
    }

    public void dismiss() {
        if (popWindow != null && popIsShow()) {
//            animationDrawable.stop();
            handler.removeCallbacks(runnable);
            popWindow.dismiss();
        }
    }

    private void initProgress() {
        animator = new AnimatorPlayer(createAnimations());
        size = text.length();
        int progressWidth = activity.getResources().getDimensionPixelSize(R.dimen.progress_width);
        spots = new AnimatedView[size];
        for (int i = 0; i < text.length(); i++) {
            AnimatedView v = new AnimatedView(activity);
            char charString = text.charAt(i);
            v.setText(charString + "");
            v.setTarget(progressWidth);
            v.setTextColor(Color.WHITE);
            v.setXFactor(-1f);
            progressLayout.addView(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 1, -0.1f);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }
}
