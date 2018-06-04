package com.qiantang.smartparty.module.index.adapter;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.widget.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by zhaoyong bai on 2018/5/24.
 */
public class VideoStudyAdapter extends EasyBindQuickAdapter<RxVideoStudy> {

    private StandardGSYVideoPlayer curPlayer;
    public static final String TAG = "VidioStudyAdapter";
    protected OrientationUtils orientationUtils;

    protected boolean isPlay;

    protected boolean isFull;

    public VideoStudyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxVideoStudy item) {
        SampleCoverVideo sampleCoverVideo = holder.getBinding().getRoot().findViewById(R.id.sdv);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sampleCoverVideo.getLayoutParams();
        params.height = MyApplication.widthPixels * 214 / 375;
        sampleCoverVideo.setLayoutParams(params);
        sampleCoverVideo.setUpLazy(item.getVideoUrl(), false, null, null, item.getTitle());
        sampleCoverVideo.getTitleTextView().setVisibility(View.GONE);
        sampleCoverVideo.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        sampleCoverVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(sampleCoverVideo);
            }
        });
        sampleCoverVideo.setRotateViewAuto(!getListNeedAutoLand());
        sampleCoverVideo.setLockLand(!getListNeedAutoLand());
        sampleCoverVideo.setPlayTag(TAG);
        sampleCoverVideo.setAutoFullWithSize(true);
        sampleCoverVideo.setReleaseWhenLossAudio(false);
        sampleCoverVideo.setShowFullAnimation(!getListNeedAutoLand());
        sampleCoverVideo.setIsTouchWiget(false);
        //循环
        sampleCoverVideo.setLooping(false);
        sampleCoverVideo.setNeedLockFull(true);

        sampleCoverVideo.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                boolean full = sampleCoverVideo.getCurrentPlayer().isIfCurrentIsFullscreen();
                if (!sampleCoverVideo.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setNeedMute(true);
                }
                curPlayer = (StandardGSYVideoPlayer) objects[1];
                isPlay = true;
                if (getListNeedAutoLand()) {
                    //重力全屏工具类
                    initOrientationUtils(sampleCoverVideo, full);
                    VideoStudyAdapter.this.onPrepared();
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                isFull = false;
                GSYVideoManager.instance().setNeedMute(true);
                if (getListNeedAutoLand()) {
                    VideoStudyAdapter.this.onQuitFullscreen();
                }
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                isFull = true;
                sampleCoverVideo.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                curPlayer = null;
                isPlay = false;
                isFull = false;
                if (getListNeedAutoLand()) {
                    VideoStudyAdapter.this.onAutoComplete();
                }
            }
        });
        sampleCoverVideo.setPlayPosition(holder.getAdapterPosition());

        holder.addOnClickListener(R.id.tv_name);
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        if (getListNeedAutoLand() && orientationUtils != null) {
            resolveFull();
        }
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, true);
    }

    public void clearCache() {
        if (curPlayer != null) {
            curPlayer.getCurrentPlayer().clearCurrentCache();
        }
    }

    public boolean isFull() {
        return isFull;
    }

    /**************************支持全屏重力全屏的部分**************************/

    /**
     * 列表时是否需要支持重力旋转
     *
     * @return 返回true为支持列表重力全屏
     */
    public boolean getListNeedAutoLand() {
        return false;
    }

    private void initOrientationUtils(StandardGSYVideoPlayer standardGSYVideoPlayer, boolean full) {
        orientationUtils = new OrientationUtils((Activity) mContext, standardGSYVideoPlayer);
        //是否需要跟随系统旋转设置
        //orientationUtils.setRotateWithSystem(false);
        orientationUtils.setEnable(false);
        orientationUtils.setIsLand((full) ? 1 : 0);
    }

    private void resolveFull() {
        if (getListNeedAutoLand() && orientationUtils != null) {
            //直接横屏
            orientationUtils.resolveByClick();
        }
    }

    private void onQuitFullscreen() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onAutoComplete() {
        if (orientationUtils != null) {
            orientationUtils.setEnable(false);
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
        isPlay = false;
    }

    public void onPrepared() {
        if (orientationUtils == null) {
            return;
        }
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(true);
    }

    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        //如果旋转了就全屏
        if (isPlay && curPlayer != null && orientationUtils != null) {
            curPlayer.onConfigurationChanged(activity, newConfig, orientationUtils, false, true);
        }
    }

    public OrientationUtils getOrientationUtils() {
        return orientationUtils;
    }


    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onDestroy() {
        if (isPlay && curPlayer != null) {
            curPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
    }

}
