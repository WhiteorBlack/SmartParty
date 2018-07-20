package com.qiantang.smartparty.module.push.viewmodel;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseNotifyBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.module.push.view.HeadWebNotifyActivity;
import com.qiantang.smartparty.module.web.view.HeadWebActivity;
import com.qiantang.smartparty.utils.WebUtil;

/**
 * Created by zhaoyong bai on 2018/6/10.
 */
public class HeadWebNotifyViewModel extends BaseObservable implements ViewModel {
    private BaseNotifyBindActivity activity;
    private AgentWeb mAgentWeb;
    private String url = "";
    private ObservableInt commentCount = new ObservableInt(0);
    public ObservableBoolean isFinish = new ObservableBoolean(false); //判断是否H5加载完毕,完毕之后在展示评论内容
    public ObservableInt progress=new ObservableInt(0);

    public HeadWebNotifyViewModel(BaseNotifyBindActivity activity) {
        this.activity = activity;
        initData();
    }

    private void initData() {
        url = activity.getIntent().getStringExtra("url") + activity.getIntent().getStringExtra("id");
    }

    public void initWev(ViewGroup viewGroup) {
        mAgentWeb = AgentWeb.with(activity)
                .setAgentWebParent(viewGroup, new LinearLayout.LayoutParams(-1, -1))
//                .useDefaultIndicator(activity.getResources().getColor(R.color.barColor), 1)
                .closeIndicator()
                .setWebChromeClient(mWebChromeClient)
                .addJavascriptInterface("qiantang", new JsToAndroid())
                .setWebViewClient(getWebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .go(url);
        mAgentWeb.getWebCreator().getWebView().setLayerType(View.LAYER_TYPE_NONE, null);
//        setUrl(url);
    }

    public void setUrl(String url) {
        mAgentWeb.getUrlLoader().loadUrl(url);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress.set(newProgress);
            if (newProgress==100){
                mAgentWeb.getIndicatorController().finish();
//                mAgentWeb.getIndicatorController().offerIndicator().hide();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };


    public WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFinish.set(true);
                ((HeadWebNotifyActivity) activity).loadEnd(true);
                if (view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
                view.getSettings().setBlockNetworkImage(false);
                Logger.e("onPageFinished");
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                Logger.e("onPageCommitVisible");
            }

            @Override //网页加载 禁止在浏览器打开在本应用打开
            public boolean shouldOverrideUrlLoading(WebView web, String url) {
                if (!url.contains("http://")) {
                    if (url.contains("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                    return true;
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                // ------5.0以上手机执行------
                Uri uri = request.getUrl();
                String url = uri.toString();
                return shouldInterceptRequest(view, url);
            }


        };
    }

    @Bindable
    public int getCommentCount() {
        return commentCount.get();
    }

    public void setCommentCount(int commentCount) {
        this.commentCount.set(commentCount);
        notifyPropertyChanged(BR.commentCount);
    }

    @Override
    public void destroy() {
        mAgentWeb.clearWebCache();
        mAgentWeb.destroy();
    }

    private class JsToAndroid {

        /**
         * 跳转新页面
         *
         * @param data
         */
        @JavascriptInterface
        public void jumpNewPage(String data) {
            if (TextUtils.isEmpty(data)) {
                return;
            }
            WebUtil.jumpWeb(activity, data, "阅读原文");
        }
    }
}
