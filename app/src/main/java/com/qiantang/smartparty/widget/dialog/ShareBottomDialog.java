package com.qiantang.smartparty.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.DialogShareBinding;
import com.qiantang.smartparty.modle.ShareInfo;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.StringUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> {

    private ShareInfo shareInfo;
    private Context context;
    private UMShareListener umShareListener;
    private DialogShareBinding binding;

    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public ShareBottomDialog(Context context, ShareInfo info) {
        super(context);
        this.context = context;
        this.shareInfo = info;

        Log.e("2222", "ShareInfo: " + info);
        this.umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
//                ToastUtil.toast("分享成功");

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        };
    }

    public void setShareInfo(ShareInfo info) {
        this.shareInfo = info;
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_share, null, false);
        AutoUtils.auto(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void setUiBeforShow() {
        binding.llWechatCircle.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.WEIXIN_CIRCLE));
        binding.llWechat.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.WEIXIN));
        binding.llQq.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.QQ));
        binding.llQzone.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.QZONE));
    }

    private void sharePlatform(SHARE_MEDIA media) {
        dismiss();
        ShareAction shareAction = new ShareAction((Activity) context);
        UMWeb web = new UMWeb(shareInfo.getRawUrl());
        if (!StringUtil.isEmpty(shareInfo.getImgUrl())) {
//            shareAction.withMedia(new UMImage(context, shareInfo.getImgUrl()));
            web.setThumb(new UMImage(context, shareInfo.getImgUrl()));
        }
        web.setTitle(shareInfo.getTitle());
        web.setDescription(shareInfo.getDescribe());
        shareAction
                .withText(shareInfo.getTitle())
                .setPlatform(media)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }
}
